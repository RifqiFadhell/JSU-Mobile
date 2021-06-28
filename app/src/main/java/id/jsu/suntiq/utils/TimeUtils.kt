package id.jsu.suntiq.utils
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object TimeUtils {

    fun getFormattedDate(): String {

        val dates: String

        dates = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val answer: String = current.format(formatter)
            DateUtils.formatDate(answer, "EEE, dd MMMM yyyy")

        } else {

            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val answer: String = formatter.format(date)
            DateUtils.formatDate(answer, "EEE, dd MMMM yyyy")
        }

        return dates
    }


    fun getDate(): String {
        val date: String?

        date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val answer: String = current.format(formatter)
            answer

        } else {

            val dates = Date()
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val answer: String = formatter.format(dates)
            answer
        }
        return date
    }

    fun getFullDate(): String {
        val date: String?

        date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val answer: String = current.format(formatter)
            answer

        } else {

            val dates = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val answer: String = formatter.format(dates)
            answer
        }
        return date
    }

    fun getDateReverse(): String {
        val date: String?

        date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val answer: String = current.format(formatter)
            answer

        } else {

            val dates = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val answer: String = formatter.format(dates)
            answer
        }
        return date
    }

    fun getTime(): String {
        val timeZone = TimeZone.getTimeZone("GMT+7")
        val calendar = Calendar.getInstance(timeZone)
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE))
    }

    fun getFullTime(): String {
        val timeZone = TimeZone.getTimeZone("GMT+7")
        val calendar = Calendar.getInstance(timeZone)
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" +
                String.format("%02d", calendar.get(Calendar.MILLISECOND))
    }
}