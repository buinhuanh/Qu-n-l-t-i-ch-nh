package com.example.designuilogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.designuilogin.adpater.RvAnhChi
import com.example.designuilogin.adpater.RvAnhThu
import com.example.designuilogin.databinding.ActivityShowImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class showImageActivity : AppCompatActivity() {

    private lateinit var adaperThu: RvAnhThu
    private val listAnhThu = mutableListOf<String>()


    private lateinit var adaperChi: RvAnhChi
    private val listAnhChi = mutableListOf<String>()

    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var binding: ActivityShowImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvThu.layoutManager=LinearLayoutManager(this)
        adaperThu= RvAnhThu(listAnhThu,this,this)
        binding.rvThu.adapter=adaperThu

        binding.rvChi.layoutManager=LinearLayoutManager(this)
        adaperChi= RvAnhChi(listAnhChi,this,this)
        binding.rvChi.adapter=adaperChi


        binding.rvThu.visibility=View.GONE
        binding.rvChi.visibility=View.GONE
        auth=FirebaseAuth.getInstance()
        storageRef=FirebaseStorage.getInstance("gs://design-ui-login-8513d.appspot.com").reference.child("images")
        loadAnhThu()
        loadAnhChi()
    }

    private fun loadAnhChi() {
        val curentUser=auth.currentUser
        curentUser?.let { user->
            val userId=(user.email?:"").substringBefore("@")
            val imagesRef=storageRef.child("$userId/Nam/Chi/image/")
            listAnhChi.clear()
            adaperChi.notifyDataSetChanged()
            updateViewVisibility()
            imagesRef.listAll().addOnSuccessListener { listResult->
                for (item in listResult.items){
                    listAnhChi.add(item.path)
                }
                adaperChi.notifyDataSetChanged()
                updateViewVisibility()
            }
        }
    }

    private fun loadAnhThu() {
       val currentUser=auth.currentUser
        currentUser?.let { user->
            val userId=(user.email ?:"").substringBefore("@")
            val imagesRef=storageRef.child("$userId/Nam/Thu/image/")
            listAnhThu.clear()
            adaperThu.notifyDataSetChanged()
            updateViewVisibility()
            imagesRef.listAll().addOnSuccessListener { listResult->
                for(item in listResult.items){
                    listAnhThu.add(item.path)
                }
                adaperThu.notifyDataSetChanged()
                updateViewVisibility()
            }
        }
    }

    private fun updateViewVisibility() {
        if (listAnhThu.isEmpty()&&listAnhChi.isEmpty()){
            binding.rvThu.visibility=View.GONE
            binding.rvChi.visibility=View.GONE
        }
        else{
            if (listAnhThu.isNotEmpty()){
                binding.rvThu.visibility=View.VISIBLE
            }
            if (listAnhChi.isNotEmpty()){
                binding.rvChi.visibility=View.VISIBLE
            }
        }
    }

    fun onImageLongClick(imagePath: String,position: Int) {

        AlertDialog.Builder(this)
            .setTitle("Xóa ảnh ")
            .setMessage("Bạn có chắc chắn muốn xóa ảnh này không?")
            .setPositiveButton("Có"){ dialog,_ ->
                val imageRef=FirebaseStorage.getInstance("gs://design-ui-login-8513d.appspot.com").reference.child(imagePath)
                imageRef.delete().addOnSuccessListener {
                   if (listAnhThu.contains(imagePath)){
                       val index=listAnhThu.indexOf(imagePath)
                       listAnhThu.removeAt(index)
                      adaperThu.notifyItemChanged(index)
                       adaperThu.notifyItemChanged(index,listAnhThu.size)
                   } else if (listAnhChi.contains(imagePath)){
                       val index=listAnhChi.indexOf(imagePath)
                       listAnhChi.removeAt(index)
                       adaperChi.notifyItemChanged(index)
                       adaperChi.notifyItemChanged(index,listAnhChi.size)
                   }
                    Toast.makeText(this,"Đã xóa ảnh",Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNeutralButton("Không"){ dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}