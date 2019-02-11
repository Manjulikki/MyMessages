package manjunath.com.mymessages.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import manjunath.com.mymessages.R;
import manjunath.com.mymessages.activity.MainActivity;

public class SmsReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_NUM = 0;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.i("New SMS Received", bundle.toString());
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
                String message = sms.getMessageBody();
                while (message.contains("FLAG")) {
                    message = message.replace("FLAG", "");
                }
                String messageFrom = sms.getOriginatingAddress();
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.putExtra(context.getResources().getString(R.string.throughNotification),true);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.notification_image)
                                .setColor(0xff080808)
                                .setContentTitle(messageFrom)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{0, 1000, 50, 2000})
                                .setWhen(System.currentTimeMillis());
                PendingIntent contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);
                mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_NUM, builder.build());
            }
        }
    }
}
