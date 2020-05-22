<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use App\Room;
class Lecture extends Model
{
   protected $table="lectures";
   protected $fillable=[
       'name','path','room_id'
   ];

   public function room(){
        return $this->$this->belongsTo(Room::class);
    }
}
