package in.co.gamedev.bookexchange.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.bookexchange.common.ExchangeListRecyclerAdapter;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.FetchExchangeDetailsRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.FetchExchangeDetailsResponse;

public class ExchangeMatchesActivity extends ActionBarActivity {

  private RecyclerView exchangeListView;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exchange_matches);

    exchangeListView = (RecyclerView) findViewById(R.id.exchange_list);
    exchangeListView.setLayoutManager(new LinearLayoutManager(this));
    exchangeListView.setAdapter(new ExchangeListRecyclerAdapter(new ArrayList()));

    SharedPreferences prefs = getSharedPreferences(
        Constants.PREFERENCES_FILE, MODE_PRIVATE);
    String userId = prefs.getString(Constants.PREF_USER_ID, null);
    if (userId != null) {
      FetchExchangeDetailsRequest request = new FetchExchangeDetailsRequest()
          .setUserId(userId);
      fetchExchanges(request);
    }
  }

  private void fetchExchanges(FetchExchangeDetailsRequest request) {
    new AsyncTask<FetchExchangeDetailsRequest, Void, FetchExchangeDetailsResponse>() {

      @Override
      protected void onPreExecute() {
        progressDialog = new ProgressDialog(ExchangeMatchesActivity.this, R.style.progress_dialog);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
      }

      @Override
      protected FetchExchangeDetailsResponse doInBackground(FetchExchangeDetailsRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().fetchExchangeDetails(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public void onPostExecute(FetchExchangeDetailsResponse response) {
        progressDialog.dismiss();
        TextView zeroBooksHelpText = (TextView) findViewById(R.id.zero_match_help_text);
        if (response == null) {
          Toast.makeText(ExchangeMatchesActivity.this,
              "Failed to fetch books list. Please try again. Maybe Network problem?",
              Toast.LENGTH_LONG).show();
        } else if (response.getExchanges() == null || response.getExchanges().size() == 0) {
          zeroBooksHelpText.setVisibility(View.VISIBLE);
          exchangeListView.setVisibility(View.INVISIBLE);
        } else {
          zeroBooksHelpText.setVisibility(View.GONE);
          exchangeListView.setVisibility(View.VISIBLE);
          ((ExchangeListRecyclerAdapter) exchangeListView.getAdapter()).addItems(response.getExchanges());
        }
      }
    }.execute(request);
  }
}
