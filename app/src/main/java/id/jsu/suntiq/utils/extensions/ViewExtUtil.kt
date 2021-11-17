package id.jsu.suntiq.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.ImageView
import id.jsu.suntiq.utils.ImageUtils.Companion.loadImages
import id.jsu.suntiq.utils.UnitFilter

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.toEnable() {
    this.isEnabled = true
}

fun View.toDisable() {
    this.isEnabled = false
}

fun ImageView.loadImage(context: Context, url: String?, drawable: Int) {
  loadImages(context, this, url, drawable)
}

fun Boolean.toVisibility(
    trueVisibility: Int = View.VISIBLE,
    falseVisibility: Int = View.GONE
): Int = if (this) trueVisibility else falseVisibility


fun View.getBitmapFromView(): Bitmap {
    val manualWidth = View.MeasureSpec.makeMeasureSpec(
        UnitFilter.dpToPx(resources, 400),
        View.MeasureSpec.EXACTLY
    )
    this.measure(manualWidth, manualWidth)
    val width = measuredWidth

    val bitmap = Bitmap.createBitmap(
        width, UnitFilter.dpToPx(this.resources, 530), Bitmap.Config
            .ARGB_8888
    )
    val canvas = Canvas(bitmap)

    layout(left, top, right, bottom)
    draw(canvas)

    return bitmap
}