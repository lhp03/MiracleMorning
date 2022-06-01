package swdm2016.gachon.mr_demo;

import java.io.Serializable;
import java.util.Calendar;

public class Routine implements Serializable {
    private int id;
    private String title;
    private int start_hour;
    private int start_minutes;
    private int end_hour;
    private int end_minutes;
    private String days;
    private int start_alarm;
    private int end_alarm;
    private int end_alarm_time;

    public Routine(String title, int start_hour, int start_minutes, int end_hour, int end_minutes, String days, int start_alarm, int end_alarm, int end_alarm_time) {
        this.title = title;
        this.start_hour = start_hour;
        this.start_minutes = start_minutes;
        this.end_hour = end_hour;
        this.end_minutes = end_minutes;
        this.days = days;
        this.start_alarm = start_alarm;
        this.end_alarm = end_alarm;
        this.end_alarm_time = end_alarm_time;
    }

    public Routine(int id, String title, int start_hour, int start_minutes, int end_hour, int end_minutes, String days, int start_alarm, int end_alarm, int end_alarm_time) {
        this(title, start_hour, start_minutes, end_hour, end_minutes, days, start_alarm, end_alarm, end_alarm_time);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public int getStart_minutes() {
        return start_minutes;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public int getEnd_minutes() {
        return end_minutes;
    }

    public String getDays() {
        return days;
    }

    public int getId() {
        return id;
    }

    public int getStart_alarm() {
        return start_alarm;
    }

    public int getEnd_alarm() {
        return end_alarm;
    }

    public int getEnd_alarm_time() {
        return end_alarm_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNextStartAlarm() {
        Calendar cal = Calendar.getInstance();

        int today_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        Calendar recent_alarm = null;
        long min_dif = 0L;

        for(int i = 0; i < this.days.length(); i++) {
            if(days.charAt(i) == '0') continue;

            Calendar temp_cal = Calendar.getInstance();
            temp_cal.set(Calendar.HOUR_OF_DAY, this.start_hour);
            temp_cal.set(Calendar.MINUTE, this.start_minutes);
            temp_cal.set(Calendar.SECOND, 0);

            if(i < today_index) {
                temp_cal.add(Calendar.DATE, i - today_index + 7);
            } else if (i == today_index) {
                if(temp_cal.before(cal)) {
                    temp_cal.add(Calendar.DATE, 7);
                }
            } else {
                temp_cal.add(Calendar.DATE, i - today_index);
            }

            if(recent_alarm == null) {
                recent_alarm = temp_cal;
                min_dif = temp_cal.getTimeInMillis() - cal.getTimeInMillis();
            } else {
                if(min_dif > temp_cal.getTimeInMillis() - cal.getTimeInMillis()) {
                    recent_alarm = temp_cal;
                    min_dif = temp_cal.getTimeInMillis() - cal.getTimeInMillis();
                }
            }
        }

        long resultTime = 0L;
        if(recent_alarm != null) {
            resultTime = recent_alarm.getTimeInMillis();
        }

        return resultTime;
    }

    public long getNextEndAlarm() {
        Calendar cal = Calendar.getInstance();

        int today_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        Calendar recent_alarm = null;
        long min_dif = 0L;

        for(int i = 0; i < this.days.length(); i++) {
            if(days.charAt(i) == '0') continue;

            Calendar temp_cal = Calendar.getInstance();

            int hour = this.end_hour;
            int minute = this.end_minutes - end_alarm_time;

            if(minute < 0) {
                minute += 60;
                hour = hour - 1;
            }

            temp_cal.set(Calendar.HOUR_OF_DAY, hour);
            temp_cal.set(Calendar.MINUTE, minute);
            temp_cal.set(Calendar.SECOND, 0);

            if(i < today_index) {
                temp_cal.add(Calendar.DATE, i - today_index + 7);
            } else if (i == today_index) {
                if(temp_cal.before(cal)) {
                    temp_cal.add(Calendar.DATE, 7);
                }
            } else {
                temp_cal.add(Calendar.DATE, i - today_index);
            }

            if(recent_alarm == null) {
                recent_alarm = temp_cal;
                min_dif = temp_cal.getTimeInMillis() - cal.getTimeInMillis();
            } else {
                if(min_dif > temp_cal.getTimeInMillis() - cal.getTimeInMillis()) {
                    recent_alarm = temp_cal;
                    min_dif = temp_cal.getTimeInMillis() - cal.getTimeInMillis();
                }
            }
        }

        long resultTime = 0L;
        if(recent_alarm != null) {
            resultTime = recent_alarm.getTimeInMillis();
        }

        return resultTime;
    }
}
