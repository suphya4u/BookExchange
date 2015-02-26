package in.co.gamedev.bookexchange.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.bookexchange.apiclient.BookExchangeServiceAsync;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;

/**
 * Created by suhas on 2/22/2015.
 */
public class BookListRecyclerAdapter
    extends RecyclerView.Adapter<BookListRecyclerAdapter.BookItemViewHolder> {

  private final List<BookData> bookList;

  public BookListRecyclerAdapter(List<BookData> bookList) {
    this.bookList = bookList;
  }

  public void addItem(BookData book) {
    bookList.add(book);
    notifyDataSetChanged();
  }

  public void addItems(List<BookData> books) {
    bookList.addAll(books);
    notifyDataSetChanged();
  }

  @Override
  public BookItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.book_list_item, viewGroup, false);
    final BookItemViewHolder viewHolder = new BookItemViewHolder(view);
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        viewHolder.onClick(v);
      }
    });
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(BookItemViewHolder viewHolder, int i) {
    BookData book = bookList.get(i);
    new ImageLoadTask(book.getThumbnailUrl(), viewHolder.thumbnail).execute();
    viewHolder.title.setText(book.getTitle());
    viewHolder.author.setText(book.getAuthor());
    viewHolder.bookId = book.getBookId();
    viewHolder.book = new SerializableBook(book);
  }

  @Override
  public int getItemCount() {
    return bookList.size();
  }

  public class BookItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public TextView title;
    public TextView author;
    public String bookId;
    public SerializableBook book;

    public BookItemViewHolder(View itemView) {
      super(itemView);
      thumbnail = (ImageView) itemView.findViewById(R.id.book_item_thumbnail);
      title = (TextView) itemView.findViewById(R.id.book_item_title);
      author = (TextView) itemView.findViewById(R.id.book_item_author);
    }

    public void onClick(View view) {
      addBookToList();
    }

    private void addBookToList() {
      Activity activity = ((Activity) itemView.getContext());
      Intent resultData = new Intent();
      resultData.putExtra(Constants.INTENT_EXTRA_SELECTED_BOOK, book);
      activity.setResult(1, resultData);
      activity.finish();
    }
  }

  public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
      this.url = url;
      this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
      try {
        URL urlConnection = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlConnection
            .openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
      super.onPostExecute(result);
      imageView.setImageBitmap(result);
    }
  }
}
