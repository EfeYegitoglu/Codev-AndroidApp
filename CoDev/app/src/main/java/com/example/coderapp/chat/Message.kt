package com.example.coderapp.chat

import java.io.Serializable

data class Message(
    var userId:Int,
    var otherUserId:Int,
    var message:String
):Serializable {
}