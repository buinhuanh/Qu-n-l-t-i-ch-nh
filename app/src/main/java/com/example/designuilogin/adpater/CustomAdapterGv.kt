package com.example.designuilogin.adpater

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.designuilogin.R
import com.example.layoutrecycleview.outdataGv

class CustomAdapterGv (val activity: Activity,val list:List<outdataGv>):
ArrayAdapter<outdataGv>(activity, R.layout.layout_gridview_item){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs=activity.layoutInflater
        val roView=contexs.inflate(R.layout.layout_gridview_item,parent,false)
        val imgThu=roView.findViewById<ImageView>(R.id.imgThu)
        val txtThu=roView.findViewById<TextView>(R.id.txtThu)
        imgThu.setImageResource(list[position].img)
        txtThu.text=list[position].ten
        return roView
    }
}