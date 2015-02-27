package in.co.gamedev.bookexchange.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

  protected void fetchBookList(final GetBookListRequest getBookListRequest) {
    new AsyncTask<GetBookListRequest, Void, GetBookListResponse>() {

      @Override
      protected GetBookListResponse doInBackground(GetBookListRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().getBookList(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }

      @Override
      public void onPostExecute(GetBookListResponse response) {
        TextView zeroBooksHelpText = (TextView) findViewById(R.id.zero_books_help_text);
        if (response == null) {
          Toast.makeText(BookListActivity.this, "Failed to fetch books list",
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
      protected AddBookResponse doInBackground(AddBookRequest... params) {
        try {
          return BookExchangeServiceAsync.getInstance().addBookToList(params[0]);
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      public void onPostExecute(AddBookResponse response) {
        if (response == null) {
          Toast.makeText(BookListActivity.this, "Failed to add book to the list", Toast.LENGTH_LONG).show();
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
