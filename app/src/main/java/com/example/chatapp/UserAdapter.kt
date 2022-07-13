package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context, val userList: ArrayList<User>, val chatList: ArrayList<String>, val timeList: ArrayList<String>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)

        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val image = arrayOf(R.drawable.ic_user_1, R.drawable.ic_user_2, R.drawable.ic_user_3, R.drawable.ic_user_4, R.drawable.ic_user_5,R.drawable.ic_user_6,R.drawable.ic_user_7, R.drawable.ic_user_8, R.drawable.ic_user_9, R.drawable.ic_user_10, R.drawable.ic_user_12)
        val currentUser = userList[position]
        val currUserMess = chatList[position]
        val currentMessageTime = timeList[position]
        holder.textName.text = currentUser.name
        holder.userDP.setImageResource(image[currentUser.num!!]);
        holder.recentMess.text = currUserMess
        holder.recentMessTime.text = currentMessageTime

        //when dp is tapped
        holder.userDP.setOnClickListener(){
            val intent = Intent(context, popUpDpActivity::class.java);
            intent.putExtra("num", currentUser.num.toString())
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return userList.size;
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txtName)
        val recentMess = itemView.findViewById<TextView>(R.id.recentMessage)
        val recentMessTime = itemView.findViewById<TextView>(R.id.time)
        val userDP = itemView.findViewById<ImageView>(R.id.dp)
    }
}