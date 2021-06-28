package id.jsu.suntiq.ui.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.jsu.suntiq.R
import id.jsu.suntiq.preference.room.DataEntity
import kotlinx.android.synthetic.main.vehicle_item.view.*

class VehicleOfflineAdapter(
    private var items: List<DataEntity>?, private var context: Context,
    listener: OnItemClickOffline
) : RecyclerView.Adapter<ViewHolderOffline>() {

    var listenerClick: OnItemClickOffline? = null

    init {
        listenerClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOffline {
        return ViewHolderOffline(
            LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolderOffline, position: Int) {
        val item = items?.get(position)
        val chasing = item?.chassingNumber
        val category = item?.category
        val type = item?.type

        holder.views.textNumberPlate.text = item?.policeNumber
        holder.views.textNameVehicle.text = "$type , $category , $chasing"
        holder.views.layoutItem.setOnClickListener {
            if (item != null) {
                listenerClick?.itemClicked(position, holder.views, item)
            }
        }

        if (category == "R2") {
            holder.views.image.setImageResource(R.drawable.ic_motorcycle)
        } else if (category == "R4") {
            holder.views.image.setImageResource(R.drawable.ic_car)
        }
    }
}

class ViewHolderOffline(view: View) : RecyclerView.ViewHolder(view) {
    val views = view.layoutItem
}

interface OnItemClickOffline {
    fun itemClicked(position: Int, view: View, vehicle: DataEntity)
}