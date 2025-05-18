package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavView;
    private NavController navController;
    private CountDownTimer countDownTimer;
    private long remainingTime = 120000; // 2分钟
    private long startTime;
    // 定义广播动作常量
    public static final String ACTION_GAME_WIN = "com.example.myapplication.ACTION_GAME_WIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化导航组件
        bottomNavView = findViewById(R.id.bottom_nav_view);
        navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // 初始化游戏数据
        initGameData(GameDifficulty.EASY);
        startTime = System.currentTimeMillis();
        startCountdown();
    }

    // 游戏数据初始化
    private void initGameData(GameDifficulty difficulty) {
        int animalCount = 0;
        switch (difficulty) {
            case EASY:
                animalCount = 10;
                break;
            case MEDIUM:
                animalCount = 15;
                break;
            case HARD:
                animalCount = 25;
                break;
        }
        // 随机生成动物布局
        List<AnimalCell> cells = generateRandomCells(animalCount);

        // TODO 添加随机生成动物布局的逻辑
    } // 随机生成动物布局
    private List<AnimalCell> generateRandomCells(int animalCount) {
        List<AnimalCell> cells = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                int type = new Random().nextInt(animalCount) + 1;
                cells.add(new AnimalCell(i, j, type));
            }
        }

        // 检查初始匹配
        while (hasInitialMatch(cells)) {
            cells = generateRandomCells(animalCount);
        }
        return cells;
    }

    // 初始匹配检查
    private boolean hasInitialMatch(List<AnimalCell> cells) {
        for (int i = 0; i < cells.size(); i++) {
            AnimalCell cell = cells.get(i);
            if (i % 9 != 8 && cells.get(i + 1).type == cell.type) return true;
            if (i + 9 < cells.size() && cells.get(i + 9).type == cell.type) return true;
        }
        return false;
    }

    // 启动倒计时
    private void startCountdown() {
        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                // 更新UI显示剩余时间
                // textViewTime.setText("剩余时间：" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                showGameOverDialog(false);
            }
        }.start();
    }

    // 游戏结束处理
    private void showGameOverDialog(boolean isWin) {
        if (isWin) {
            sendWinBroadcast(calculateScore(), System.currentTimeMillis() - startTime);
        } else {
            Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
            intent.putExtra("is_win", false);
            startActivity(intent);
        }
    }

    // 发送胜利广播
    private void sendWinBroadcast(int score, long timeUsed) {
        Intent intent = new Intent(ACTION_GAME_WIN);
        intent.putExtra("score", score);
        intent.putExtra("time_used", timeUsed);
        sendBroadcast(intent);
    }

    // 计算得分（示例）
    private int calculateScore() {
        return (int) (1000 - remainingTime / 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}