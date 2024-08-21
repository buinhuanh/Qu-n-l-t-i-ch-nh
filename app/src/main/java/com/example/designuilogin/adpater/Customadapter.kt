package com.example.designuilogin.adpater

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.designuilogin.R
import com.example.layoutapp.outdata

class customadapter(val activity: Activity,val list: List<outdata>): ArrayAdapter<outdata>(activity,
    R.layout.list_item) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexs=activity.layoutInflater
        val roView=contexs.inflate(R.layout.list_item,parent,false)
        val img=roView.findViewById<ImageView>(R.id.images)
        val title=roView.findViewById<TextView>(R.id.title)
        val desc=roView.findViewById<TextView>(R.id.desc)
        title.text=list[position].title
        desc.text=list[position].desc
        img.setImageResource(list[position].img)
        return roView
    }
}