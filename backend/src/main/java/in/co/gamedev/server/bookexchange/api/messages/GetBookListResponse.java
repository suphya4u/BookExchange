package in.co.gamedev.server.bookexchange.api.messages;

import com.google.common.collect.ImmutableList;

import in.co.gamedev.server.bookexchange.data.storage.BookData;

/**
 * Created by suhas on 2/22/2015.
 */
public class GetBookListResponse extends ServiceResponse {

  private ImmutableList<BookData> books;

  public ImmutableList<BookData> getBooks() {
    return books;
  }

  public GetBookListResponse setBooks(ImmutableList<BookData> books) {
    this.books = books;
    return this;
  }
}
