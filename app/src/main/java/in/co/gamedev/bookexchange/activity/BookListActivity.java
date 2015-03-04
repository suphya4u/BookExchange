package in.co.gamedev.bookexchange.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.BookListRecyclerAdapter;
import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.bookexchange.common.SerializableBook;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.GetBookListRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.GetBookListResponse;

/**
 * Created by suhas on 2/24/2015.
 */
public abstract class BookListActivity extends ActionBarActivity {

  protected RecyclerView bookListView;
  protected ProgressDialog progressDialog;

  protected void fetchBookList(final GetBookListRequest getBookListRequest) {
    new AsyncTask<GetBookListRequest, Void, GetBookListResponse>() {

      @Override
      protected void onPreExecute() {
        progressDialog = new ProgressDialog(BookListActivity.this, R.style.progress_dialog);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
      }

      @Override
      protected GetBookListResponse doInBackground(GetBookListRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().getBookList(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public void onPostExecute(GetBookListResponse response) {
        progressDialog.dismiss();
        TextView zeroBooksHelpText = (TextView) findViewById(R.id.zero_books_help_text);
        if (response == null) {
          Toast.makeText(BookListActivity.this,
              "Failed to fetch books list. Please try again. Maybe Network problem?",
              Toast.LENGTH_LONG).show();
        } else if (response.getBooks() == null || response.getBooks().size() == 0) {
          zeroBooksHelpText.setVisibility(View.VISIBLE);
          bookListView.setVisibility(View.INVISIBLE);
        } else {
          zeroBooksHelpText.setVisibility(View.GONE);
          bookListView.setVisibility(View.VISIBLE);
          ((BookListRecyclerAdapter) bookListView.getAdapter()).addItems(response.getBooks());
        }
      }
    }.execute(getBookListRequest);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (data == null || data.getSerializableExtra(Constants.INTENT_EXTRA_SELECTED_BOOK) == null) {
      return;
    }
    SerializableBook book = (SerializableBook) data.getSerializableExtra(
        Constants.INTENT_EXTRA_SELECTED_BOOK);
    BookData bookData = book.getBookData();

    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES_FILE, MODE_PRIVATE);
    String userId = prefs.getString(Constants.PREF_USER_ID, null);
    if (userId == null) {
      return;
    }
    AddBookRequest request = new AddBookRequest()
        .setListType(getListType())
        .setBookId(bookData.getBookId())
        .setUserId(userId);
    addBookToList(request, bookData);
  }

  protected abstract String getListType();

  protected void addBookToList(final AddBookRequest addBookRequest, final BookData bookData) {
    new AsyncTask<AddBookRequest, Void, AddBookResponse>() {

      @Override
      protected void onPreExecute() {
        progressDialog = new ProgressDialog(BookListActivity.this, R.style.progress_dialog);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
      }

      @Override
      protected AddBookResponse doInBackground(AddBookRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().addBookToList(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public void onPostExecute(AddBookResponse response) {
        progressDialog.dismiss();
        if (response == null) {
          Toast.makeText(BookListActivity.this,
              "Failed to add book to the list. Please try again. Maybe Network problem?",
              Toast.LENGTH_LONG).show();
          return;
        }
        if (bookListView.getAdapter() != null) {
          BookListRecyclerAdapter adapter = ((BookListRecyclerAdapter) bookListView.getAdapter());
          adapter.addItem(bookData);

          TextView zeroBooksHelpText = (TextView) findViewById(R.id.zero_books_help_text);
          zeroBooksHelpText.setVisibility(View.GONE);
          bookListView.setVisibility(View.VISIBLE);
        }
        Toast.makeText(BookListActivity.this, "Book added successfully", Toast.LENGTH_LONG).show();
      }
    }.execute(addBookRequest);
  }
}
