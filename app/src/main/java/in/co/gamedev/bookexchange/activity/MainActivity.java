package in.co.gamedev.bookexchange.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.GcmRegistrationAsyncTask;
import in.co.gamedev.bookexchange.common.Constants;


public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new GcmRegistrationAsyncTask(this).execute();
    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);

    if (prefs.getString(Constants.PREF_USER_ID, null) == null) {
      // Not signed up yet. Go to Signup activity.
      gotoActivity(SignupActivity.class, true /* clearBackNavigation */);
      return;
    }
    if (!prefs.getBoolean(Constants.PREF_LOCATION_SAVED, false)) {
      // Location for book exchange is not yet set up.
      // TODO(suhas): Maybe do not go to activity and set default location as current location.
      gotoActivity(UserLocationActivity.class, true /* clearBackNavigation */);
      return;
    }
    setContentView(R.layout.activity_main);

    ViewGroup matchedExchanges = (ViewGroup) findViewById(R.id.matched_exchanges);
    matchedExchanges.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gotoActivity(ExchangeMatchesActivity.class, false);
      }
    });

    ViewGroup myBooks = (ViewGroup) findViewById(R.id.my_books);
    myBooks.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gotoActivity(MyBooksActivity.class, false);
      }
    });

    ViewGroup expectedBooks = (ViewGroup) findViewById(R.id.expected_books);
    expectedBooks.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gotoActivity(NeededBooksActivity.class, false);
      }
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_clear_prefs) {
      SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
      SharedPreferences.Editor prefEditor = prefs.edit();
      prefEditor.clear();
      prefEditor.apply();
      return true;
    }
    if (id == R.id.action_set_location) {
      gotoActivity(UserLocationActivity.class, false /* clearBackNavigation */);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void gotoActivity(Class activity, boolean clearBackNavigation) {
    Intent intent = new Intent(this, activity);
    if (clearBackNavigation) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
    startActivity(intent);
  }

  public static class NavigationPanelFragment extends Fragment {
    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_main_navigation_panel, container, false);
    }
  }
}
