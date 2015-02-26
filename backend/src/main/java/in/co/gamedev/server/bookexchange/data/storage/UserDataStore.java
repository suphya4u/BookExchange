package in.co.gamedev.server.bookexchange.data.storage;

import com.google.common.base.Preconditions;

import java.util.UUID;

import in.co.gamedev.server.bookexchange.api.messages.RegisterUserRequest;

import static in.co.gamedev.server.bookexchange.OfyService.ofy;

/**
 * Created by suhas on 2/23/2015.
 */
public class UserDataStore {

  public UserData getUserData(String userId) {
    return ofy().load().type(UserData.class).id(userId).now();
  }

  public UserData createNewUserData(RegisterUserRequest registerUserRequest) {
    String userId = registerUserRequest.getUserId();
    if (userId != null) {
      Preconditions.checkArgument(getUserData(registerUserRequest.getUserId()) == null,
          "User already exists with given userId.");
    } else {
      userId = generateNewUserId();
    }

    UserData userData = new UserData();
    if (userId != null) {
      userData.setUserId(userId);
    }
    userData.setFirstName(registerUserRequest.getFirstName())
        .setGcmRegistrationId(registerUserRequest.getGcmRegistrationId())
        .setUserLocationLat(registerUserRequest.getUserLocationLat())
        .setUserLocationLong(registerUserRequest.getUserLocationLong())
        .setEmailAddress(registerUserRequest.getEmailAddress())
        .setPhoneNumber(registerUserRequest.getPhoneNumber());

    ofy().save().entity(userData).now();
    return userData;
  }

  public void updateUserData(UserData userData) {
    Preconditions.checkNotNull(userData, "userData");
    Preconditions.checkNotNull(userData.getUserId(), "userId");

    ofy().save().entity(userData).now();
  }

  private String generateNewUserId() {
    return UUID.randomUUID().toString();
  }
}
