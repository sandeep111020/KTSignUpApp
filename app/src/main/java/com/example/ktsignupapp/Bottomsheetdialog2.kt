package com.example.ktsignupapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Bottomsheetdialog2 : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private lateinit var Reg: TextView
    private lateinit var errortext: TextView
    lateinit var Empid: EditText
    lateinit var password: EditText
    lateinit var CheckPassword: EditText
    var Sempid: String? = null
    var Spassword: String? = null
    var Scheckpassword: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.regsiterpopuplayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Reg = view.findViewById(R.id.loginbutton)
        Empid = view.findViewById(R.id.edttxt_empid)
        errortext = view.findViewById(R.id.errortext)
        password = view.findViewById(R.id.edttxt_password)
        CheckPassword = view.findViewById(R.id.edttxt_password_check)
        Reg.setOnClickListener(View.OnClickListener {
            Sempid = Empid.getText().toString()
            Spassword = password.getText().toString()
            Scheckpassword = CheckPassword.getText().toString()
            if (TextUtils.isEmpty(Sempid)) {
                errortext.setText("Please enter Employee id")
            } else if (TextUtils.isEmpty(Spassword)) {
                errortext.setText("Please enter password")
            } else if (TextUtils.isEmpty(Scheckpassword)) {
                errortext.setText("Please enter check password")
            } else if (!Sempid!!.contains(".com")) {
                errortext.setText("Please Enter Vaild Email")
            } else if (Scheckpassword == Spassword) {


                //                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                //                    DatabaseReference reference = rootNode.getReference("Accounts");
                //
                //
                //                    UserAccount addnewUser = new UserAccount(Sempid,Spassword);
                //                    reference.child(Sempid).setValue(addnewUser);
                val i = Intent(activity, NewActivity::class.java)
                i.putExtra("empid", Sempid)
                i.putExtra("password", Spassword)
                startActivity(i)
            } else {
                errortext.setText("Please enter same passwords in two fields")
            }
        })
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
        fun newInstance(): Bottomsheetdialog2 {
            return Bottomsheetdialog2()
        }
    }
}