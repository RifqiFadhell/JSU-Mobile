package id.fadhell.testpayfazz.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fadhell.testpayfazz.R
import id.fadhell.testpayfazz.database.NewsEntity
import kotlinx.android.synthetic.main.news_item.view.*

class NewsOfflineAdapter(
    private var items: List<NewsEntity>?, private var context: Context,
    listener: OnItemClickOffline
) : RecyclerView.Adapter<ViewHolderOffline>() {

    var listenerClick: OnItemClickOffline? = null

    init {
        listenerClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOffline {
        return ViewHolderOffline(
            LayoutInflater.from(context).inflate(R.layout.news_item, parent, false)
        )
    }

    fun updateList(list: List<NewsEntity>) {
        items = list
         notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolderOffline, position: Int) {
        val item = items?.get(position)

        holder.views.textTitle.text = item?.title
        holder.views.textSubTitle.text = item?.description
        Glide.with(context)
            .load(item?.urlImage)
            .into(holder.views.image)
        holder.views.setOnClickListener {
            if (item != null) {
                listenerClick?.itemClicked(position, holder.views, item)
            }
        }
    }
}

class ViewHolderOffline(view: View) : RecyclerView.ViewHolder(view) {
    val views = view.layoutItem
}

interface OnItemClickOffline {
    fun itemClicked(position: Int, view: View, news: NewsEntity)
}