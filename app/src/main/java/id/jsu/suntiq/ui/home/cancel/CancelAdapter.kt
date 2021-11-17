package id.jsu.suntiq.ui.home.cancel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.jsu.suntiq.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.cancel_item.view.*

class CancelAdapter(
    private var items: List<String>,
    private var context: Context,
    private val onItemClick: ((Int, String) -> Unit)? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.cancel_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
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

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItems(
            position: Int,
            item: String,
            onItemClick: ((Int, String) -> Unit)? = null
        ) {
            containerView.textItem.text = item
            if (selectedItem == position) {
                containerView.textItem.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_check), null, null, null)
            } else {
                containerView.textItem.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_circle_gray), null, null, null)
            }
            containerView.layoutItem.setOnClickListener {
                selectTaskListItem(position)
                onItemClick?.invoke(position, item)
            }
        }
    }
}