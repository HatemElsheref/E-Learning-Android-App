<?php

namespace App\Http\Controllers\Api;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\User;
class AuthController extends Controller
{
    use ResponseFormater;
    public function __construct()
    {
        $this->middleware('auth:api', ['except' => ['login','register']]);
    }
    public function login()
    {
        $credentials = request(['email', 'password']);

        if (! $token = auth('api')->attempt($credentials)) {
            return response()->json(['error' => 'Unauthorized'], 401);
        }

        $user=User::where('email',request('email'))->first();
        $role=$user->role;


        return $this->respondWithToken($token,$role);
    }
    public function register(Request $request)
    {

        $validated_data=Validator::make($request->all(),[
        'name'=>"required|string|max:191",
        'role'=>"required|string|max:191",
        'email'=>'required|email|max:191|unique:users,email',
        'password'=>'required|string|max:191|min:5',
        'device_token'=>'required'
    ],[
        'name.required'=>'Name Is Required',
        'name.max'=>'Name Must Be Less Than Or Equal 191',
        'role.required'=>'Name Is Required',
        'role.max'=>'Name Must Be Less Than Or Equal 191',
        'email.max'=>'Email Must Be Less Than Or Equal 191',
        'email.required'=>'Email Is Required',
        'email.unique'=>'Email Must Be Unique This Email Already Exist',
        'password.max'=>'Password Must Be Less Than Or Equal 191',
        'password.min'=>'Password Must Be Greater Than Or Equal 5',
        'password.required'=>'Password Is Required',
        'device_token.required'=>'device Token Is Required',
    ]);
        if ($validated_data->fails()){
            return Response()->json(self::Format_Validation_Error($validated_data->errors()));
        }
        $user=User::create([
            'name'=>$request->name,
            'role'=>$request->role,
            'email'=>$request->email,
            'device_token'=>$request->device_token,
            'password'=>bcrypt($request->password)
        ]);
        if ($user){
            $credintials=['password'=>$request->password,'email'=>$request->email];
            if ($token = auth('api')->attempt($credintials)) {
               // $user->api_token=$token;
                //$user->save();
                return $this->respondWithToken($token);
            }
            return response()->json(['error' => 'Fail To Register Account'], 207);
        }

        return response()->json(['error' => 'Fail To Register Account'], 207);






    }

    /**
     * Get the authenticated User.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function me()
    {
        return response()->json(auth('api')->user());
    }

    /**
     * Log the user out (Invalidate the token).
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout()
    {
        auth('api')->logout();

        return response()->json(['message' => 'Successfully logged out']);
    }

    /**
     * Refresh a token.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function refresh()
    {
        return $this->respondWithToken(auth('api')->refresh());
    }

    /**
     * Get the token array structure.
     *
     * @param  string $token
     *
     * @return \Illuminate\Http\JsonResponse
     */
    protected function respondWithToken($token,$role=null)
    {
        if ($role){
            return response()->json([
                'access_token' =>$token,
                'role' => $role,
                'expires_in' => auth('api')->factory()->getTTL() * 600
            ]);
        }
        return response()->json([
            'access_token' =>$token,
//            'token_type' => 'bearer',
            'expires_in' => auth('api')->factory()->getTTL() * 600
        ]);
    }
}
