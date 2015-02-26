package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/21/2015.
 */
public class ServiceResponse {

  private boolean isSuccess;

  public boolean isSuccess() {
    return isSuccess;
  }

  public ServiceResponse setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
    return this;
  }
}
