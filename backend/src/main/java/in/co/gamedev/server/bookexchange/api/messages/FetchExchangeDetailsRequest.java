package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/21/2015.
 */
public class FetchExchangeDetailsRequest {

  private String userId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
