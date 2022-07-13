package com.example.chatapp

import android.app.Notification
import android.app.NotificationChannel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.core.app.NotificationCompat
import androidx.core.view.MenuItemCompat.setOnActionExpandListener
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.RemoteMessage
import java.net.Inet4Address

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var searchList: ArrayList<User>
    private lateinit var chatList: ArrayList<String>
    private lateinit var timeList: ArrayList<String>
    private lateinit var actionBar: ActionBar
    private lateinit var lastMessage: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        searchList = ArrayList()
        chatList = ArrayList()
        timeList = ArrayList()
        adapter = UserAdapter(this, userList, chatList, timeList)
        lastMessage = ArrayList()

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter
        actionBar = supportActionBar!!
        actionBar.title = "Chat App"

        val me = FirebaseAuth.getInstance().currentUser!!.uid
        mDbRef.child("chats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lastMessage.clear()
                for (postSnapshot in snapshot.children) {
                    var chatUser = postSnapshot.key?.chunked(postSnapshot.key!!.length / 2)
                    //if condition
                    if (chatUser?.get(0) == me) {
                        var i=0
                        for(mess in postSnapshot.child("messages").children){
                            i+=1
                            if(i==postSnapshot.child("messages").children.count()){
                                //last message is reached
                                lastMessage.add(mess.value.toString())
                            }
                        }

                    }

                }
                lastMessage.sortByDescending {
                    it.replace("{", "").replace("}","").split(", ")[5]
                }
                //print(lastMessage[1].replace("{", "").replace("}","").split(", ")[4])
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        mDbRef.child("User").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                chatList.clear()
                timeList.clear()
                for (user in lastMessage) {
                    val sizeR = user.replace("{", "").replace("}","")
                        .split(", ")[1].length
                    val sizeS = user.replace("{", "").replace("}","")
                        .split(", ")[0].length

                    var chattingWith=""
                    if(user.replace("{", "").replace("}","")
                            .split(", ")[0].substring(9..sizeS-1)==me){
                                //im the sender
                        chattingWith = user.replace("{", "").replace("}","")
                                        .split(", ")[1].substring(11..sizeR-1)

                    }else if(user.replace("{", "").replace("}","")
                            .split(", ")[1].substring(11..sizeR-1)==me){

                        //im the receiver
                        chattingWith=user.replace("{", "").replace("}","")
                            .split(", ")[0].substring(9..sizeS-1)

                    }


                    val size1 = user.replace("{", "").replace("}","")
                        .split(", ")[4].length

                    var mess=""
                    if(size1>43){
                        mess = user.replace("{", "").replace("}","")
                            .split(", ")[4].substring(8..39)+"..."
                    }else{
                        mess = user.replace("{", "").replace("}","")
                            .split(", ")[4].substring(8..size1-1)
                    }

                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)

                        //logic for extracting the receiver id in the array of strings containing the last messages


                        if (chattingWith == currentUser?.uid) {
                            if (mAuth.currentUser?.uid == chattingWith){
                                currentUser?.name = "Note to Self"
                            }
                            userList.add(currentUser!!)
                            chatList.add(mess)


                            val size2 = user.replace("{", "").replace("}","")
                                .split(", ")[3].length
                            timeList.add(user.replace("{", "").replace("}","")
                                .split(", ")[3].substring(5..(size2-1)))
                        }

                    }
                    adapter.notifyDataSetChanged()
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        //dp is tapped



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val search = menu?.findItem(R.id.search_button)
        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search Chat here"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val user_searched = p0?.trim()
                var flag = 0
                searchView.clearFocus()
                searchView.setQuery("", false)
                search.collapseActionView()
                mDbRef.child("User").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (postSnapshot in snapshot.children) {
                            val currentUser = postSnapshot.getValue(User::class.java)
                            if (user_searched == currentUser?.email) {
                                flag = 1;
                                searchList.add(currentUser!!)
                                //just jump to the chat of the searched user

                                val intent = Intent(this@MainActivity, ChatActivity::class.java)
                                if (mAuth.currentUser?.uid == currentUser?.uid) {
                                    intent.putExtra("name", "Note to Self")
                                } else {
                                    intent.putExtra("name", currentUser?.name)
                                }
                                intent.putExtra("uid", currentUser?.uid)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                                finish()
                            }
                        }
                        if (flag == 0) {
                            Toast.makeText(
                                this@MainActivity,
                                "User doesn't exist.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            //write logic for logging out
            mAuth.signOut()
            val intent = Intent(this@MainActivity, logIn::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}

