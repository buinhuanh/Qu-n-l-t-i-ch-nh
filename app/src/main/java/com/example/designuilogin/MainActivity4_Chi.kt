package com.example.designuilogin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.designuilogin.databinding.ActivityMain2Binding
import com.example.designuilogin.databinding.ActivityMain4Binding
import com.example.designuilogin.databinding.ActivityMainActivity4ChiBinding
import com.example.qunltichnh.NetworkChangeReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private lateinit var binding: ActivityMainActivity4ChiBinding
class MainActivity4_Chi : AppCompatActivity() {
    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference
    private var uri: Uri?=null
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    private val captureImage=registerForActivityResult(ActivityResultContracts.TakePicture()){success->
        if (success){
            uri?.let{uploadImage(it)}
        }
    }
    private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            this.uri=it
            uploadImage(it)
        }
    }
    // tải ảnh lên Firebase Storage
    private fun uploadImage(Uri: Uri) {
        val currentUser= auth.currentUser
        currentUser?.let { user ->
            val userID=(user.email?:"").substringBefore("@")
            val imageRef=storageRef.child(userID).child("Nam").child("Chi").child("image/${uri?.lastPathSegment}")
            imageRef.putFile(Uri)
                .addOnSuccessListener {
                    Toast.makeText(this,"tải ảnh lên thành công",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "tải ảnh lên thất bại", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity4ChiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance("gs://design-ui-login-8513d.appspot.com").getReference("images")

        val btnLich = findViewById<ImageButton>(R.id.BtnLichChi)
        val btnDongHo = findViewById<ImageButton>(R.id.BtnDongHoChi)
        val txtTime = findViewById<TextView>(R.id.txtTimeChi)
        val txtDate = findViewById<TextView>(R.id.txtDateChi)


        val today = Calendar.getInstance()
        val starYear = today.get(Calendar.YEAR)
        val starMonth = today.get(Calendar.MONTH)
        val starDay = today.get(Calendar.DAY_OF_MONTH)
        val starHour = today.get(Calendar.HOUR_OF_DAY)
        val starMinute = today.get(Calendar.MINUTE)
        btnDongHo.setOnClickListener {
            TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    txtTime.setText("$hourOfDay giờ $minute phút")
                },
                starHour, starMinute, true
            ).show()
        }

        btnLich.setOnClickListener {
            DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    txtDate.setText("$dayOfMonth/${month + 1}/$year")
                },
                starYear, starMonth, starDay
            ).show()
        }
        binding.btnSaveChi.setOnClickListener {
            if (uri!=null){
                uploadImage(uri!!)
            }
            saveEmployeeData()
        }
        binding.btnAnhChi.setOnClickListener {
            showImagePickerDialog()
        }
    }


    //Lưu dữ liệu
    private fun saveEmployeeData() {
        val currentUser= auth.currentUser
        currentUser?.let { user->
            val userId=(user.email?:"").substringBefore("@")
            val emDate= binding.txtDateChi.text.toString()
            val empTime= binding.txtTimeChi.text.toString()
            val empKhoanChi= binding.edtKhoanChi.text.toString()
            val empLuongChi= binding.edtLuongChi.text.toString()
            val idThu=dbRef.push().key!!
            val employeeDataRef=dbRef.child(userId)
                .child("Nam")
                .child("Chi")
                .child(idThu)
            val employeeChi=employeeChi(idThu,emDate,empTime,empKhoanChi,empLuongChi)
            if (empLuongChi.isEmpty()){
                binding.edtLuongChi.error="Vui lòng nhập chi tiết khoản chi"
                return
            }
            if (empKhoanChi.isEmpty()){
                binding.edtKhoanChi.error="Vui lòng nhập chi tiết khoản chi"
                return
            }
            employeeDataRef.setValue(employeeChi)
                .addOnSuccessListener {
                    Toast.makeText(this,"Thêm dữ liệu thành công",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showImagePickerDialog() {
        val options= arrayOf<CharSequence>("Chụp ảnh","Chọn ảnh từ thư viện")
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Chọn ảnh")
        builder.setItems(options){ _,item->
            when (options[item]) {
                "Chụp ảnh"->{
                    val photoUri=createImageUri()
                    if (photoUri != null) {
                        captureImage.launch(photoUri)
                    }
                    uri=photoUri
                }
                "Chọn ảnh từ thư viện" ->{
                    pickImage.launch("image/*")
                }
            }
        }
        builder.setNeutralButton("Hủy"){ dialog,_->dialog.dismiss()}
        builder.show()
    }

    private fun createImageUri(): Uri? {
        val resolver=contentResolver
        val contentValues=ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,"new_image.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

}