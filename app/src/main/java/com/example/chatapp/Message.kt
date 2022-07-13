package com.example.chatapp

import com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver

class Message {
    var message: String?=null
    var senderId: String?=null
    var time: String?=null
    var timestamp: Long?=null
    var receiverId: String?=null
    var seenStatus: Boolean?=null
    constructor(){}

    constructor(message: String?, senderId: String?, receiverId: String?,time:String?, timestamp:Long?, seenStatus: Boolean?){
        this.message = message
        this.senderId = senderId
        this.time = time
        this.timestamp=timestamp
        this.receiverId = receiverId
        this.seenStatus = seenStatus
    }

}