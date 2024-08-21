package com.example.designuilogin

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.designuilogin.databinding.ActivityMain2Binding
import com.example.designuilogin.databinding.ActivityUpdateChiBinding
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding: ActivityUpdateChiBinding
class UpdateChi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateChiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnUpdateChi.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("idChi").toString(),
                intent.getStringExtra("khoanChi").toString()
            )
        }
        binding.btnDeleteChi.setOnClickListener {
            delete(intent.getStringExtra("idChi").toString())
        }
        setValueToView()
    }


    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(idChi: String, khoanThu: String) {
        val mDialog=AlertDialog.Builder(this)
        val mDialogView=layoutInflater.inflate(R.layout.layout_dialog_updatechi,null)
        mDialog.setView(mDialogView)
        val etEmpDateChi= mDialogView.findViewById<EditText>(R.id.etEmpDateChi)
        val etEmpTimeChi = mDialogView.findViewById<EditText>(R.id.etEmpTimeChi)
        val etEmpKhoanChi = mDialogView.findViewById<EditText>(R.id.etEmpKhoanChi)
        val etEmpLuongChi = mDialogView.findViewById<EditText>(R.id.etEmpLuongChi)
        val btnUpdateDataChi = mDialogView.findViewById<Button>(R.id.btnUpdateDataChi)

        etEmpDateChi.setText(intent.getStringExtra("dateChi").toString())
        etEmpTimeChi.setText(intent.getStringExtra("timeChi").toString())
        etEmpKhoanChi.setText(intent.getStringExtra("khoanChi").toString())
        etEmpLuongChi.setText(intent.getStringExtra("luongChi").toString())

        mDialog.setTitle("Cập nhật thông tin của $khoanThu")
        val alertDialog=mDialog.create()
        alertDialog.show()
        btnUpdateDataChi.setOnClickListener {
            updateDataChi(idChi,etEmpDateChi.text.toString(),etEmpTimeChi.text.toString(),etEmpKhoanChi.text.toString(),etEmpLuongChi.text.toString())
            binding.tvDateChi.text=etEmpDateChi.text.toString()
            binding.tvTimeChi.text=etEmpTimeChi.text.toString()
            binding.tvKhoanChi.text=etEmpKhoanChi.text.toString()
            binding.tvLuongChi.text=etEmpLuongChi.text.toString()
            alertDialog.dismiss()
        }
    }

    private fun updateDataChi(idChi: String, dateChi: String, timeChi: String, khoanChi: String, luongChi: String) {
        val dbRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        val currentUser= auth.currentUser!!
        val userId=(currentUser.email?:"").substringBefore("@")
        val employeeDataChi=dbRef.child(userId).child("Nam").child("Chi")
        val empInfoChi=employeeChi(idChi,dateChi,timeChi,khoanChi,luongChi)
        employeeDataChi.child(idChi).setValue(empInfoChi).addOnSuccessListener {
            Toast.makeText(this, "Đã chỉnh sửa thông tin thành công", Toast.LENGTH_SHORT).show()
        }

    }

    private fun delete(idChi : String) {
        val dbRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        val currentUser= auth.currentUser!!
        val userId=(currentUser.email?:"").substringBefore("@")
        val employeeDataChi=dbRef.child(userId).child("Nam").child("Chi")
         employeeDataChi.child(idChi).removeValue().addOnSuccessListener {
             Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show()
             finish()
         }

    }

    private fun setValueToView() {
        binding.tvDateChi.text =intent.getStringExtra("dateChi")
        binding.tvTimeChi.text =intent.getStringExtra("timeChi")
        binding.tvKhoanChi.text=intent.getStringExtra("khoanChi")
        binding.tvLuongChi.text=intent.getStringExtra("luongChi")
    }
}