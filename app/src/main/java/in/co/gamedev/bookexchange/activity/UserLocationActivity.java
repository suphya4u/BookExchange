package in.co.gamedev.bookexchange.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.ServiceResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.UpdateLocationRequest;

public class UserLocationActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_location);

    Spinner location_range_spinner = (Spinner) findViewById(R.id.location_range_spinner);
    location_range_spinner.setSelection(1);

    final Button setLocationButton = (Button) findViewById(R.id.set_location_button);
    setLocationButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        handleSetLocationButtonClick(view);
      }
    });
  }

  private void handleSetLocationButtonClick(View view) {
    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
    String userId = prefs.getString(Constants.PREF_USER_ID, null);
    if (userId == null) {
      return;
    }
    // TODO(suhas): Get lat-long from map.
    UpdateLocationRequest request = new UpdateLocationRequest()
        .setUserId(userId)
        .setUserLocationLat(11L)
        .setUserLocationLong(22L);
    updateLocation(request);
  }

  private void updateLocation(UpdateLocationRequest updateLocationRequest) {
    new AsyncTask<UpdateLocationRequest, Void, ServiceResponse>() {
      @Override
      protected ServiceResponse doInBackground(UpdateLocationRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().updateLocation(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      protected void onPostExecute(ServiceResponse response) {
        if (response == null) {
          Toast.makeText(UserLocationActivity.this,
              "Location update Failed. Please try again. Maybe Network problem?",
              Toast.LENGTH_LONG).show();
          return;
        }
        SharedPreferences prefs = getSharedPreferences(
            Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(Constants.PREF_LOCATION_SAVED, response.getSuccess());
        prefEditor.apply();
        Toast.makeText(UserLocationActivity.this,
            response.getSuccess() ? R.string.msg_location_update_successful
                : R.string.msg_location_update_failed,
            Toast.LENGTH_LONG).show();

        Intent intent = new Intent(UserLocationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    }.execute(updateLocationRequest);
  }
}
