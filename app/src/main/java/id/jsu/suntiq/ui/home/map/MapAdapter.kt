package id.jsu.suntiq.ui.home.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.jsu.suntiq.R
import id.jsu.suntiq.api.response.location.LocationItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.destination_item.view.*

class MapAdapter(
    private var items: List<LocationItem>,
    private var context: Context,
    private val onItemClick: ((Int, LocationItem) -> Unit)? = null,
    private val onLocationClick: ((Int, LocationItem) -> Unit)? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderOnline(
            LayoutInflater.from(context).inflate(R.layout.destination_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderOnline -> {
                items[position].let { holder.bindItems(position, it, onItemClick) }
            }
        }
    }

    fun selectTaskListItem(pos: Int) {
        val previousItem = selectedItem
        selectedItem = pos
        if (previousItem != null) {
            notifyItemChanged(previousItem)
        }
        notifyItemChanged(pos)
    }

    inner class ViewHolderOnline(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItems(
            position: Int,
            item: LocationItem,
            onItemClick: ((Int, LocationItem) -> Unit)? = null
        ) {
            if (selectedItem == position) {
                containerView.buttonSubmit.icon = ContextCompat.getDrawable(context, R.drawable.ic_verified)
                containerView.buttonSubmit.text = context.getString(R.string.delivery_button_selected_title)
            } else {
                containerView.buttonSubmit.icon = null
                containerView.buttonSubmit.text = context.getString(R.string.delivery_button_title)
            }
            containerView.textNameCity.text = item.officeName
            containerView.textAddress.text = item.branchAddress
            containerView.textTime.text = "08:00 s/d 17:00"
            containerView.buttonSubmit.setOnClickListener {
                selectTaskListItem(position)
                onItemClick?.invoke(position, item)
            }
            containerView.buttonShowPoint.setOnClickListener {
                onLocationClick?.invoke(
                    position,
                    item
                )
            }
        }
    }
}