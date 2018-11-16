package com.example.aoyun.todolistplus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TodoActivity extends MainActivity {

    private AlarmManager alarmManager;
    private PendingIntent pi;

    TextView alarm_time ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        alarm_time =findViewById(R.id.alarm_time);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(TodoActivity.this, com.example.aoyun.todolistplus.Notification.class);
        pi = PendingIntent.getActivity(TodoActivity.this, 0, intent, 0);


        Button button = findViewById(R.id.query);
        button.setOnClickListener(new View.OnClickListener() {
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
                Calendar currentTime = Calendar.getInstance();
                new TimePickerDialog(TodoActivity.this, 0,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //设置当前时间
                                Calendar c = Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.HOUR, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND,0);
                                // 设置AlarmManager在Calendar对应的时间启动Activity
                                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                                Log.e("HEHE",c.getTimeInMillis()+"");   //这里的时间是一个unix时间戳
                                // 提示闹钟设置完毕:
                                Toast.makeText(TodoActivity.this, "闹钟设置完毕~"+"\n"+c.getTime(),
                                        Toast.LENGTH_SHORT).show();
                                alarm_time.setText("设置的闹钟时间为："+"\n"+c.getTime());
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show();
            }
        });

//        Button test_button = findViewById(R.id.test);
//        test_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new TimePickerDialog(TodoActivity.this, 1, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onDate s(TimePicker view, int hourOfDay, int minute) {
//
//                    }
//                })
//            }
//        });


    }

    public void deleteTask(View view) {
    }
}
