package in.co.gamedev.server.bookexchange.api.messages;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;

import in.co.gamedev.server.bookexchange.data.storage.BookData;

/**
 * Created by suhas on 2/21/2015.
 */
public class BookSearchResponse extends ServiceResponse implements Serializable {

  private ImmutableList<BookData> books;

  public ImmutableList<BookData> getBooks() {
    return books;
  }

  public BookSearchResponse setBooks(ImmutableList<BookData> books) {
    this.books = books;
    return this;
  }
}
