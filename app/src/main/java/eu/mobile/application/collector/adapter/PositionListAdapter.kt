package eu.mobile.application.collector.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import eu.mobile.application.collector.R

class PositionListAdapter(private val context: Context, private val title: ArrayList<String>, private val description: ArrayList<String>, private val imgid: ArrayList<Int>)
    : ArrayAdapter<String>(context, R.layout.list_position, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.list_position, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
       // val imageView = rowView.findViewById(R.id.icon) as ImageView
        //val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = title[position]
        //imageView.setImageResource(imgid[position])
        //subtitleText.text = description[position]

        return rowView
    }
}