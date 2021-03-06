package in.co.gamedev.bookexchange.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.co.gamedev.bookexchange.R;
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

  public void updateItems(List<BookData> books) {
    bookList.clear();
    addItems(books);
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
    new ImageLoaderTask(book.getThumbnailUrl(), viewHolder.thumbnail).execute();
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
}
