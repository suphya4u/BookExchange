package in.co.gamedev.server.bookexchange.api.messages;

import java.io.Serializable;

public class RegisterUserRequest implements Serializable {

  private String userId;
  private String gcmRegistrationId;
  private String firstName;
  private String emailAddress;
  private String phoneNumber;
  private long userLocationLat;
  private long userLocationLong;

  public String getUserId() {
    return userId;
  }

  public RegisterUserRequest setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

  public RegisterUserRequest setGcmRegistrationId(String gcmRegistrationId) {
    this.gcmRegistrationId = gcmRegistrationId;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public RegisterUserRequest setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public RegisterUserRequest setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  public long getUserLocationLat() {
    return userLocationLat;
  }

  public RegisterUserRequest setUserLocationLat(long userLocationLat) {
    this.userLocationLat = userLocationLat;
    return this;
  }

  public long getUserLocationLong() {
    return userLocationLong;
  }

  public RegisterUserRequest setUserLocationLong(long userLocationLong) {
    this.userLocationLong = userLocationLong;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
