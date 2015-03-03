package in.co.gamedev.bookexchange.apiclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.activity.ExchangeMatchesActivity;
import in.co.gamedev.bookexchange.common.Constants;

public class GcmIntentService extends IntentService {

  public GcmIntentService() {
    super("GcmIntentService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Bundle extras = intent.getExtras();
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
    // The getMessageType() intent parameter must be the intent you received
    // in your BroadcastReceiver.
    String messageType = gcm.getMessageType(intent);

    if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
      // Since we're not using two way messaging, this is all we really to check for
      if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
        Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

        String notificationType = extras.getString(Constants.NOTIFICATION_TYPE);
        if (!notificationType.equals(Constants.NOTIFICATION_TYPE_UPDATE_ON_EXCHANGE)) {
          return;
        }
        String title = extras.getString(Constants.NOTIFICATION_KEY_TITLE);
        String text = extras.getString(Constants.NOTIFICATION_KEY_TEXT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_stat_exchange_arrows)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true);

        Intent exchangeActivity = new Intent(this, ExchangeMatchesActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ExchangeMatchesActivity.class);
        stackBuilder.addNextIntent(exchangeActivity);
        PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
      }
    }
    GcmBroadcastReceiver.completeWakefulIntent(intent);
  }
}