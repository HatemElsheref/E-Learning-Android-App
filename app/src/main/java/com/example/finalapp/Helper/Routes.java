package com.example.finalapp.Helper;

public class Routes {
    public static String ROOT="http://mufix.org/api_sub_domain/public/api/v1/";

    /* Auth Routes */
    public static String login             =ROOT+"auth/login";
    public static String register          =ROOT+"auth/register";
    public static String logout            =ROOT+"auth/logout";
    public static String me                =ROOT+"auth/me";
    public static String refresh           =ROOT+"auth/refresh";
    /* Posts Routes */
    public static String show_all_posts    =ROOT+"post";
    public static String show_certain_posts=ROOT+"post/";
    public static String delete_post       =ROOT+"post/";
    public static String store_post        =ROOT+"post/";
    public static String update_post       =ROOT+"post/";

    /* Classes Routes */
    //rooms where current auth user=>instructor is admin
    public static String show_all_admin_classes     =ROOT+"room/adminrooms";//+token
    //rooms where current auth user=>student is exist in it
    public static String show_all_student_classes   =ROOT+"room/get_student_rooms";//+token
    public static String show_class_lectures        =ROOT+"room/lectures/";//+class id+token
    public static String delete_class               =ROOT+"room/delete/";//token+class id
    public static String store_class                =ROOT+"room/store";//token
    public static String update_class               =ROOT+"room/update/";//+class id + token

    /* Lecture Routes */
    public static String delete_lecture               =ROOT+"lecture/delete/";//token+class id
    public static String store_lecture                =ROOT+"lecture/store";//token
}
