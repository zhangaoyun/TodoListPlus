package com.example.aoyun.todolistplus;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aoyun.todolistplus.view.MainActivity;
import com.example.aoyun.todolistplus.view.Notification;

import java.util.Calendar;




public class TodoActivity extends MainActivity {

    private AlarmManager alarmManager;
    private PendingIntent pi;

    TextView alarm_time ;


    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;

    private static int system_year;
    private static int system_month;
    private static int system_day;
    private static int system_hour;
    private static int system_minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        alarm_time =findViewById(R.id.alarm_time);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(TodoActivity.this, Notification.class);
        pi = PendingIntent.getActivity(TodoActivity.this, 0, intent, 0);


        Button query_button = findViewById(R.id.query);
        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDbHelper mHelper = null;
                SQLiteDatabase db = mHelper.getWritableDatabase();
                Cursor cursor =db.query("tasks",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String title =cursor.getString(cursor.getColumnIndex("title"));
                        Log.d("TodoActivity",title);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        Button button1 = findViewById(R.id.set_notification);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotificationTime();
            }
        });
    }

//    public void deleteTask(View view) {
//    }

    private void setNotificationTime() {
        Calendar calendar = Calendar.getInstance();
        system_year = calendar.get(Calendar.YEAR);
        system_month = calendar.get(Calendar.MONTH);
        system_day = calendar.get(Calendar.DAY_OF_MONTH);
        system_hour = calendar.get(Calendar.HOUR_OF_DAY);
        system_minute = calendar.get(Calendar.MINUTE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(TodoActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, final int month, int dayOfMonth) {
                        mMonth = month;
                        mDay = dayOfMonth;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                setClock(mMonth, mDay, mHour, mMinute);
                            }
                        }, system_hour, system_minute, true);
                        timePickerDialog.show();
                    }
                }, system_year, system_month, system_day);
        datePickerDialog.show();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis());
    }
    private void setClock(int month, int day, int hour, int minute) {


        Calendar clockCalendar = Calendar.getInstance();
        clockCalendar.set(Calendar.MONTH, month);
        clockCalendar.set(Calendar.DAY_OF_MONTH, day);
        clockCalendar.set(Calendar.HOUR_OF_DAY, hour);
        clockCalendar.set(Calendar.MINUTE, minute);

        Toast.makeText(this, "提醒设置时间为：" + month + "月" + day + "日" + hour + "时" + minute + "分", Toast.LENGTH_SHORT).show();
        alarm_time.setText("设置的提醒时间为："+"\n"+clockCalendar.getTime());
        alarmManager.set(AlarmManager.RTC_WAKEUP, clockCalendar.getTimeInMillis(), pi);
//        //下面是通知
//
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        android.app.Notification notification = new NotificationCompat.Builder(this, "default")
//                .setAutoCancel(true)
//                .setContentTitle("This is content title")
//                .setContentText("This is content text")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setContentIntent(pi)
////                .setSound(Uri.fromFile(new File("/storage/emulated/0/KuwoMusic/music/都选C.mp3")))
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setLights(Color.GREEN, 1000, 1000)
//                .build();
//        manager.notify(1, notification);
    }
}
