package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

    var seenStatus : Boolean = false
    var receiverRoom: String?=null;
    var senderRoom: String?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        println(Calendar.getInstance().time.toString())
        val timestamp: Long = System.currentTimeMillis().toLong()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        //create a unique room for the sender and the receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name;

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter =  MessageAdapter(this, messageList)


        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        //add the message from the database to the recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        val time = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                    messageRecyclerView.scrollToPosition(messageList.size-1)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        messageBox.setOnClickListener(){

        }


        sendButton.setOnClickListener(){
            //add message to the database
            val message = messageBox.text.toString().trim()
            if(message != ""){
                val time1 = Calendar.getInstance().time.toString().split(" ").get(3).slice(0..4)
                val time2 = Calendar.getInstance().time.toString().split(" ").get(0)
                val time = time2+" "+time1

                var messageObject = Message(message,senderUid, receiverUid, time, timestamp, seenStatus)

                //create a node for chats in the realtime database
                if(senderUid!=receiverUid){

                    mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                                .setValue(messageObject)
                        }
                }else{
                    seenStatus = true
                    messageObject = Message(message,senderUid, receiverUid, time, timestamp, seenStatus)
                    mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

                messageBox.setText("")
            }else{
                Toast.makeText(this@ChatActivity, "Empty message!", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //write logic for deleting all the messages
        mDbRef.child("chats").child(senderRoom!!).child("messages").removeValue()
        mDbRef.child("chats").child(receiverRoom!!).child("messages").removeValue()
        val intent = Intent(this@ChatActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this@ChatActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}