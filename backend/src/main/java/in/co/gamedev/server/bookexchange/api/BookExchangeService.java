package in.co.gamedev.server.bookexchange.api;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import in.co.gamedev.server.bookexchange.Constants;
import in.co.gamedev.server.bookexchange.api.messages.AddBookRequest;
import in.co.gamedev.server.bookexchange.api.messages.AddBookResponse;
import in.co.gamedev.server.bookexchange.api.messages.BookSearchRequest;
import in.co.gamedev.server.bookexchange.api.messages.BookSearchResponse;
import in.co.gamedev.server.bookexchange.api.messages.ChangeExchangeApprovalRequest;
import in.co.gamedev.server.bookexchange.api.messages.ChangeExchangeApprovalResponse;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsRequest;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsResponse;
import in.co.gamedev.server.bookexchange.api.messages.GetBookListRequest;
import in.co.gamedev.server.bookexchange.api.messages.GetBookListResponse;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserRequest;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserResponse;
import in.co.gamedev.server.bookexchange.api.messages.ServiceResponse;
import in.co.gamedev.server.bookexchange.api.messages.UpdateLocationRequest;
import in.co.gamedev.server.bookexchange.bookapis.goodreads.GoodReadsApi;
import in.co.gamedev.server.bookexchange.data.storage.BookData;
import in.co.gamedev.server.bookexchange.data.storage.BookDataStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycle;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycleStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus.ApprovalStatus;
import in.co.gamedev.server.bookexchange.data.storage.UserBook;
import in.co.gamedev.server.bookexchange.data.storage.UserData;
import in.co.gamedev.server.bookexchange.data.storage.UserDataStore;

import static in.co.gamedev.server.bookexchange.OfyService.ofy;

/**
 * Created by suhas on 2/20/2015.
 */
@Api(name = "bookExchangeService",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "bookexchange.server.gamedev.co.in",
        ownerName = "bookexchange.server.gamedev.co.in",
        packagePath = ""))
public class BookExchangeService {

  private final Logger logger = Logger.getLogger(BookExchangeService.class.getName());
  private final UserDataStore userDataStore;
  private final BookDataStore bookDataStore;
  private final ExchangeCycleStore exchangeCycleStore;

  public BookExchangeService() {
    userDataStore = new UserDataStore();
    bookDataStore = new BookDataStore();
    exchangeCycleStore = new ExchangeCycleStore();
  }

  @ApiMethod(name = "registerUser")
  public RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest) {
    UserData userData = userDataStore.createNewUserData(registerUserRequest);
    return new RegisterUserResponse().setUserId(userData.getUserId());
  }

  @ApiMethod(name = "updateLocation")
  public ServiceResponse updateLocation(UpdateLocationRequest updateLocationRequest) {
    UserData userData = userDataStore.getUserData(updateLocationRequest.getUserId());
    userData.setUserLocationLat(updateLocationRequest.getUserLocationLat())
        .setUserLocationLong(updateLocationRequest.getUserLocationLong());
    userDataStore.updateUserData(userData);
    return new ServiceResponse().setSuccess(true);
  }

  @ApiMethod(name = "fetchExchangeDetails")
  public FetchExchangeDetailsResponse fetchExchangeDetails(
      FetchExchangeDetailsRequest fetchExchangeDetailsRequest) {

    UserData userData = userDataStore.getUserData(fetchExchangeDetailsRequest.getUserId());
    Set<UserData.ExchangeData> exchanges = userData.getExchangeData();
    List<FetchExchangeDetailsResponse.Exchange> exchangeList = new ArrayList();
    for (UserData.ExchangeData exchangeData : exchanges) {
      ExchangeCycle exchangeCycle = exchangeCycleStore.getExchangeCycle(exchangeData.getExchangeId());
      ExchangeStatus.ApprovalStatus othersApprovalStatus = ExchangeStatus.ApprovalStatus.APPROVED;
      FetchExchangeDetailsResponse.Exchange exchange = new FetchExchangeDetailsResponse.Exchange();
      for (ExchangeCycle.UserBookInvolved userBookInvolved : exchangeCycle.getUserBooksInvolved()) {
        if (userBookInvolved.getUserId().equals(fetchExchangeDetailsRequest.getUserId())) {
          BookData pickupBook = bookDataStore.getBookData(userBookInvolved.getPickupBookId());
          BookData dropBook = bookDataStore.getBookData(userBookInvolved.getDropBookId());
          exchange.setPickupBook(pickupBook);
          exchange.setDropBook(dropBook);
          exchange.setMyApprovalStatus(userBookInvolved.getApprovalStatus());
        } else {
          if (othersApprovalStatus.equals(ExchangeStatus.ApprovalStatus.APPROVED)) {
            othersApprovalStatus = userBookInvolved.getApprovalStatus();
          } else if (othersApprovalStatus.equals(ExchangeStatus.ApprovalStatus.WAITING)
              && userBookInvolved.getApprovalStatus().equals(
                  ExchangeStatus.ApprovalStatus.CANCELLED)) {
            othersApprovalStatus = ExchangeStatus.ApprovalStatus.CANCELLED;
          }
        }
      }
      exchange.setOtherUserApprovalStatus(othersApprovalStatus);
      exchange.setExchangeCycleId(exchangeCycle.getExchangeCycleId());
      exchangeList.add(exchange);
    }
    return new FetchExchangeDetailsResponse().setExchanges(exchangeList);
  }

  @ApiMethod(name = "searchBook")
  public BookSearchResponse searchBook(BookSearchRequest bookSearchRequest) {
    List<BookData> result = new GoodReadsApi().search(bookSearchRequest.getQuery());
    // TODO(suhas): Hack to remember book data. GoodsReads currently do not allow fetching book
    // data. It needs extra permissions. Figure out better free APIs and replace this hacky way.
    for (BookData book : result) {
      bookDataStore.createOrUpdateBookData(book);
    }
    return new BookSearchResponse().setBooks(ImmutableList.copyOf(result));
  }

  @ApiMethod(name = "fetchBookList")
  public GetBookListResponse fetchBookList(GetBookListRequest getBookListRequest) {
    Set<UserBook> userBooks = null;
    UserData userData = userDataStore.getUserData(getBookListRequest.getUserId());
    if (getBookListRequest.getListType().equalsIgnoreCase(Constants.BOOK_LIST_TYPE_MY_BOOKS)) {
      userBooks = userData.getOwnedBooks();
    } else if (getBookListRequest.getListType().equalsIgnoreCase(
        Constants.BOOK_LIST_TYPE_EXPECTED_BOOKS)) {
      userBooks = userData.getExpectedBooks();
    }
    List<BookData> books = new ArrayList();
    for (UserBook userBook : userBooks) {
      BookData book = bookDataStore.getBookData(userBook.getBookId());
      if (book != null && book.getTitle() != null) {
        books.add(book);
      }
    }
    return new GetBookListResponse().setBooks(ImmutableList.copyOf(books));
  }

  @ApiMethod(name = "addBookToList")
  public AddBookResponse addBookToList(AddBookRequest addBookRequest) {
    UserData userData = userDataStore.getUserData(addBookRequest.getUserId());
    if (addBookRequest.getListType().equalsIgnoreCase(Constants.BOOK_LIST_TYPE_MY_BOOKS)) {
      userData.addOwnedBook(addBookRequest.getBookId());
    } else if (addBookRequest.getListType().equalsIgnoreCase(Constants.BOOK_LIST_TYPE_EXPECTED_BOOKS)) {
      userData.addExpectedBook(addBookRequest.getBookId());
    }
    userDataStore.updateUserData(userData);
    return new AddBookResponse();
  }

  @ApiMethod(name = "addExchangeCycle")
  public void addExchangeCycle(ExchangeCycle exchangeCycle) throws IOException {
    exchangeCycleStore.storeExchangeCycle(exchangeCycle);
    // TODO(suhas): This should not be here. Whoever finds exchanges should decide whether to notify user.
    for (ExchangeCycle.UserBookInvolved userBookInvolved : exchangeCycle.getUserBooksInvolved()) {
      UserData user = userDataStore.getUserData(userBookInvolved.getUserId());
      sendMessage(user, "Book Exchange found a new exchange", "There is a new exchange available for your books.");
    }
  }

  @ApiMethod(name = "changeExchangeApproval")
  public ChangeExchangeApprovalResponse changeExchangeApproval(
      ChangeExchangeApprovalRequest changeExchangeApprovalRequest) throws IOException {
    exchangeCycleStore.updateExchangeApproval(
        changeExchangeApprovalRequest.getExchangeCycleId(),
        changeExchangeApprovalRequest.getUserId(),
        changeExchangeApprovalRequest.getNewApprovalStatus()
    );
    ChangeExchangeApprovalResponse response = new ChangeExchangeApprovalResponse();
    response.setSuccess(true);

    ExchangeCycle exchangeCycle = exchangeCycleStore.getExchangeCycle(
        changeExchangeApprovalRequest.getExchangeCycleId());

    String approvalMessageTitle;
    String approvalMessageText;

    if (changeExchangeApprovalRequest.getNewApprovalStatus().equals(ApprovalStatus.APPROVED)) {
      approvalMessageTitle = "One of your exchange is now Approved";
      approvalMessageText = "Congratulations!! One of your exchange is now approved.";
    } else if (changeExchangeApprovalRequest.getNewApprovalStatus().equals(ApprovalStatus.CANCELLED)) {
      approvalMessageTitle = "One of your exchange is now Cancelled";
      approvalMessageText = "We hate it when this happens. One of your exchange is now cancelled.";
    } else {
      return response;
    }
    for (ExchangeCycle.UserBookInvolved userBookInvolved : exchangeCycle.getUserBooksInvolved()) {
      if (!userBookInvolved.equals(changeExchangeApprovalRequest.getUserId())) {
        // Notify other users that one user approved.
        UserData user = userDataStore.getUserData(userBookInvolved.getUserId());
        sendMessage(user, approvalMessageTitle, approvalMessageText);
      }
    }
    return response;
  }

  @ApiMethod(name = "fakeUserAndExchange")
  public void fakeUserAndExchange(@Named("userId") String userId) throws IOException {
    UserData existingUser = userDataStore.getUserData(userId);
    UserData newUser = userDataStore.getUserData(registerUser(new RegisterUserRequest()
        .setEmailAddress("fake@user.com")).getUserId());
    String ownedBook = existingUser.getOwnedBooks().asList().get(0).getBookId();
    String expectedBook = existingUser.getExpectedBooks().asList().get(0).getBookId();
    addBookToList(new AddBookRequest()
        .setUserId(newUser.getUserId())
        .setBookId(expectedBook)
        .setListType(Constants.BOOK_LIST_TYPE_MY_BOOKS));

    addExchangeCycle(new ExchangeCycle()
        .setExchangeCycleId("fake-exchange")
        .addUserBooksInvolved(new ExchangeCycle.UserBookInvolved()
            .setUserId(existingUser.getUserId())
            .setPickupBookId(ownedBook)
            .setDropBookId(expectedBook))
        .addUserBooksInvolved(new ExchangeCycle.UserBookInvolved()
            .setUserId(newUser.getUserId())
            .setPickupBookId(expectedBook)
            .setDropBookId(ownedBook)));
  }

  private void sendMessage(UserData userData, String title, String text) throws IOException {
    if (userData.getGcmRegistrationId() == null) {
      return;
    }
    Sender sender = new Sender(Constants.API_KEY);
    Message msg = new Message.Builder()
        .addData(Constants.NOTIFICATION_TYPE, Constants.NOTIFICATION_TYPE_UPDATE_ON_EXCHANGE)
        .addData(Constants.NOTIFICATION_KEY_TITLE, title)
        .addData(Constants.NOTIFICATION_KEY_TEXT, text)
        .build();
    Result result = sender.send(msg, userData.getGcmRegistrationId(), 5);
    if (result.getMessageId() != null) {
      String canonicalRegId = result.getCanonicalRegistrationId();
      if (canonicalRegId != null) {
        userData.setGcmRegistrationId(canonicalRegId);
        userDataStore.updateUserData(userData);
      }
    }
  }
}
