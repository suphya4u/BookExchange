package in.co.gamedev.server.bookexchange.api.messages;

/**
 * Created by suhas on 2/21/2015.
 */
public class UpdateLocationRequest {

  private String userId;
  private long userLocationLat;
  private long userLocationLong;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public long getUserLocationLat() {
    return userLocationLat;
  }

  public void setUserLocationLat(long userLocationLat) {
    this.userLocationLat = userLocationLat;
  }

  public long getUserLocationLong() {
    return userLocationLong;
  }

  public void setUserLocationLong(long userLocationLong) {
    this.userLocationLong = userLocationLong;
  }
}
