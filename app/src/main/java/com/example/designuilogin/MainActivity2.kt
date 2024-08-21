package com.example.designuilogin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.designuilogin.databinding.ActivityMain2Binding
import com.example.designuilogin.adpater.customadapter
import com.example.layoutapp.outdata

lateinit var customAdapter: customadapter

private lateinit var binding: ActivityMain2Binding
class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
//        val txtLogOut = findViewById<TextView>(R.id.txtLogOut)
//        txtLogOut.setOnClickListener {
//            // Xóa trạng thái đăng nhập khỏi SharedPreferences
//            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            editor.remove("isChecked")
//            editor.apply()

            // Chuyển về MainActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish() // Đóng activity hiện tại để ngăn người dùng quay lại màn hình app
//        }

        val list= mutableListOf<outdata>()
        list.add(outdata(R.drawable.nam, title = "Nam", desc ="Hãy chọn nếu bạn là nam" ))
        list.add(outdata(R.drawable.nu, title = "Nữ", desc ="Hãy chọn nếu bạn là nữ" ))
        list.add(outdata(R.drawable.giadinh, title = "Gia Đình", desc ="Hãy chọn nếu bạn có nhu cầu về gia đình " ))
        list.add(outdata(R.drawable.congty, title = "Công ty", desc ="Hãy chọn nếu bạn có nhu cầu về công ty" ))

        customAdapter= customadapter(this,list)
        val LvCheDo=findViewById<ListView>(R.id.LvCheDo)
        LvCheDo.adapter= customAdapter

        LvCheDo.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0->{
                    val i=Intent(this,MainActivity3::class.java)
                    startActivity(i)
                }
                1->{
                    val i=Intent(this,MainActivity3::class.java)
                    startActivity(i)
                }
                2->{
                    val i=Intent(this,MainActivity3::class.java)
                    startActivity(i)
                }
                3->{
                    val i=Intent(this,MainActivity3::class.java)
                    startActivity(i)
                }
            }
        }


    }
}