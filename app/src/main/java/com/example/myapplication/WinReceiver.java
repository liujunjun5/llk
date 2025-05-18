package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WinReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MainActivity.ACTION_GAME_WIN)) {
            int score = intent.getIntExtra("score", 0);
            long timeUsed = intent.getLongExtra("time_used", 0);

            Intent winIntent = new Intent(context, WinActivity.class);
            winIntent.putExtra("score", score);
            winIntent.putExtra("time_used", timeUsed);
            context.startActivity(winIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}