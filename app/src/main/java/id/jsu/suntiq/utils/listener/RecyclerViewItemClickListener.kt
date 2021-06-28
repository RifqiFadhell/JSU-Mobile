package id.jsu.suntiq.utils.listener

import androidx.annotation.IdRes


interface RecyclerViewItemClickListener<in T> {
  fun itemClick(position: Int, item: T?, @IdRes viewId: Int)
}