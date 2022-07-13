package com.example.chatapp

import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.Gravity
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity

class popUpDpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val image = arrayOf(R.drawable.ic_user_1, R.drawable.ic_user_2, R.drawable.ic_user_3, R.drawable.ic_user_4, R.drawable.ic_user_5,R.drawable.ic_user_6,R.drawable.ic_user_7, R.drawable.ic_user_8, R.drawable.ic_user_9, R.drawable.ic_user_10, R.drawable.ic_user_12)
        supportActionBar?.hide()
        //val num = intent.getStringExtra("num")
        val bundle: Bundle? = intent.extras
        val num: String? = bundle?.getString("num")

        print("nummmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm: "+num)

        setContentView(R.layout.dp_window)

        var img = findViewById<ImageView>(R.id.dp_enlarge)
        img.setImageResource(image[num!!.toInt()])
        val dm = DisplayMetrics()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        windowManager.defaultDisplay.getMetrics(dm)

        var w = dm.widthPixels
        var h = dm.heightPixels
        window.setLayout((w*0.7).toInt(), (h*0.4).toInt())
        var params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20

    }
}

