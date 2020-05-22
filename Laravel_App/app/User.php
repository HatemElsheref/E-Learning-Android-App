<?php

namespace App;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Tymon\JWTAuth\Contracts\JWTSubject;
use Illuminate\Support\Facades\DB;
class User extends Authenticatable  implements JWTSubject
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password','role','device_token'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function posts(){
        return $this->hasMany(App\Post::class);
    }



    /**
     * Get the identifier that will be stored in the subject claim of the JWT.
     *
     * @return mixed
     */
    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    /**
     * Return a key value array, containing any custom claims to be added to the JWT.
     *
     * @return array
     */
    public function getJWTCustomClaims()
    {
        return [];
    }
    //for student
//    public function room(){
//        return $this->belongsTo(App\Room::class);
//    }
//    //for instructor
//    public function rooms(){
//        return $this->hasMany(App\Room::class);
//    }
    public function getAdminRooms(){
        return DB::table("rooms")
            ->select("*")
            ->where("user_id",auth("api")->user()->id)->get();
    }
    public function getStudentRooms(){
        $rooms_ids=DB::table("room_user")
            ->select("*")
            ->where("user_id",auth("api")->user()->id)->get();

        $roomsArray=$rooms_ids->pluck("room_id")->toArray();
        $rooms=DB::table("room")
            ->select("*")
            ->whereIn("id",$roomsArray);
        return $rooms;

    }
    public function rooms(){
        return $this->belongsToMany(Room::class);
    }

}
