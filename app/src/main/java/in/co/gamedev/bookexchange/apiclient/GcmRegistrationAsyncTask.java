package in.co.gamedev.bookexchange.apiclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
      String regId = gcm.register(Constants.SENDER_ID);
      return regId;
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }

  @Override
  protected void onPostExecute(String regId) {
    SharedPreferences prefs = context.getSharedPreferences(
        Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor prefEditor = prefs.edit();
    prefEditor.putString(Constants.PREF_REGISTRATION_ID, regId);
    prefEditor.apply();
  }
}