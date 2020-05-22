<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    $r=fcm()
        ->to([

            'cjwz_EBsHhI:APA91bH4EBJbW3hAChQnmEpmFaX5QyXk-fpzvmJax0LS6M_7FPe0g9CiB-8vg51-c1iira_Kp5oEH92wfragIhUGOW0z0w2p1kyUjwSJoGiWDQCvnXRO0cmtIlp1g4QSlXWF93RqJnHl'
        ])
        ->priority('high')
        ->timeToLive(0)
        ->data([
            'title' => 'From Class One',
            'body' => 'New Lecture Is Ready Now',
        ])
        ->send();

    dd($r);
    //return view('welcome');
});
