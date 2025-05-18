package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        // 获取分数和用时
        int score = getIntent().getIntExtra("score", 0);
        long timeUsed = getIntent().getLongExtra("time_used", 0);

        // 更新UI
        TextView scoreView = findViewById(R.id.scoreView);
        TextView timeView = findViewById(R.id.timeView);
        scoreView.setText("得分: " + score);
        timeView.setText("用时: " + timeUsed/1000 + "秒");
    }
}