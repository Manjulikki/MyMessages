package manjunath.com.mymessages.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static String getDate(Long  milliSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliSeconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }
}
