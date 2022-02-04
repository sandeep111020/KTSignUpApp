package com.example.ktsignupapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity(), BottomsheetdialogFragment.ItemClickListener {

    lateinit var admin: Button
    lateinit var register: Button
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        admin = findViewById(R.id.Admin)
        val addPhotoBottomDialogFragment: BottomsheetdialogFragment = BottomsheetdialogFragment.newInstance()
        addPhotoBottomDialogFragment.show(supportFragmentManager,
                BottomsheetdialogFragment.TAG)
        //        getSupportActionBar().hide();
        register = findViewById(R.id.register)

        register.setOnClickListener(View.OnClickListener {
            val addPhotoBottomDialogFragment: Bottomsheetdialog2 = Bottomsheetdialog2.newInstance()
            addPhotoBottomDialogFragment.show(supportFragmentManager,
                    Bottomsheetdialog2.TAG)
        })
        admin.setOnClickListener(View.OnClickListener {
            val addPhotoBottomDialogFragment: BottomsheetdialogFragment = BottomsheetdialogFragment.newInstance()
            addPhotoBottomDialogFragment.show(supportFragmentManager,
                    BottomsheetdialogFragment.TAG)
        })
    }


    companion object {
        private const val TAG = "hi"
        private const val RC_SIGN_IN = 100
    }

    override fun onItemClick(item: String?) {
        TODO("Not yet implemented")
        textView!!.text = "Selected action item is $item"

    }
}