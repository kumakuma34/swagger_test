package TemplateClass;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;

    public static boolean compareTime(String post_create_time, String time, boolean updateNow){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        post_create_time = post_create_time.substring(0,16);
        post_create_time = post_create_time.replace("T", "-");
        post_create_time = post_create_time.replace(":", "-");
        try{
            Calendar calendar = Calendar.getInstance();

            if(updateNow){
                time = post_create_time.substring(0,16);
                time = post_create_time.replace("T", "-");
                time = post_create_time.replace(":", "-");
            }
            Date date_time = formatter.parse(time);
            if(updateNow){
                calendar.setTime(date_time);
                //TODO: change magic number to enum
                calendar.add(Calendar.HOUR_OF_DAY, 9);
                date_time = calendar.getTime();
            }
            Date post_time = formatter.parse(post_create_time);
            calendar.setTime(post_time);
            //TODO: change magic number to enum
            calendar.add(Calendar.HOUR_OF_DAY, 9);
            post_time = calendar.getTime();
            int compareRes = post_time.compareTo(date_time);
            return (compareRes>0) ? true:false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static String getCurrentTimeMinusMin(){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);
        date = calendar.getTime();
        String dateToStr = formatter.format(date);
        System.out.println(dateToStr);
        return dateToStr;
    }

    public static String getCurrentTimeString(){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        Date date = new Date(System.currentTimeMillis());
        String dateToStr = formatter.format(date);
        System.out.println(dateToStr);
        return dateToStr;
    }

    public static String getDayBeforeString(){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        String dateToStr = formatter.format(date);
        System.out.println(dateToStr);
        return dateToStr;
    }

    public static String getWeekBeforeString(){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -7);
        date = calendar.getTime();
        String dateToStr = formatter.format(date);
        System.out.println(dateToStr);
        return dateToStr;
    }

    public static String getMonthBeforeString(){
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();
        String dateToStr = formatter.format(date);
        System.out.println(dateToStr);
        return dateToStr;
    }
    public static String timeToString(String day){
        String res = "";
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm"));
        try{
            Calendar calendar = Calendar.getInstance();
            Date date = formatter.parse(day);
            calendar.setTime(date);
            date = calendar.getTime();
            Timestamp st = new Timestamp(date.getTime());
            res = st.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static String dayToString(String day){
        String res = "";
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd"));
        try{
            Calendar calendar = Calendar.getInstance();
            Date date = formatter.parse(day);
            calendar.setTime(date);
            date = calendar.getTime();
            Timestamp st = new Timestamp(date.getTime());
            res = st.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public static String dayToStringAddDay(String day){
        String res = "";
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd"));
        try{
            Calendar calendar = Calendar.getInstance();
            Date date = formatter.parse(day);
            calendar.setTime(date);
            calendar.add(Calendar.DATE,1);
            date = calendar.getTime();
            Timestamp st = new Timestamp(date.getTime());
            res = st.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public static Timestamp Dateformatting(String time){
        time = time.substring(0,18);
        time = time.replace("T", "-");
        time = time.replace(":", "-");
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-MM-dd-HH-mm-ss"));
        Date date = new Date();
        Timestamp st = new Timestamp(date.getTime());
        try{
            Calendar calendar = Calendar.getInstance();
            Date post_time = formatter.parse(time);
            calendar.setTime(post_time);
            //TODO: change magic number to enum
            calendar.add(Calendar.HOUR_OF_DAY, 9);
            post_time = calendar.getTime();
            st = new Timestamp(post_time.getTime());
            return st;
        }catch(Exception e){
            e.printStackTrace();
        }
        return st;
    }

}
