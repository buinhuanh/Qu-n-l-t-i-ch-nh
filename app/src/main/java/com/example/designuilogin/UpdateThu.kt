package com.example.designuilogin

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.designuilogin.databinding.ActivityMain2Binding
import com.example.designuilogin.databinding.ActivityUpdateChiBinding
import com.example.designuilogin.databinding.ActivityUpdateThuBinding
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding: ActivityUpdateThuBinding
class UpdateThu : AppCompatActivity() {
    lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateThuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setValueToView()
        binding.btnDeleteThu.setOnClickListener {
            deleteData(intent.getStringExtra("idThu").toString())
        }
        binding.btnUpdateThu.setOnClickListener {
            openUpdateDialogThu(
                intent.getStringExtra("idThu").toString(),
                intent.getStringExtra("khoanThu").toString()
            )
        }
    }

    private fun openUpdateDialogThu(idThu: String, khoanThu: String) {
     val build=AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.layout_dialog_updatethu,null)
        build.setView(view)

        val etEmpDateThu= view.findViewById<EditText>(R.id.etEmpDateThu)
        val etEmpTimeThu = view.findViewById<EditText>(R.id.etEmpTimeThu)
        val etEmpKhoanThu = view.findViewById<EditText>(R.id.etEmpKhoanThu)
        val etEmpLuongThu = view.findViewById<EditText>(R.id.etEmpLuongThu)
        val btnUpdateDataThu = view.findViewById<Button>(R.id.btnUpdateDataThu)

        etEmpDateThu.setText(intent.getStringExtra("dateThu").toString())
        etEmpTimeThu.setText(intent.getStringExtra("timeThu").toString())
        etEmpKhoanThu.setText(intent.getStringExtra("khoanThu").toString())
        etEmpLuongThu.setText(intent.getStringExtra("luongThu").toString())

        build.setTitle("Cập nhật thông tin của $khoanThu")
        dialog=build.create()
        dialog.show()
        btnUpdateDataThu.setOnClickListener {
            updateDataThu(idThu,etEmpTimeThu.text.toString(),etEmpDateThu.text.toString(),etEmpKhoanThu.text.toString(),etEmpLuongThu.text.toString())
            val tvDateThu = findViewById<TextView>(R.id.tvDateThu)
            val tvTimeThu = findViewById<TextView>(R.id.tvTimeThu)
            val tvKhoanThu = findViewById<TextView>(R.id.tvKhoanThu)
            val tvLuongThu = findViewById<TextView>(R.id.tvLuongThu)
            tvDateThu.text=etEmpDateThu.text.toString()
            tvTimeThu.text=etEmpTimeThu.text.toString()
            tvKhoanThu.text=etEmpKhoanThu.text.toString()
            tvLuongThu.text=etEmpLuongThu.text.toString()
            dialog.dismiss()
        }
    }

    private fun updateDataThu(idThu: String,dateThu : String, timeThu: String, khoanThu: String, luongThu: String) {
        val dbRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        val currentUser= auth.currentUser!!
        val userId=(currentUser.email ?: "").substringBefore("@")
        val employeeDataThu=dbRef.child(userId).child("Nam").child("Thu")
        val empInfoThu=employeeThu(idThu, dateThu, timeThu, khoanThu, luongThu)
        employeeDataThu.child(idThu).setValue(empInfoThu).addOnSuccessListener {
            Toast.makeText(this, "Đã chỉnh sửa thông tin", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteData(idThu: String) {
      val dbRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/")
          .getReference("users")
        val currentUser= auth.currentUser!!
        val usedId=(currentUser.email?:"").substringBefore("@")
        val employeeDataThu=dbRef.child(usedId).child("Nam").child("Thu")
        employeeDataThu.child(idThu).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setValueToView() {
        binding.tvDateThu.text=intent.getStringExtra("dateThu")
        binding.tvTimeThu.text=intent.getStringExtra("timeThu")
        binding.tvKhoanThu.text=intent.getStringExtra("khoanThu")
        binding.tvLuongThu.text=intent.getStringExtra("luongThu")
    }
}