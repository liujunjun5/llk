package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lj
 * 项目的主模块 游戏的主逻辑
 */
public class MainActivity extends AppCompatActivity {
    //底部导航视图
    private BottomNavigationView bottomNavView;
    //导航控制器
    private NavController navController;
    //计时器
    private CountDownTimer countDownTimer;
    //剩余时间定义
    private long remainingTime = 120000;
    //开始时间
    private long startTime;
    // 定义广播动作 发送游戏胜利
    public static final String ACTION_GAME_WIN = "com.example.myapplication.ACTION_GAME_WIN";

    /**
     * 页面创建时初始化
     * @param savedInstanceState 过去保存的状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化导航组件
        bottomNavView = findViewById(R.id.bottom_nav_view);
        navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        // 初始化游戏数据 默认暂时设置为简单
        initGameData(GameDifficulty.EASY);
        startTime = System.currentTimeMillis();
        startCountdown();
    }

    /**
     * 游戏数据的初始化
     * @param difficulty 难度枚举
     */
    private void initGameData(GameDifficulty difficulty) {
        int animalCount = 0;
        switch (difficulty) {
            case MEDIUM:
                animalCount = 15;
                break;
            case HARD:
                animalCount = 25;
                break;
            default:
                animalCount = 10;
                break;
        }
        // 随机生成动物布局
        List<AnimalCell> cells = generateRandomCells(animalCount);

        // 添加随机生成动物布局的逻辑
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        AnimalCellAdapter adapter = new AnimalCellAdapter(cells);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 9));
    }

    /**
     * 随机生成布局
     * @param animalCount 动物数量
     * @return 动物单元格信息的列表
     */
    private List<AnimalCell> generateRandomCells(int animalCount) {
        List<AnimalCell> cells = new ArrayList<>();
        // 8*9布局
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                int type = new Random().nextInt(animalCount) + 1;
                cells.add(new AnimalCell(i, j, type));
            }
        }

        // 检查初始匹配（起始阶段相邻单元格具有相同的类型）
        while (hasInitialMatch(cells)) {
            cells = generateRandomCells(animalCount);
        }
        return cells;
    }

    /**
     * @param cells 动物单元格信息
     * @return 有无初始匹配
     */
    private boolean hasInitialMatch(List<AnimalCell> cells) {
        // 一维集合映射二维布局
        for (int i = 0; i < cells.size(); i++) {
            AnimalCell cell = cells.get(i);
            // 一行8个 共9行
            if (i % 9 != 8 && cells.get(i + 1).type == cell.type) {
                return true;
            }
            if (i + 9 < cells.size() && cells.get(i + 9).type == cell.type) {
                return true;
            }
        }
        return false;
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                // 更新UI显示剩余时间
//                 textViewTime.setText("剩余时间：" + millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                // 游戏结束 失败
                showGameOverDialog(false);
            }
        }.start();
    }

    /**
     * 游戏结束
     * @param isWin 胜利与否
     */
    private void showGameOverDialog(boolean isWin) {
        if (isWin) {
            sendWinBroadcast(calculateScore(), System.currentTimeMillis() - startTime);
        } else {
            Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
            intent.putExtra("is_win", false);
            startActivity(intent);
        }
    }

    /**
     * 胜利广播
     * @param score
     * @param timeUsed
     */
    private void sendWinBroadcast(int score, long timeUsed) {
        Intent intent = new Intent(ACTION_GAME_WIN);
        intent.putExtra("score", score);
        intent.putExtra("time_used", timeUsed);
        sendBroadcast(intent);
    }

    /**
     * 分数计算
     * @return 分数
     */
    private int calculateScore() {
        return (int) (1000 - remainingTime / 100);
    }

    /**
     * 页面销毁 计算器关闭
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}