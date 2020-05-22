<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/


Route::group(['prefix'=>'v1'], function () {

    Route::group(['prefix'=>'post','middleware'=>'api'],function(){
            Route::get('/', 'PostController@index');
            Route::get('/{id}', 'PostController@show');
            Route::post('/', 'PostController@store');
            Route::put('/{id}', 'PostController@update');
            Route::delete('/{id}', 'PostController@destroy');

    });

  /*  Route::group(['prefix'=>'auth'],function() {
        Route::post('login', 'AuthController@login');
        Route::post('register', 'AuthController@register');
        Route::group(['middleware'=>'api'],function(){
            //Route::post('login', 'AuthController@login');
           // Route::post('register', 'AuthController@register');
            Route::post('logout', 'AuthController@logout');
            Route::post('refresh', 'AuthController@refresh');
            Route::post('me', 'AuthController@me');
        });
    });*/
    Route::group([

        'middleware' => 'api',
        'prefix' => 'auth'

    ], function ($router) {

        Route::post('login', 'AuthController@login');
        Route::post('register', 'AuthController@register');
        Route::post('logout', 'AuthController@logout');
        Route::post('refresh', 'AuthController@refresh');
        Route::get('me', 'AuthController@me');

    });

    Route::group([

        'middleware' => 'api',
        'prefix' => 'room'

    ], function ($router) {

        Route::get('/report', 'RoomController@report');

        Route::get('/adminrooms', 'RoomController@getRooms');
        Route::get('/lectures/{id}', 'RoomController@getLectures');
        Route::post('/store', 'RoomController@store');
        Route::put('/update/{id}', 'RoomController@update');
        Route::delete('/delete/{id}', 'RoomController@destroy');

        Route::post('/join/{code}', 'RoomController@join');
        Route::post('/exit/{id}', 'RoomController@exit');
        Route::get('/my_rooms/', 'RoomController@student_rooms');


        Route::get('/get_student/{id}', 'RoomController@students_in_room');




    });
    Route::group([

        'middleware' => 'api',
        'prefix' => 'lecture'

    ], function ($router) {

        Route::post('/store', 'LectureController@store');
        Route::delete('/delete/{id}', 'LectureController@destroy');

    });




});
