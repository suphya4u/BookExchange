package in.co.gamedev.server.bookexchange.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import in.co.gamedev.server.bookexchange.Constants;
import in.co.gamedev.server.bookexchange.api.messages.AddBookRequest;
import in.co.gamedev.server.bookexchange.api.messages.AddBookResponse;
import in.co.gamedev.server.bookexchange.api.messages.BookSearchRequest;
import in.co.gamedev.server.bookexchange.api.messages.BookSearchResponse;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsRequest;
import in.co.gamedev.server.bookexchange.api.messages.FetchExchangeDetailsResponse;
import in.co.gamedev.server.bookexchange.api.messages.GetBookListRequest;
import in.co.gamedev.server.bookexchange.api.messages.GetBookListResponse;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserResponse;
import in.co.gamedev.server.bookexchange.api.messages.RegisterUserRequest;
import in.co.gamedev.server.bookexchange.api.messages.ServiceResponse;
import in.co.gamedev.server.bookexchange.api.messages.UpdateLocationRequest;
import in.co.gamedev.server.bookexchange.bookapis.goodreads.GoodReadsApi;
import in.co.gamedev.server.bookexchange.data.storage.BookData;
import in.co.gamedev.server.bookexchange.data.storage.BookDataStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycle;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeCycleStore;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus;
import in.co.gamedev.server.bookexchange.data.storage.UserBook;
import in.co.gamedev.server.bookexchange.data.storage.UserData;
import in.co.gamedev.server.bookexchange.data.storage.UserDataStore;

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
      boolean othersApproved = true;
      FetchExchangeDetailsResponse.Exchange exchange = new FetchExchangeDetailsResponse.Exchange();
      for (ExchangeCycle.UserBookInvolved userBookInvolved : exchangeCycle.getUserBooksInvolved()) {
        if (userBookInvolved.getUserId().equalsIgnoreCase(fetchExchangeDetailsRequest.getUserId())) {
          BookData pickupBook = bookDataStore.getBookData(userBookInvolved.getPickupBookId());
          BookData dropBook = bookDataStore.getBookData(userBookInvolved.getDropBookId());
          exchange.setPickupBook(pickupBook);
          exchange.setDropBook(dropBook);
          exchange.setMyApprovalStatus(userBookInvolved.getApprovalStatus());
        } else {
          othersApproved = othersApproved
              && userBookInvolved.getApprovalStatus().equals(ExchangeStatus.ApprovalStatus.APPROVED);
        }
      }
      exchange.setOtherUserApprovalStatus(othersApproved ? ExchangeStatus.ApprovalStatus.APPROVED
          : ExchangeStatus.ApprovalStatus.WAITING);
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
}
