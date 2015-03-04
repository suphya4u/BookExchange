package in.co.gamedev.bookexchange.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.RegisterUserRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.RegisterUserResponse;

public class SignupActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    final Button signupButton = (Button) findViewById(R.id.signup_button);
    signupButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        handleSignupButtonClick(view);
      }
    });
  }

  private void handleSignupButtonClick(View view) {
    final TextView firstName = (TextView) findViewById(R.id.first_name);
    final TextView emailAddress = (TextView) findViewById(R.id.email_address);
    final TextView phoneNumber = (TextView) findViewById(R.id.phone_number);

    if (firstName.getText().toString().equals("")
        || emailAddress.getText().toString().equals("")
        || phoneNumber.getText().toString().equals("")) {
      // TODO(suhas): Show error message.
      return;
    }

    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
    String regId = prefs.getString(Constants.PREF_REGISTRATION_ID, null);
    if (regId == null) {
      Toast.makeText(this, "Device not registered. Your app might not behave correctly",
          Toast.LENGTH_LONG).show();
      return;
    }
    RegisterUserRequest request = new RegisterUserRequest()
        .setGcmRegistrationId(regId)
        .setEmailAddress(emailAddress.getText().toString())
        .setFirstName(firstName.getText().toString());
    registerUser(request);
  }

  private void registerUser(RegisterUserRequest registerUserRequest) {
    new AsyncTask<RegisterUserRequest, Void, RegisterUserResponse>() {
      @Override
      protected RegisterUserResponse doInBackground(RegisterUserRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().registerUser(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }

      @Override
      protected void onPostExecute(RegisterUserResponse registerUserResponse) {
        SharedPreferences prefs = getSharedPreferences(
            Constants.PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(Constants.PREF_USER_ID, registerUserResponse.getUserId());
        prefEditor.apply();

        Intent intent = new Intent(SignupActivity.this, UserLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    }.execute(registerUserRequest);
  }

}
