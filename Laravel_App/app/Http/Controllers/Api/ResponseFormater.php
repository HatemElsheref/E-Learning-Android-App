<?php


namespace App\Http\Controllers\Api;


trait ResponseFormater
{

    public static function SUCCESS(){
        return [200=>'Ok',201=>'Created',202=>'Accepted'];
    }
    public static function REDIRECT_ERROR(){
        return [301=>'Moved Permanently',302=>'Found',303=>'See Other',304=>'Not Modified',307=>'Temporary Redirect'];
    }
    public static function CLIENT_ERROR(){
        return [400=>'Bad Request',401=>'Un Authorized',403=>'Access Forbidden',404=>'Not Found',405=>'Method Not Allowed',406=>'Not Acceptable',415=>'Unsupported Media Type'];
    }
    public static function SERVER_ERROR(){
        return [500=>'Internal Server Error',501=>'Not Implemented'];
    }


    public static function CHECK_CODE($code,$data){

        if (in_array($code,array_keys(self::SUCCESS()))){
            return self::SUCCESS_Formater(self::SUCCESS(),$code,$data);
        }
        else if (in_array($code,array_keys(self::CLIENT_ERROR()))){
                return self::ERROR_Formater(self::CLIENT_ERROR(),$code,$data);
        }
        else if (in_array($code,array_keys(self::SERVER_ERROR()))){
            return self::ERROR_Formater(self::SERVER_ERROR(),$code,$data);
        }
        else{
            return self::ERROR_Formater(self::REDIRECT_ERROR(),$code,$data);
        }

    }
    public static function SUCCESS_Formater($Success_Array,$code,$data){
        return [
            'success'=>true,
            'status'=>$code,
            'message'=>$Success_Array[$code],
            'data'=>$data
        ];
    }
    public static function ERROR_Formater($Errors_Array,$code,$data){
        return [
            'success'=>false,
            'status'=>$code,
            'message'=>$Errors_Array[$code],
            'data'=>$data
        ];
    }


      public static function Formater($_code=200,$_data=null){
         return self::CHECK_CODE($_code,$_data);
      }


    public static function Format_Collection($data){
        return [
            'success'=>true,
            'status'=>200,
            'message'=>'OK',
            'data'=>$data
        ];
    } public static function Format_Validation_Error($data){
    return [
        'success'=>false,
        'status'=>422,
        'message'=>'Validation Error',
        'data'=>$data
    ];
}

}
