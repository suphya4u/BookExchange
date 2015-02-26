package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/21/2015.
 */
public class BookSearchRequest {

  private String query;
  private int pageNumber;

  public String getQuery() {
    return query;
  }

  public BookSearchRequest setQuery(String query) {
    this.query = query;
    return this;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public BookSearchRequest setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }
}
