package in.co.gamedev.bookexchange.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.BookListRecyclerAdapter;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookSearchRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookSearchResponse;

public class SearchBookActivity extends ActionBarActivity {

  private RecyclerView searchResultView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_book);

    Button searchButton = (Button) findViewById(R.id.search_button);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleSearch(view);
      }
    });

    searchResultView = (RecyclerView) findViewById(R.id.book_search_results);
    searchResultView.setLayoutManager(new LinearLayoutManager(this));
    searchResultView.setAdapter(new BookListRecyclerAdapter(new ArrayList<BookData>()));
  }

  private void handleSearch(View view) {
    TextView searchQuery = (TextView) findViewById(R.id.search_query);
    InputMethodManager imm = (InputMethodManager)getSystemService(
        Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);

    BookSearchRequest request = new BookSearchRequest()
        .setQuery(searchQuery.getText().toString());
    searchBook(request);
  }

  private void searchBook(BookSearchRequest bookSearchRequest) {
    new AsyncTask<BookSearchRequest, Void, BookSearchResponse>() {
      @Override
      protected BookSearchResponse doInBackground(BookSearchRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().searchBook(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      protected void onPostExecute(BookSearchResponse response) {
        if (response == null || response.getBooks() == null) {
          Toast.makeText(SearchBookActivity.this,
              "Search Failed. Please try again. Maybe Network problem?", Toast.LENGTH_LONG).show();
          return;
        }
        Toast.makeText(SearchBookActivity.this, "Found '" + response.getBooks().size() + "' books",
            Toast.LENGTH_LONG).show();
        ((BookListRecyclerAdapter) searchResultView.getAdapter()).updateItems(response.getBooks());
      }
    }.execute(bookSearchRequest);
  }
}
