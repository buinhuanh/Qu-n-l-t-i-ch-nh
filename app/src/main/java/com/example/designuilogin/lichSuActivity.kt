package com.example.designuilogin

import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.designuilogin.adpater.LichsuChiAdapter
import com.example.designuilogin.adpater.LichsuThuAdapter
import com.example.designuilogin.databinding.ActivityLichSuBinding
import com.example.designuilogin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var binding: ActivityLichSuBinding
class lichSuActivity : AppCompatActivity() {
    private lateinit var dsThu:ArrayList<employeeThu>
    private lateinit var dsChi:ArrayList<employeeChi>
    private lateinit var dbRef : DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var databaseReferenceThu: DatabaseReference
    private lateinit var databaseReferenceChi: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLichSuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvLsThu.layoutManager=LinearLayoutManager(this)
        binding.rvLsChi.layoutManager=LinearLayoutManager(this)
        dsThu=ArrayList()
        dsChi=ArrayList()
        fetchData()
    }

    private fun fetchData() {
        dbRef=FirebaseDatabase.getInstance("https://design-ui-login-8513d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        currentUser= auth.currentUser!!
        val userId=(currentUser.email?:"").substringBefore("@")
        databaseReferenceThu=dbRef.child(userId).child("Nam").child("Thu")
        databaseReferenceChi=dbRef.child(userId).child("Nam").child("Chi")


        databaseReferenceThu.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dsThu.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empDataThu=empSnap.getValue(employeeThu::class.java)
                        if (empDataThu != null) {
                            dsThu.add(empDataThu)
                        }
                    }
                    val mAdapterThu=LichsuThuAdapter(dsThu)
                    binding.rvLsThu.adapter=mAdapterThu
                    mAdapterThu.setOnItemClickListener(object :LichsuThuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val i=Intent(this@lichSuActivity,UpdateThu::class.java)
                            i.putExtra("idThu",dsThu[position].idThu)
                            i.putExtra("dateThu",dsThu[position].dateThu)
                            i.putExtra("timeThu",dsThu[position].timeThu)
                            i.putExtra("luongThu",dsThu[position].luongThu)
                            i.putExtra("khoanThu",dsThu[position].khoanThu)
                            startActivity(i)
                        }
                    })
                    if (dsThu.isEmpty()){
                        binding.rvLsThu.visibility= View.GONE
                    }else{
                        binding.rvLsThu.visibility=View.VISIBLE
                    }
            }else{
                binding.rvLsThu.visibility=View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@lichSuActivity,"Không thể truy vấn được dữ liệu",Toast.LENGTH_SHORT).show()
            }
        })
        databaseReferenceChi.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dsChi.clear()
                if (snapshot.exists()){
                    for (emap in snapshot.children){
                        val empDataChi=emap.getValue(employeeChi::class.java)
                        if (empDataChi != null) {
                            dsChi.add(empDataChi)
                        }
                        val mAdapter=LichsuChiAdapter(dsChi)
                        binding.rvLsChi.adapter=mAdapter
                        mAdapter.setOnItemClickListener(object :LichsuChiAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val i = Intent(this@lichSuActivity, UpdateChi::class.java)
                                i.putExtra("idChi",dsChi[position].idChi)
                                i.putExtra("dateChi",dsChi[position].dateChi)
                                i.putExtra("timeChi",dsChi[position].timeChi)
                                i.putExtra("khoanChi",dsChi[position].khoanChi)
                                i.putExtra("luongChi",dsChi[position].luongChi)
                                startActivity(i)
                            }

                        })
                    }
                    if (dsChi.isEmpty()){
                        binding.rvLsChi.visibility= View.GONE
                    }else{
                        binding.rvLsChi.visibility=View.VISIBLE
                    }
                }else{
                    binding.rvLsChi.visibility=View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@lichSuActivity, "Lỗi không thể hiện thị", Toast.LENGTH_SHORT).show()
            }
        })
    }
}