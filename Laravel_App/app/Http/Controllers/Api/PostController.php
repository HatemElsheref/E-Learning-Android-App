<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Post;
use http\Env\Response;
use Illuminate\Http\Request;
use App\Http\Resources\PostResource;
use Illuminate\Support\Facades\Validator;

class PostController extends Controller
{
    use ResponseFormater;

    public function __construct()
    {
        $this->middleware('auth:api')->except(['show','index']);
    }

    public function index(){
        $posts=Post::orderBy('created_at','DESC')->paginate(10);
        return response()->json(self::Format_Collection(PostResource::collection($posts)));
    }
    public function show($id){
       if ($post=Post::find($id)){
         return self::Formater(200,$post);
       }
       return  self::Formater(404,null);
    }
    public function store(Request $request){
        $validated_data=Validator::make($request->all(),[
           'title'=>"required|string|max:191|unique:posts",
           'body'=>'required'
        ],[
            'title.required'=>'Post Title Is Required',
            'title.unique'=>'Post Title Must Be Unique',
            'title.max'=>'Post Title Must Be Less Than Or Equal 191',
            'body.required'=>'Post Body Is Required',
        ]);
        if ($validated_data->fails()){
            return Response()->json(self::Format_Validation_Error($validated_data->errors()));
        }
        $post=Post::create([
            'title'=>$request->title,
            'body'=>$request->body,
            'user_id'=>auth('api')->user()->id
        ]);
        if ($post){
            return self::Formater(200,$post);
        }
        return self::Formater(401,null);
    }
    public function update(Request $request,$id){
        $post=Post::find($id);

        $validated_data=Validator::make($request->all(),[
            'title'=>"required|string|max:191|unique:posts,id,$id",
            'body'=>'required'
        ],[
            'title.required'=>'Post Title Is Required',
            'title.unique'=>'Post Title Must Be Unique',
            'title.max'=>'Post Title Must Be Less Than Or Equal 191',
            'body.required'=>'Post Body Is Required',
        ]);
        if ($validated_data->fails()){
            return Response()->json(self::Format_Validation_Error($validated_data->errors()));
        }

        if ($post){
            $post->update([
                'title'=>$request->title,
                'body'=>$request->body,
                'user_id'=>auth('api')->user()->id
            ]);
            if ($post){
                return self::Formater(200,$post);
            }
        }
        return response()->json(self::Formater(404,null));

    }
    public function destroy($id){
        $post=Post::find($id);
        if ($post){
            $post->delete();
            return response()->json(self::Formater(200,'Success Delete'));
        }
        return response()->json(self::Formater(404,null));
    }
}
