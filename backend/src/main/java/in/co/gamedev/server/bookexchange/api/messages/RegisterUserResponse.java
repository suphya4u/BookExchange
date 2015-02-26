package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by SonaliPhand on 2/20/2015.
 */
public class RegisterUserResponse extends ServiceResponse {

  private String userId;

  public String getUserId() {
    return userId;
  }

  public RegisterUserResponse setUserId(String userId) {
    this.userId = userId;
    return this;
  }
}
