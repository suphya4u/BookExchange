package in.co.gamedev.bookexchange.apiclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import in.co.gamedev.bookexchange.common.Constants;

public class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
  private GoogleCloudMessaging gcm;
  private Context context;

  public GcmRegistrationAsyncTask(Context context) {
    this.context = context;
  }

  @Override
  protected String doInBackground(Void... params) {
    try {
      if (gcm == null) {
        gcm = GoogleCloudMessaging.getInstance(context);
      }
      return gcm.register(Constants.SENDER_ID);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  protected void onPostExecute(String regId) {
    if (regId == null) {
      showError("Error registering your device. Network problem?");
      return;
    }
    SharedPreferences prefs = context.getSharedPreferences(
        Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor prefEditor = prefs.edit();
    prefEditor.putString(Constants.PREF_REGISTRATION_ID, regId);
    prefEditor.apply();
  }

  private void showError(String errorMessage) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
  }
}