package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/22/2015.
 */
public class AddBookRequest {

  private String userId;
  private String listType;
  private String bookId;

  public String getListType() {
    return listType;
  }

  public AddBookRequest setListType(String listType) {
    this.listType = listType;
    return this;
  }

  public String getBookId() {
    return bookId;
  }

  public AddBookRequest setBookId(String bookId) {
    this.bookId = bookId;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public AddBookRequest setUserId(String userId) {
    this.userId = userId;
    return this;
  }
}
