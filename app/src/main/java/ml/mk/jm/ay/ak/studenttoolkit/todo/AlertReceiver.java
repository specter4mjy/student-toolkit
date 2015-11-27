package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.database.DatabaseConnection;

/**This class is used to receive alarm from the set alarm by users.*/
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int todoId = intent.getIntExtra("TODO_ID", -1);
        String todoTitle =  intent.getStringExtra("TODO_TITLE");
        String todoDesc =  intent.getStringExtra("TODO_DESCRIPTION");
        Log.d("todo", "todo Id: " + todoId + " todoTitle: " + todoTitle + " todoDes" + todoDesc);

        
        createNotification(context, todoTitle, todoDesc, "Todo Alert", todoId);
    }

    private void createNotification(Context context, String msg, String msgText, String msgAlert, int todoId) {
        PendingIntent notifyIntent = PendingIntent.getActivity(context, 0, new Intent(context, NewToDoActivity.class), 0);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.add)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert);
        nBuilder.setContentIntent(notifyIntent);
        nBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        nBuilder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(todoId, nBuilder.build());


    }
}