package com.example.designuilogin.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.designuilogin.R
import com.example.designuilogin.showImageActivity
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class RvAnhChi(
    private val ds:MutableList<String>,
    private val context: Context,
    private val listtener: showImageActivity
) :RecyclerView.Adapter<RvAnhChi.ThuViewHolder>(){
    inner class ThuViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val imageChi: ImageView =  itemView.findViewById(R.id.imageThu)}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThuViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_recyclerview_item,parent,false)
        return ThuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ds.size
    }

    override fun onBindViewHolder(holder: ThuViewHolder, position: Int) {
       val imageRef=FirebaseStorage.getInstance("gs://design-ui-login-8513d.appspot.com").reference.child(ds[position])
       imageRef.downloadUrl.addOnSuccessListener { uri->
           Picasso.get().load(uri).into(holder.imageChi)
           holder.imageChi.setOnClickListener{
               listtener.onImageLongClick(ds[position], position)
               true
           }
       }
    }
}
