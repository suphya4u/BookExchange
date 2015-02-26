package in.co.gamedev.server.bookexchange.data.storage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by suhas on 2/25/2015.
 */
public class UserBook {
  @Index
  private String bookId;
  private ExchangeStatus.BookStatus bookExchangeStatus;
  private int count;
  private ExchangeStatus.BookListType bookListType;

  public UserBook() {

  }

  public UserBook(String bookId, ExchangeStatus.BookListType bookListType) {
    this.setBookId(bookId)
        .setBookExchangeStatus(ExchangeStatus.BookStatus.READY_FOR_EXCHANGE)
        .setBookListType(bookListType);
  }

  public String getBookId() {
    return bookId;
  }

  public UserBook setBookId(String bookId) {
    this.bookId = bookId;
    return this;
  }

  public int getCount() {
    return count;
  }

  public UserBook setCount(int count) {
    this.count = count;
    return this;
  }

  public ExchangeStatus.BookStatus getBookExchangeStatus() {
    return bookExchangeStatus;
  }

  public UserBook setBookExchangeStatus(ExchangeStatus.BookStatus bookExchangeStatus) {
    this.bookExchangeStatus = bookExchangeStatus;
    return this;
  }

  public ExchangeStatus.BookListType getBookListType() {
    return bookListType;
  }

  public UserBook setBookListType(ExchangeStatus.BookListType bookListType) {
    this.bookListType = bookListType;
    return this;
  }
}
