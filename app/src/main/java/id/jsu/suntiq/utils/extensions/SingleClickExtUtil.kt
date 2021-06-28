package id.jsu.suntiq.utils.extensions

import android.view.View

fun View.setSingleClickListener(onSafeClick: (View) -> Unit) {
  val safeClickListener = OneClickListener {
    onSafeClick(it)
  }
  setOnClickListener(safeClickListener)
}
