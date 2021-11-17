package id.jsu.suntiq.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.jsu.suntiq.R
import id.jsu.suntiq.api.response.login.User
import id.jsu.suntiq.api.response.vehicle.confirmation.DataConfirmation
import id.jsu.suntiq.api.response.vehicle.delivery.DataDelivery
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_PROFILE
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TEMPORARY_DATA_CONFIRM_DELIVERY
import id.jsu.suntiq.preference.tinyDb.TinyConstant.TINY_TOKEN
import id.jsu.suntiq.preference.tinyDb.TinyDB

fun Context.getScreenWidthInDPs(): Int {
    val dm = DisplayMetrics()
    val windowManager = this.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(dm)
    return Math.round(dm.widthPixels / dm.density)
}

fun Context.getActionBarHeight(): Int {
    val textSizeAttr = intArrayOf(R.attr.actionBarSize)
    val a = this.obtainStyledAttributes(TypedValue().data, textSizeAttr)
    val height = a.getDimensionPixelSize(0, 0)
    a.recycle()
    return height
}

fun Context.showOkDialog(
    message: String,
    positiveText: String,
    listener: DialogInterface.OnClickListener?
) {
    val builder = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText, listener)
        .setCancelable(false)
    val mAlertBuilder = builder.create()
    mAlertBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mAlertBuilder.show()
}

fun Activity.showOkDialog(
    message: String,
    positiveText: String,
    listener: DialogInterface.OnClickListener?
) {
    val builder = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText, listener)
        .setCancelable(false)
    val mAlertBuilder = builder.create()
    mAlertBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mAlertBuilder.show()
}

fun Activity.showToast(message: String, long: Int) {
    Toast.makeText(this, message, long).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showYesNoDialog(
    message: String, positiveText: String, negativeText: String,
    yesListener: DialogInterface.OnClickListener
) {
    lateinit var alertBuilder: AlertDialog
    val builder = AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText, yesListener)
        .setNegativeButton(negativeText) { _, _ -> alertBuilder.dismiss() }
    alertBuilder = builder.create()
    alertBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
    alertBuilder.show()
}

fun Context.getDataUser(): User? {
    var loginResponse: User? = null
    if (TinyDB(this).getObject(TINY_PROFILE, User::class.java) != null) {
        loginResponse = TinyDB(this).getObject(TINY_PROFILE, User::class.java)
    }
    return loginResponse
}

fun Context.getToken(): String? {
    var token: String? = ""
    if ( TinyDB(this).getString(TINY_TOKEN) != null) {
        token = TinyDB(this).getString(TINY_TOKEN)
    }
    return token
}

fun Context.getDataExistingProgress(): DataConfirmation? {
    var data: DataConfirmation? = null
    if (TinyDB(this).getObject(TINY_TEMPORARY_DATA_CONFIRM, DataConfirmation::class.java) != null) {
        data = TinyDB(this).getObject(TINY_TEMPORARY_DATA_CONFIRM, DataConfirmation::class.java)
    }
    return data
}

fun Context.getDataExistingProgressDelivery(): DataDelivery? {
    var data: DataDelivery? = null
    if (TinyDB(this).getObject(TINY_TEMPORARY_DATA_CONFIRM_DELIVERY, DataDelivery::class.java) != null) {
        data = TinyDB(this).getObject(TINY_TEMPORARY_DATA_CONFIRM_DELIVERY, DataDelivery::class.java)
    }
    return data
}
