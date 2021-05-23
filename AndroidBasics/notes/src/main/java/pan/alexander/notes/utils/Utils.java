package pan.alexander.notes.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static String formatTime(long time) {
        Date date = new Date(time);
        Date currentDate = new Date();
        DateFormat dateFormat;
        if (Utils.isSameDay(date, currentDate)) {
            dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        } else {
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        }
        return dateFormat.format(date);
    }
}
