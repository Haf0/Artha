
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    // Get start and end of a specific month
    fun getStartAndEndOfSpecificMonth(timestamp: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    fun Long.toFormattedDate(): String {
        val date = Date(this)
        val format = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
        return format.format(date)
    }

    fun Long.toFormattedFullDate(): String {
        val date = Date(this)
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return format.format(date)
    }

    private fun getTodayDate(): String {
        val formatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun getTodayTimestamp(): Long {
        return convertDateStringToLong(getTodayDate())
    }

    fun convertDateStringToLong(dateString: String): Long {
        val formatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val date = formatter.parse(dateString)
        return date?.time ?: 0L
    }

    fun getEndOfDay(endDate: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = endDate
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    fun getStartOfDay(startDate: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = startDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }


    fun getTodayMonthAndYear(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        return Pair(year, month)
    }

}
