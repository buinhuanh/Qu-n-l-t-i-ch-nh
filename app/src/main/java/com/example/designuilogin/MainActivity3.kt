package com.example.designuilogin

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.designuilogin.adpater.CustomAdapterGv
import com.example.designuilogin.adpater.customadapter
import com.example.designuilogin.databinding.ActivityMain2Binding
import com.example.designuilogin.databinding.ActivityMain3Binding
import com.example.layoutrecycleview.outdataGv
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private lateinit var binding: ActivityMain3Binding
class MainActivity3 : AppCompatActivity() {
    lateinit var dialog:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLichSu.setOnClickListener {
            val i=Intent(this,lichSuActivity::class.java)
            startActivity(i)
        }
        binding.btnHoaDon.setOnClickListener {
            val i =Intent(this,showImageActivity::class.java)
            startActivity(i)
        }
        binding.btnDoThi.setOnClickListener {
            val i=Intent(this,doThiActivity::class.java)
            startActivity(i)
        }
        val list= mutableListOf<outdataGv>()
        list.add(outdataGv(R.drawable.phieuluog, ten = "Phiếu Lương"))
        list.add(outdataGv(R.drawable.lamthem, ten = "Làm Thêm "))
        list.add(outdataGv(R.drawable.laixuat, ten = "Lãi Xuất"))
        list.add(outdataGv(R.drawable.dautu, ten = "Đầu Tư"))
        list.add(outdataGv(R.drawable.khac, ten = "Khác"))
        val customThu=CustomAdapterGv(this,list)
        val gvThu=findViewById<GridView>(R.id.gvThuNam)
        gvThu.adapter = customThu
        gvThu.setOnItemClickListener { parent, view, position, id ->
            val i= Intent(this,MainActivity4::class.java)
            startActivity(i)
        }



        val list1= mutableListOf<outdataGv>()
        list1.add(outdataGv(R.drawable.suckhoe, ten = "Sức Khỏe"))
        list1.add(outdataGv(R.drawable.giatri, ten = "Giải Trí "))
        list1.add(outdataGv(R.drawable.giaoduc, ten = "Giao Dục"))
        list1.add(outdataGv(R.drawable.giadinh1, ten = "Gia Đình"))
        list1.add(outdataGv(R.drawable.dichuyen, ten = "Di Chuyển"))
        list1.add(outdataGv(R.drawable.muasam, ten = "Mua Sắm"))
        list1.add(outdataGv(R.drawable.thethao, ten = "Thể Thao"))
        list1.add(outdataGv(R.drawable.anuong, ten = "Ăn Uống"))
        list1.add(outdataGv(R.drawable.khac, ten = "Khác"))
        val customChi= CustomAdapterGv(this,list1)
        val gvChi=findViewById<GridView>(R.id.gvChiNam)
        gvChi.adapter=customChi
        gvChi.setOnItemClickListener { parent, view, position, id ->
            val i= Intent(this,MainActivity4_Chi::class.java)
            startActivity(i)
        }
        binding.btnCheckTK.setOnClickListener {

        val databaseRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
            val currentUser= auth.currentUser!!
            val userId=(currentUser.email?:"").substringBefore("@")
            val employeeDataRefThu=databaseRef.child(userId).child("Nam").child("Thu")
            val employeeDataRefChi=databaseRef.child(userId).child("Nam").child("Chi")


            var totalLuongThu=0
            var totalLuongChi=0

            var listenerCount=0

            employeeDataRefThu.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    totalLuongThu=0
                    for (snapshot in dataSnapshot.children){
                    val employee=snapshot.getValue(employeeThu::class.java)
                        val luongThu=employee?.luongThu?.toIntOrNull() ?:0
                        totalLuongThu+=luongThu
                    }
                    listenerCount++
                    if (listenerCount==2){
                        calculateAndShowDialog(totalLuongThu,totalLuongChi)
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity3, "Lỗi lượng thu", Toast.LENGTH_SHORT).show()
                }
            })
               employeeDataRefChi.addListenerForSingleValueEvent(object :ValueEventListener{
                   override fun onDataChange(dataSnapshot: DataSnapshot) {
                       totalLuongChi=0
                       for (snapshot in dataSnapshot.children){
                           val employee=snapshot.getValue(employeeChi::class.java)
                           val luongChi=employee?.luongChi?.toIntOrNull()?:0
                           totalLuongChi+=luongChi

                       }
                       listenerCount++
                       if (listenerCount==2){
                           calculateAndShowDialog(totalLuongThu,totalLuongChi)
                       }
                   }

                   override fun onCancelled(error: DatabaseError) {
                       Toast.makeText(this@MainActivity3, "Lỗi lượng chi", Toast.LENGTH_SHORT).show()
                   }
               })





        }
    }

    private fun calculateAndShowDialog(totalLuongThu: Int, totalLuongChi: Int) {
           val hieu=totalLuongThu-totalLuongChi
            binding.txtTietKiem.text="$hieu"
        if (hieu<=0){
            showtietkienlo()
        }else{
            showtietkienlai()
        }
    }

    private fun showtietkienlai() {
        val build=AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.customdialoglai,null)
        build.setView(view)
        dialog=build.create()
        dialog.show()
    }

    private fun showtietkienlo() {
        val build= AlertDialog.Builder(this)
        val view=layoutInflater.inflate(R.layout.customdialog,null)
        build.setView(view)
        dialog=build.create()
        dialog.show()
    }
}