<?php

namespace App\Http\Resources;


use Illuminate\Http\Resources\Json\JsonResource;

class LectureResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            'id'=>$this->id,
            'name'=>$this->name,
            'path'=>$this->path,
            'room_id'=>$this->room_id,
            'date'=>(substr($this->created_at,0,10))
        ];
    }
}
