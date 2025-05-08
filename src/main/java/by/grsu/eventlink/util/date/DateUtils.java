package by.grsu.eventlink.util.date;

import java.sql.Date;
import java.time.Period;
import java.util.Calendar;

public class DateUtils {

    public static Date getDatePlusDays(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DATE, amount);

        return new Date(calendar.getTimeInMillis());
    }

    public static Date getDateNow() {
        return DateUtils.getDatePlusDays(0);
    }

    public static int calculateAge(Date birthDate) {
        return Period.between(birthDate.toLocalDate(), getDateNow().toLocalDate()).getYears();
    }

}
