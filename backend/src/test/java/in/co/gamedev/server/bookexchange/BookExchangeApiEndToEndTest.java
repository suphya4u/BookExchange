package in.co.gamedev.server.bookexchange;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import in.co.gamedev.server.bookexchange.api.BookExchangeService;
import in.co.gamedev.server.bookexchange.api.messages.ChangeExchangeApprovalRequest;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsRequest;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsResponse;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserRequest;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserResponse;
import in.co.gamedev.server.bookexchange.data.storage.BookData;
import in.co.gamedev.server.bookexchange.data.storage.BookDataStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycle;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycleStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus.ApprovalStatus;
import in.co.gamedev.server.bookexchange.data.storage.UserData;
import in.co.gamedev.server.bookexchange.data.storage.UserDataStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by suhas on 2/27/2015.
 */
public class BookExchangeApiEndToEndTest {

  private static final String USER_EMAIL = "user@example.com";
  private static final String BOOK_1_ID = "book1";
  private static final String BOOK_2_ID = "book2";
  private static final String EXCHANGE_CYCLE_ID = "new-exchange-cycle";

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
          .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

  private UserDataStore userDataStore;
  private BookDataStore bookDataStore;
  private ExchangeCycleStore exchangeCycleStore;
  private BookExchangeService bookExchangeService;

  @Before
  public void setUp() {
    helper.setUp();
    userDataStore = new UserDataStore();
    bookDataStore = new BookDataStore();
    exchangeCycleStore = new ExchangeCycleStore();
    bookExchangeService = new BookExchangeService();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testRegister() throws Exception {
    assertNull(userDataStore.getUserData("new"));

    RegisterUserResponse response = bookExchangeService.registerUser(
        new RegisterUserRequest()
            .setEmailAddress(USER_EMAIL));

    UserData newUser = userDataStore.getUserData(response.getUserId());
    assertEquals(USER_EMAIL, newUser.getEmailAddress());
  }

  @Test
  public void testExchangeLifeCycle() throws Exception {

    // Create user1 who owns book1
    bookDataStore.createOrUpdateBookData(new BookData().setBookId(BOOK_1_ID));
    UserData user1 = userDataStore.createNewUserData(new RegisterUserRequest());
    user1.addOwnedBook(BOOK_1_ID);
    userDataStore.updateUserData(user1);

    // Create user2 who owns book2
    bookDataStore.createOrUpdateBookData(new BookData().setBookId(BOOK_2_ID));
    UserData user2 = userDataStore.createNewUserData(new RegisterUserRequest());
    user2.addOwnedBook(BOOK_2_ID);
    userDataStore.updateUserData(user2);

    // Exchange cycle to exchange book1 with book2
    bookExchangeService.addExchangeCycle(new ExchangeCycle()
        .setExchangeCycleId(EXCHANGE_CYCLE_ID)
        .addUserBooksInvolved(new ExchangeCycle.UserBookInvolved()
            .setUserId(user1.getUserId())
            .setPickupBookId(BOOK_1_ID)
            .setDropBookId(BOOK_2_ID))
        .addUserBooksInvolved(new ExchangeCycle.UserBookInvolved()
            .setUserId(user2.getUserId())
            .setPickupBookId(BOOK_2_ID)
            .setDropBookId(BOOK_1_ID)));

    // Check user1 exchange data
    assertUserExchange(user1.getUserId(), ApprovalStatus.WAITING, ApprovalStatus.WAITING,
        BOOK_1_ID, BOOK_2_ID);

    // Check user2 exchange data
    assertUserExchange(user2.getUserId(), ApprovalStatus.WAITING, ApprovalStatus.WAITING,
        BOOK_2_ID, BOOK_1_ID);

    // User1 Approves
    bookExchangeService.changeExchangeApproval(new ChangeExchangeApprovalRequest()
        .setExchangeCycleId(EXCHANGE_CYCLE_ID)
        .setUserId(user1.getUserId())
        .setNewApprovalStatus(ExchangeStatus.ApprovalStatus.APPROVED));

    // Check user1 exchange data
    assertUserExchange(user1.getUserId(), ApprovalStatus.APPROVED, ApprovalStatus.WAITING,
        BOOK_1_ID, BOOK_2_ID);

    // Check user2 exchange data
    assertUserExchange(user2.getUserId(), ApprovalStatus.WAITING, ApprovalStatus.APPROVED,
        BOOK_2_ID, BOOK_1_ID);

    // User2 approves
    bookExchangeService.changeExchangeApproval(new ChangeExchangeApprovalRequest()
        .setExchangeCycleId(EXCHANGE_CYCLE_ID)
        .setUserId(user2.getUserId())
        .setNewApprovalStatus(ExchangeStatus.ApprovalStatus.APPROVED));

    // Check user1 exchange data
    assertUserExchange(user1.getUserId(), ApprovalStatus.APPROVED, ApprovalStatus.APPROVED,
        BOOK_1_ID, BOOK_2_ID);

    // Check user2 exchange data
    assertUserExchange(user2.getUserId(), ApprovalStatus.APPROVED, ApprovalStatus.APPROVED,
        BOOK_2_ID, BOOK_1_ID);

    // User2 cancels
    bookExchangeService.changeExchangeApproval(new ChangeExchangeApprovalRequest()
        .setExchangeCycleId(EXCHANGE_CYCLE_ID)
        .setUserId(user2.getUserId())
        .setNewApprovalStatus(ExchangeStatus.ApprovalStatus.CANCELLED));

    // Check user1 exchange data
    assertUserExchange(user1.getUserId(), ApprovalStatus.APPROVED, ApprovalStatus.CANCELLED,
        BOOK_1_ID, BOOK_2_ID);

    // Check user2 exchange data
    assertUserExchange(user2.getUserId(), ApprovalStatus.CANCELLED, ApprovalStatus.APPROVED,
        BOOK_2_ID, BOOK_1_ID);
  }

  private void assertUserExchange(String userId, ApprovalStatus myApprovalStatus,
      ApprovalStatus othersApprovalStatus, String pickupBookId, String dropBookId) {
    FetchExchangeDetailsResponse userExchangeResponse = bookExchangeService.fetchExchangeDetails(
        new FetchExchangeDetailsRequest().setUserId(userId));
    assertEquals(1, userExchangeResponse.getExchanges().size());
    FetchExchangeDetailsResponse.Exchange userExchange =
        userExchangeResponse.getExchanges().get(0);
    if (pickupBookId != null) {
      assertEquals(pickupBookId, userExchange.getPickupBook().getBookId());
    }
    if (dropBookId != null) {
      assertEquals(dropBookId, userExchange.getDropBook().getBookId());
    }
    assertEquals(myApprovalStatus, userExchange.getMyApprovalStatus());
    assertEquals(othersApprovalStatus, userExchange.getOtherUserApprovalStatus());
  }
}
