package in.co.gamedev.bookexchange.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.bookexchange.common.BookListRecyclerAdapter;
import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.bookexchange.common.SerializableBook;
import in.co.gamedev.bookexchange.common.Storage;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.GetBookListRequest;

public class NeededBooksActivity extends BookListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_needed_books);

    Button addBookButton = (Button) findViewById(R.id.add_expected_book_button);
    addBookButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(NeededBooksActivity.this, SearchBookActivity.class);
        startActivityForResult(intent, 0);
      }
    });
    bookListView = (RecyclerView) findViewById(R.id.expected_books_list);
    bookListView.setLayoutManager(new LinearLayoutManager(this));
    bookListView.setAdapter(new BookListRecyclerAdapter(new ArrayList()));

    SharedPreferences prefs = getSharedPreferences(
        Constants.PREFERENCES_FILE, MODE_PRIVATE);
    String userId = prefs.getString(Constants.PREF_USER_ID, null);
    if (userId != null) {
      GetBookListRequest request = new GetBookListRequest()
          .setUserId(userId)
          .setListType(Constants.BOOK_LIST_TYPE_EXPECTED_BOOKS);
      fetchBookList(request);
    }
  }

  @Override
  protected String getListType() {
    return Constants.BOOK_LIST_TYPE_EXPECTED_BOOKS;
  }
}
