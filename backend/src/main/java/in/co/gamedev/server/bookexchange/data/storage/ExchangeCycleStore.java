package in.co.gamedev.server.bookexchange.data.storage;

import com.google.apphosting.utils.remoteapi.RemoteApiPb;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.VoidWork;

import static in.co.gamedev.server.bookexchange.OfyService.ofy;

/**
 * Created by suhas on 2/26/2015.
 */
public class ExchangeCycleStore {

  private final UserDataStore userDataStore;

  public ExchangeCycleStore() {
    userDataStore = new UserDataStore();
  }
  public ExchangeCycle getExchangeCycle(String exchangeCycleId) {
    return ofy().load().type(ExchangeCycle.class).id(exchangeCycleId).now();
  }

  public void storeExchangeCycle(final ExchangeCycle exchangeCycle) {
    Preconditions.checkNotNull(exchangeCycle.getExchangeCycleId());
    Preconditions.checkNotNull(exchangeCycle.getUserBooksInvolved());

    ofy().transact(new VoidWork() {
      @Override
      public void vrun() {
        Preconditions.checkArgument(getExchangeCycle(exchangeCycle.getExchangeCycleId()) == null,
            "Existing cycle.");
        for (ExchangeCycle.UserBookInvolved userBookInvolved : exchangeCycle.getUserBooksInvolved()) {
          Preconditions.checkNotNull(userBookInvolved.getUserId());
          Preconditions.checkNotNull(userBookInvolved.getDropBookId());
          Preconditions.checkNotNull(userBookInvolved.getPickupBookId());

          UserData userData = userDataStore.getUserData(userBookInvolved.getUserId());
          Preconditions.checkNotNull(userData);
          userData.addExchangeCycle(exchangeCycle.getExchangeCycleId(),
              userBookInvolved.getPickupBookId(), userBookInvolved.getDropBookId());
          userDataStore.updateUserData(userData);
        }
        ofy().save().entity(exchangeCycle).now();
      }
    });

  }
}
