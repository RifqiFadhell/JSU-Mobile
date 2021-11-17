package id.jsu.suntiq.utils
import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

object UnitFilter {

    fun convertToComma(value: Int?): String {
        val decimalFormat: DecimalFormat = NumberFormat.getInstance(Locale.ENGLISH) as DecimalFormat
        decimalFormat.applyPattern("#,###")
        return decimalFormat.format(value)
    }

    fun convertToRupiah(value: Int?): String {
        val decimalFormat: DecimalFormat = NumberFormat.getInstance(Locale.ENGLISH) as DecimalFormat
        return "Rp ${decimalFormat.format(value)}"
    }

    fun dpToPx(resources: Resources, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).roundToInt()
    }

    fun Bitmap?.convertToUri(activity: Activity?): Uri {
        if (this == null) return Uri.parse("")
        val imgBitmapPath: String =
            MediaStore.Images.Media.insertImage(activity?.contentResolver, this, "${
                UUID
                .randomUUID()} image",
                null)

        return Uri.parse(imgBitmapPath)
    }
}