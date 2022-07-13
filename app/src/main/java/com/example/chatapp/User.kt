package com.example.chatapp

class User {
    var name: String? = null;
    var email: String? = null;
    var uid: String? = null;
    var num: Int? = null;

    constructor(){}

    constructor(name:String?, email:String?, uid:String?, num:Int?){
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.num = num;
    }
}