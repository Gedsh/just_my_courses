package pan.alexander.notes.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static final float STATE_SELECTED_COLOR_DARKNESS = .4f;

    public static String formatTime(long time, boolean showDateTime) {
        Date date = new Date(time);
        Date currentDate = new Date();
        DateFormat dateFormat;
        if (Utils.isSameDay(date, currentDate)) {
            dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        } else if (showDateTime) {
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        } else {
            dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        }
        return dateFormat.format(date);
    }

    private static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static ColorStateList calculateColorStateList(int color) {
        int colorSelected = ColorUtils.blendARGB(color, Color.BLACK, STATE_SELECTED_COLOR_DARKNESS);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{}
        };

        int[] colors = new int[]{
                colorSelected,
                color
        };

        return new ColorStateList(states, colors);
    }

    public static String colorIntToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
