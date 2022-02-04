package com.example.ktsignupapp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class NewActivity : AppCompatActivity(), OnDateSetListener, AdapterView.OnItemSelectedListener {
    var dob: EditText? = null
    var name: EditText? = null
    var number: EditText? = null
    lateinit var empid: TextView
    var Sname: String? = null
    var Snumber: String? = null
    var Semail: String? = null
    var Sempid: String? = null
    var Sdob: String? = null
    var loadingbar: ProgressDialog? = null
    var uploadimg: CircleImageView? = null
    private var ImageUri: Uri? = null
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null
    var employeeid: String? = null
    var Semployeeid: String? = null
    var Spasswword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        loadingbar = ProgressDialog(this)
        employeeid = intent.getStringExtra("name")
        name = findViewById(R.id.user_name)
        number = findViewById(R.id.user_phone_number)
        empid = findViewById(R.id.empid)
        Semployeeid = intent.getStringExtra("empid")
        Spasswword = intent.getStringExtra("password")
        storageReference = FirebaseStorage.getInstance().getReference("ImagesProfile")
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        empid.setText(Semployeeid)
        val submit = findViewById<Button>(R.id.btn_submit_user_infor)
        submit.setOnClickListener { ValidateProductData() }
        uploadimg = findViewById<View>(R.id.profile_image) as CircleImageView
        uploadimg!!.setOnClickListener { OpenGallery() }
    }

    private fun ValidateProductData() {
        Sname = name!!.text.toString()
        Snumber = number!!.text.toString()
        Sempid = empid!!.text.toString()
        if (ImageUri == null) {
            Toast.makeText(this, "Product Image is Required", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Sname)) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Snumber)) {
            Toast.makeText(this, "Number is required", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Sempid)) {
            Toast.makeText(this, "Employee id is required", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Semail)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
        } else {
            StoreProductInformation()
        }
    }

    private fun StoreProductInformation() {
        loadingbar!!.setMessage("Please Wait")
        loadingbar!!.setTitle("Adding New Product")
        loadingbar!!.setCancelable(false)
        loadingbar!!.show()
        UploadImage()
    }

    fun UploadImage() {
        if (ImageUri != null) {
            loadingbar!!.setTitle("Account is Creating...")
            loadingbar!!.show()
            val storageReference2 = storageReference!!.child(
                System.currentTimeMillis().toString() + "." + GetFileExtension(ImageUri)
            )
            storageReference2.putFile(ImageUri!!)
                .addOnSuccessListener {
                    val TempName = name!!.text.toString().trim { it <= ' ' }
                    val Tempemail = empid!!.text.toString().trim { it <= ' ' }
                    val Tempempid = empid!!.text.toString().trim { it <= ' ' }
                    val Tempnumber = number!!.text.toString().trim { it <= ' ' }
                    loadingbar!!.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Profile Created Successfully ",
                        Toast.LENGTH_LONG
                    ).show()
                    storageReference2.downloadUrl.addOnSuccessListener { uri ->
                        val url = uri.toString()
                        val userProfileInfo =
                            profileinfo(TempName, Tempnumber, Tempemail, Tempempid, Spasswword, url)
                        val ImageUploadId = databaseReference!!.push().key
                        databaseReference!!.child(Tempemail.replace(".", "_"))
                            .setValue(userProfileInfo)
                        val intent = Intent(this@NewActivity, MainActivity::class.java)
                        intent.putExtra("empid", Semployeeid)
                        intent.putExtra("password", Spasswword)
                        startActivity(intent)
                    }


                    //                            @SuppressWarnings("VisibleForTests")
                    //
                    //                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName,TempImageDescription,TempImagePrice, taskSnapshot.getUploadSessionUri().toString());
                    //                            String ImageUploadId = databaseReference.push().getKey();
                    //                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                }
        } else {
            Toast.makeText(
                this@NewActivity,
                "Please Select Image or Add Image Name",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            uploadimg!!.setImageURI(ImageUri)
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this, this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH], Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val monthName = arrayOf(
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"
        )
        val Smonth = monthName[month]
        val date = "$dayOfMonth/$Smonth/$year"
        dob!!.setText(date)
        Sdob = dayOfMonth.toString() + "" + Smonth + "" + year
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    private fun OpenGallery() {
        val galleryintent = Intent()
        galleryintent.action = Intent.ACTION_GET_CONTENT
        galleryintent.type = "image/*"
        startActivityForResult(galleryintent, GalleryPick)
    }

    companion object {
        const val GalleryPick = 1
    }
}