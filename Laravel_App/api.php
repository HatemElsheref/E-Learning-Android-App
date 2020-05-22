<?php


define('db_host','localhost');
define('USER_NAME','root');
define('USER_password','');
define('db_name','laravel_android');

if ($_SERVER['REQUEST_METHOD']=='GET'){

    $Connection=mysqli_connect(db_host,USER_NAME,USER_password,db_name);
    $sql="select * from posts";
    $result=mysqli_query($Connection,$sql);

    $data=[];
    while($row=mysqli_fetch_assoc($result)){
             array_push($data,$row);
    }
    return json_decode($data);


}
