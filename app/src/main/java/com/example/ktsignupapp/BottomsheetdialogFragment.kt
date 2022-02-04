package com.example.ktsignupapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*

class BottomsheetdialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private lateinit var Login: TextView
    private lateinit var error: TextView
    lateinit var empid: EditText
    lateinit var password: EditText
    var Sempid: String? = null
    var Spassword: String? = null
    lateinit var check: CheckBox
    var newempid: String? = null
    var newpassword: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loginpopuplayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Login = view.findViewById(R.id.loginbutton)
        empid = view.findViewById(R.id.edttxt_empid)
        error = view.findViewById(R.id.errorlogintext)
        password = view.findViewById(R.id.edttxt_password)
        check = view.findViewById(R.id.checkaccount)
        newempid = requireActivity().intent.getStringExtra("empid")
        newpassword = requireActivity().intent.getStringExtra("password")
        if (!TextUtils.isEmpty(newempid) && !TextUtils.isEmpty(newpassword)) {
            empid.setText(newempid)
            password.setText(newpassword)
        }
        val preferences = requireActivity().getSharedPreferences("ckeckbox", Context.MODE_PRIVATE)
        val checkbox = preferences.getString("remember", "")
        val Aempid = preferences.getString("name", "")
        if (checkbox == "true") {
            val i = Intent(activity, HomeActivity::class.java)
            i.putExtra("name", " ")
            startActivity(i)
        } else if (checkbox == "false") {
        }
        check.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                val preferences = requireActivity().getSharedPreferences("ckeckbox", Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("name", empid.getText().toString())
                editor.putString("remember", "true")
                editor.apply()
            } else if (!buttonView.isChecked) {
                val preferences = requireActivity().getSharedPreferences("ckeckbox", Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("remember", "false")
                editor.apply()
            }
        })
        Login.setOnClickListener(View.OnClickListener {
            Sempid = empid.getText().toString()
            Spassword = password.getText().toString()
            val checkuser: Query = FirebaseDatabase.getInstance().getReference("Users").child(
                Sempid!!.replace(".", "_")
            )
            checkuser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        empid.setError(null)
                        empid.setEnabled(false)
                        val systempassword = snapshot.child("password").getValue(
                            String::class.java
                        )
                        val userempid = snapshot.child("userempid").getValue(
                            String::class.java
                        )
                        val Name = snapshot.child("userName").getValue(
                            String::class.java
                        )
                        val img = snapshot.child("imageURL").getValue(
                            String::class.java
                        )
                        if (systempassword != null && systempassword == Spassword) {
                            password.setError(null)
                            password.setEnabled(false)
                            val intent = Intent(activity, HomeActivity::class.java)
                            intent.putExtra("img", img)
                            intent.putExtra("name", Name)
                            startActivity(intent)
                        } else {
                            error.setText("your password is wrong")
                        }
                    } else {
                        error.setText("There is no Account with this id")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): BottomsheetdialogFragment {
            return BottomsheetdialogFragment()
        }
    }
}