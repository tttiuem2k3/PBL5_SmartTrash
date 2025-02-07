package com.example.trashmobie.Model

object  Model {
    var token_login: String = ""
    var user_id: String = ""
    var exp: String = ""
    var user_name: String = ""
    var trash: Int = 0
    var trashList = mutableListOf<Int>()
    var trashnamelist =mutableListOf<String>()
//    var doman :String ="http://192.168.1.168:8000"

//    var doman :String ="http://192.168.195.179:8000"
//    var doman :String ="http://172.21.0.117:8000"

    var doman :String ="https://lab-moving-grizzly.ngrok-free.app"
}