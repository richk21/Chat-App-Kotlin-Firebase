package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(val context:Context, val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED = 1;
    val ITEM_SENT = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate receive
            val view : View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            return receivedViewHolder(view)
        }else{
            //inflate sent
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return sentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == sentViewHolder::class.java){
            //do something for the sent view holder

            val viewHolder = holder as sentViewHolder
            holder.sentMessage.text = currentMessage.message
            holder.sentTime.text = currentMessage.time
            holder.seen.isVisible = position==messageList.size-1

            //if the receiver saw the message, set seen="seen", else nothing
            if(currentMessage.seenStatus==true){
                holder.seen.text = "Seen"
            }

        }else{
            //do something for received view holder
            val viewHolder = holder as receivedViewHolder
            holder.receivedMessage.text = currentMessage.message
            holder.receivedTime.text = currentMessage.time
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVED
        }

    }

    override fun getItemCount(): Int {
        return messageList.size;
    }

    class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txtSentMessage)
        val sentTime = itemView.findViewById<TextView>(R.id.sentTime)
        val seen = itemView.findViewById<TextView>(R.id.seen)
    }

    class receivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.findViewById<TextView>(R.id.txtReceivedMessage)
        val receivedTime = itemView.findViewById<TextView>(R.id.receivedTime)
    }
}