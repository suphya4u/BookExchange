package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/22/2015.
 */
public class GetBookListRequest {

  private String userId;
  private String listType;
  private int pageNumber;

  public String getListType() {
    return listType;
  }

  public GetBookListRequest setListType(String listType) {
    this.listType = listType;
    return this;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public GetBookListRequest setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public GetBookListRequest setUserId(String userId) {
    this.userId = userId;
    return this;
  }
}
