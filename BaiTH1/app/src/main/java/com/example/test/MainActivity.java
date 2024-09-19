package com.example.test;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.AlarmReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText alarmTime;
    private Button setAlarmButton;
    private boolean isAlarmSet = false; // Trạng thái báo thức

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmTime = findViewById(R.id.alarmTime);
        setAlarmButton = findViewById(R.id.setAlarmButton);

        setAlarmButton.setOnClickListener(v -> {
            if (!isAlarmSet) {
                setAlarm();
            } else {
                cancelAlarm();
            }
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm() {
        String time = alarmTime.getText().toString();
        String[] timeParts = time.split(":");

        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Thiết lập báo thức
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Cập nhật trạng thái và nút sau khi đặt báo thức
        isAlarmSet = true;
        setAlarmButton.setText("Đã đặt");
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent); // Hủy báo thức

        // Cập nhật trạng thái và nút sau khi hủy báo thức
        isAlarmSet = false;
        setAlarmButton.setText("Set Alarm");
    }
}
