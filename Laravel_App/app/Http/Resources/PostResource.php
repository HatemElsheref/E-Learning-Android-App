<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class PostResource extends JsonResource
{
//    private $data=null;
//
//    public function __construct($resource)
//    {
//        $this->data=$resource;
//    }

    public function toArray($request)
    {
        return [
            'id'=>$this->id,
            'title'=>$this->title,
            'content'=>$this->body,
        ];
        //return parent::toArray($request);
    }
}
