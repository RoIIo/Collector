package eu.mobile.application.collector.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import eu.mobile.application.collector.R
import eu.mobile.application.collector.entity.Position

class PositionListAdapter(private val context: Context, private var positions: ArrayList<Position>)
    : BaseAdapter(), Filterable {

    private var filteredList: ArrayList<Position> = positions
    /*
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.list_position, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
       // val imageView = rowView.findViewById(R.id.icon) as ImageView
        //val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = filteredList[position].name
        //imageView.setImageResource(imgid[position])
        //subtitleText.text = description[position]

        return rowView
    }
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_position, parent, false)
        } else {
            view = convertView
        }

        val item = positions[position]
        val titleText = view.findViewById(R.id.title) as TextView
        titleText.text = item.name

        return view
    }
    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): Position? {
        return filteredList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = if (constraint.isNullOrBlank()) {
                    positions
                } else {
                    positions.filter{ it.name?.contains(constraint, ignoreCase = true)?: false }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredList = results.values as ArrayList<Position>
                notifyDataSetChanged()
            }
        }
    }
    inner class ViewHolder(itemView: View) {
        // Inicjalizuj elementy widoku
        //val textView: TextView = itemView.findViewById(R.id.title)
        //val imageView: ImageView = itemView.findViewById(R.id.image_view)
        // ...
    }
}