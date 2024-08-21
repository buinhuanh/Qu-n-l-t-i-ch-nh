package com.example.designuilogin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.designuilogin.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.database

lateinit var auth: FirebaseAuth
lateinit var dialog:AlertDialog
private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isChecked = sharedPreferences.getBoolean("isChecked", false)

//        if (isChecked) {
//            // Nếu checkbox được chọn, chuyển sang màn hình app luôn mà không cần đăng nhập lại
//            val currentUser = auth.currentUser
//            if (currentUser != null) {
//                val i = Intent(this, MainActivity2::class.java)
//                startActivity(i)
//                finish() // Đóng MainActivity để ngăn người dùng quay lại màn hình đăng nhập
//
//            }
//
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("isChecked", binding.chkLuuDangNhap.isChecked)
//            editor.apply()
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.txtFogotPass.setOnClickListener {
            dialogforgotpass()
        }

        binding.btnDangKi.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }
        auth=Firebase.auth
        binding.btnDangNhap.setOnClickListener {
            val email=binding.edtUser.getText().toString().trim()
            val pass=binding.edtPass.getText().toString().trim()
            Login(email,pass)
        }

    }

    private fun dialogforgotpass() {
        val build =AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.forgotpasswordialog,null)
        build.setView(view)
        dialog=build.create()
        dialog=build.show()
        val btnHuy=view.findViewById<Button>(R.id.btnHuy)
        val btnGui=view.findViewById<Button>(R.id.btnGui)
        val sendEmail=view.findViewById<EditText>(R.id.sendEmail)
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        btnGui.setOnClickListener {
            val email=sendEmail.text.toString().trim()
           sendPassWordEmail(email)

        }
    }

    private fun sendPassWordEmail(email: String) {
         auth.sendPasswordResetEmail(email)
             .addOnCompleteListener { task->
                 if (task.isSuccessful){
                     Toast.makeText(this,"Vui lòng kiểm tra Email",Toast.LENGTH_SHORT).show()
                     dialog.dismiss()
                 } else {
                     Toast.makeText(this,"Gửi mail đặt lại mật khẩu thất bại",Toast.LENGTH_SHORT).show()
                 }

             }
    }

    private fun Login(email: String, pass: String) {
        if (email.isEmpty()||pass.isEmpty()){
            Toast.makeText(this,"Email hoặc mật khẩu không được để trống ",Toast.LENGTH_SHORT).show()
        return
        }
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener (this){ task->
                if (task.isSuccessful){
                   Toast.makeText(this,"đăng nhập thành công",Toast.LENGTH_SHORT).show()
                    val currentUser= auth.currentUser
                    if (currentUser!=null){
                        val i=Intent(this,MainActivity2::class.java)
                        startActivity(i)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Thông tin không hợp lệ ",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Thông tin không hợp lệ",Toast.LENGTH_SHORT).show()
                }
            }


    }

}