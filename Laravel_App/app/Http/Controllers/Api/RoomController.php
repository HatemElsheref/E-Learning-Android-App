<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\LectureResource;
use App\Lecture;
use App\Room;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class RoomController extends Controller
{
    public function __construct(){
         $this->middleware('auth:api');
    }
    public function report(){
        $user=auth('api')->user()->id;
        $lectures=Lecture::all()->count();
        $instructor_rooms=Room::where('user_id',$user)->count();
        $student_rooms=DB::table("room_user")->where("user_id",$user)->count();
        return response()->json(['lectures'=>$lectures,'instructor_rooms'=>$instructor_rooms,'student_rooms'=>$student_rooms],200);

    }

    public function students_in_room($id){
        $students=DB::table("room_user")->where("room_id",$id)->get();
        if ($students){

            //dd($students);
            return response()->json(['data'=>$students],200);
        }   else{
            return response()->json(['data'=>null],200);
        }
    }
    public function join($code){
        $room=Room::where('code',$code)->first();
        if ($room){
            $students=DB::table("room_user")->insert(['user_id'=>auth('api')->user()->id,'room_id'=>$room->id]);
            return response(['message'=>'you joined class successfully'],200);
        }
        return response(['message'=>'Failed to join class'],404);

    }
    public function exit($id){
        $room=Room::find($id);
        if ($room){
            $user=auth('api')->user()->id;
            $data=DB::table("room_user")->where("user_id",$user)->where('room_id',$id)->delete();
            return response(['message'=>'you Exited from class successfully'],200);
        }
        return response(['message'=>'Failed to exit from class'],404);
    }
    public function student_rooms(){
        $user=auth('api')->user()->id;
        $data=DB::table("room_user")->where("user_id",$user)->get('room_id');
        $room=Room::whereIn('id',$data->pluck("room_id"))->get();

        return response()->json($room);
    }


    //function used by instructor
   public function getRoomLecture($id){
       $students=DB::table("room_user")->where("room_id",$id)->get();
       return $students;
   }
   public function getLectures($id){
       // retrive all lectures in this classs
       $room=Room::find($id);
       if ($room){
           $lectures=Lecture::where('room_id',$id)->get();
           return  LectureResource::collection($lectures);
       }
       return response()->json(['message'=>"Room Not Found"],404);
   }
    public function getRooms(){
        $ROOMS=Room::where("user_id",auth("api")->user()->id)->get();
        return response()->json($ROOMS);
    }
    public function store(Request $request){

       $validated_data=Validator::make($request->all(),[
           'name'=>'required',
       ],[
           'name.required'=>'class name is required',
       ]);

       if ($validated_data->fails()){
           return response()->json("Invalid Operation",207);
       } else{
           $roomCode=Str::random(20);
            $room=Room::create([
                'name'=>$request->name,
                'code'=>$roomCode,
                'user_id'=>auth('api')->user()->id
            ]);
            if ($room){


                return response()->json(['message'=>"Room Created Successfully",'room_code'=>$roomCode],200);
            }
            return  response()->json("invalid operation ",207);
       }

    }
    public function update(Request $request,$id){
        $validated_data=Validator::make($request->all(),[
            'name'=>'required',
        ],[
            'name.required'=>'class name is required',
        ]);

        if ($validated_data->fails()){
            return response()->json("Invalid Operation",207);
        } else{
            $room=Room::find($id);
            if ($room){
                //$roomCode=Str::random(20);
                $room->update([
                    'name'=>$request->name,
                    //'code'=>$room->code,
                   // 'user_id'=>auth('api')->user()->id
                ]);
                return response()->json(['message'=>"Room Updated Successfully",'room_code'=>$room->code],200);
            }
            return  response()->json("Class Room Not Found",404);

        }

    }
    public function destroy($id){
        $room=Room::find($id);

      if ($room){
          $firebase_file_paths=$room->lectures;
          $paths=[];
          foreach ($firebase_file_paths as $path){
              array_push($paths,$path->path);
          }
          $room->delete();
          Lecture::where('room_id',$id)->delete();
          return response()->json(['result'=>'class removed successfully','paths'=>$paths],200);
      }
        return response()->json(['result'=>'Failed Operation'],207);
    }

}
