package com.example.ktsignupapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity() {
    var Name: String? = null
    var img: String? = null
    lateinit var imgvi: CircleImageView
    lateinit var tv: TextView
    lateinit var logout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        imgvi = findViewById(R.id.imgvi)
        logout = findViewById(R.id.logout)
        tv = findViewById(R.id.text)
        Name = intent.getStringExtra("name")
        img = intent.getStringExtra("img")
        tv.setText("Hi!! $Name\n Welocome to Celebrity Schools")
        Picasso.get().load(img).placeholder(R.drawable.dasprofile).into(imgvi)
        logout.setOnClickListener(View.OnClickListener {
            val preferences = getSharedPreferences("ckeckbox", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("remember", "false")
            editor.apply()
            val i = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(i)
        })
    }
}