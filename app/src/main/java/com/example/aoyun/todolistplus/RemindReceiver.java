package com.example.aoyun.todolistplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class RemindReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        ToastUtils.showMessage(context,"收到消息");
        Bundle bundle = intent.getExtras();
        intent = new Intent(context, TodoActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
