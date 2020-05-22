<?php

namespace App;

use App\Lecture;
use App\User;
use Illuminate\Database\Eloquent\Model;
class Room extends Model
{
    protected $table="rooms";
    protected $fillable=['name','code','user_id'];

    public function lectures(){
        return $this->hasMany(Lecture::class);
    }
    public function students(){
        return $this->belongsToMany(User::class);
    }

}
