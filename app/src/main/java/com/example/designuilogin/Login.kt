package com.example.designuilogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.designuilogin.databinding.ActivityLoginBinding
import com.example.designuilogin.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

private lateinit var binding: ActivityLoginBinding
class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtToMain.setOnClickListener {
            val i=Intent(this ,MainActivity::class.java)
            startActivity(i)
        }

        binding.btnXacThuc.setOnClickListener {
            val email= binding.edtGmail.text.toString().trim()
            val pass= binding.edtPassWord.text.toString().trim()
            if(email.isEmpty()&&pass.isEmpty()){
                Toast.makeText(this,"Email và mật khẩu không được để trống",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            guiMaXacThuc(email)


        }
        binding.btnSent.setOnClickListener {
            val email= binding.edtGmail.text.toString().trim()
            val pass= binding.edtPassWord.text.toString().trim()
            if (email.isEmpty()||pass.isEmpty()){
                Toast.makeText(this,"Email và mật khẩu không được để trống",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            kiemTraXacThuc(email,pass)
        }
    }

    private fun kiemTraXacThuc(email: String, pass: String) {
    auth.signInWithEmailAndPassword(email,"mật khẩu tạm thời")
        .addOnCompleteListener { task->
            if (task.isSuccessful){
                val user= auth.currentUser
                if (user!=null&&user.isEmailVerified){
                   auth.currentUser?.delete()?.addOnCompleteListener { task->
                        if (task.isSuccessful){
                            dangkitaikhoan(email,pass)
                        }
                    }
                }else{
                    Toast.makeText(this, "Bạn vui lòng hãy xác thực Email", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Đăng kí thất bại", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun guiMaXacThuc(email: String) {
    auth.createUserWithEmailAndPassword(email,"mật khẩu tạm thời")
        .addOnCompleteListener(this) { task->
            if (task.isSuccessful){
                val user= auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { task->
                        if (task.isSuccessful){
                            Toast.makeText(this,"Hãy kiểm tra email",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this,"Gửi xác nhận thất bại ",Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this,"Không tìm thấy email",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dangkitaikhoan(email:String,pass:String) {
            auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this) { task->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Tao tài khoản thành công", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}