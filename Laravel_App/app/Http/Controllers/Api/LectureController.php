<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Lecture;
use App\Room;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;

class LectureController extends Controller
{
    public function __construct(){
        $this->middleware('auth:api');
    }
    public function store(Request $request){

        $validated_data=Validator::make($request->all(),[
            'name'=>'required',
            'path'=>'required|unique:lectures,path',
            'room_id'=>'required',
        ],[
            'name.required'=>'Lecture name is required',
            'path.required'=>'Lecture Path is required',
            'path.unique'=>'Lecture Path Must Be Unique',
            'room_id.required'=>'Room Id is required',
        ]);

        if ($validated_data->fails()){
            return response()->json("Invalid Operation",207);
        } else{
            $lecture=Lecture::create([
                'name'=>$request->name,
                'path'=>$request->path,
                'room_id'=>$request->room_id
            ]);
            if ($lecture){

                //send notifications to all students in this room

                $students=DB::table('room_user')->where('room_id',$lecture->room_id)->pluck('user_id')->toArray();
                   $tokens=User::whereIn('id',$students)->get()->pluck('device_token')->toArray();
                //return array_values($tokens);
                $re=fcm()
                    ->to(array_values($tokens))
                    ->priority('high')
                    ->timeToLive(0)
                    ->data([
                        'title' => 'From Class '.Room::find($lecture->room_id)->name,
                        'body' => 'New Lecture Is Ready Now With Name '.$lecture->name,
                    ])
                    ->send();
                     return $re;

                return response()->json(['message'=>"Lecture  Uploaded Successfully"],200);
            }
            return  response()->json("invalid operation ",207);
        }

    }
    public function destroy($id){
        $lecture=Lecture::find($id);

        if ($lecture){
            $lecture->delete();
            return response()->json(['result'=>'lecture removed successfully'],200);
        }
        return response()->json(['result'=>'Failed Operation'],207);
    }
}
