package in.co.gamedev.server.bookexchange.data.storage;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by suhas on 2/23/2015.
 */
@Entity
public class UserData {

  @Id private String userId;

  private String gcmRegistrationId;
  private String firstName;
  private String emailAddress;
  private String phoneNumber;
  private long userLocationLat;
  private long userLocationLong;
  private Set<UserBook> ownedBooks;
  private Set<UserBook> expectedBooks;
  private Set<ExchangeData> exchangeData;

  public UserData() {
    ownedBooks = new HashSet();
    expectedBooks = new HashSet();
    exchangeData = new HashSet();
  }

  public String getUserId() {
    return userId;
  }

  public UserData setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

  public UserData setGcmRegistrationId(String gcmRegistrationId) {
    this.gcmRegistrationId = gcmRegistrationId;
    return this;
  }

  public long getUserLocationLat() {
    return userLocationLat;
  }

  public UserData setUserLocationLat(long userLocationLat) {
    this.userLocationLat = userLocationLat;
    return this;
  }

  public long getUserLocationLong() {
    return userLocationLong;
  }

  public UserData setUserLocationLong(long userLocationLong) {
    this.userLocationLong = userLocationLong;
    return this;
  }

  public ImmutableSet<UserBook> getOwnedBooks() {
    return ImmutableSet.copyOf(ownedBooks);
  }

  public UserData setOwnedBooks(ImmutableSet<UserBook> ownedBooks) {
    this.ownedBooks.addAll(ownedBooks);
    return this;
  }

  public UserData addOwnedBook(UserBook book) {
    this.ownedBooks.add(book);
    return this;
  }

  public UserData addOwnedBook(String bookId) {
    this.ownedBooks.add(new UserBook(bookId, ExchangeStatus.BookListType.MY_BOOK));
    return this;
  }

  public ImmutableSet<UserBook> getExpectedBooks() {
    return ImmutableSet.copyOf(expectedBooks);
  }

  public UserData setExpectedBooks(ImmutableSet<UserBook> expectedBooks) {
    this.expectedBooks.addAll(expectedBooks);
    return this;
  }

  public UserData addExpectedBook(UserBook book) {
    this.expectedBooks.add(book);
    return this;
  }

  public UserData addExpectedBook(String bookId) {
    addExpectedBook(new UserBook(bookId, ExchangeStatus.BookListType.EXPECTED_BOOK));
    return this;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public UserData setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public UserData setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public UserData setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public Set<ExchangeData> getExchangeData() {
    return exchangeData;
  }

  public void addExchangeCycle(String exchangeId, String pickupBookId, String dropBookId) {
    updateBookStatusForExchange(pickupBookId);
    // TODO(suhas): Drop book check could be little loose. As user can accept something that is not in the list.
    // But add few checks to avoid spammy exchange alerts.
    exchangeData.add(new ExchangeData(exchangeId, pickupBookId, dropBookId));
  }

  private void updateBookStatusForExchange(String bookId) {
    for (UserBook userBook : ownedBooks) {
      if (userBook.getBookId().equalsIgnoreCase(bookId)) {
        Preconditions.checkArgument(userBook.getBookExchangeStatus().equals(
            ExchangeStatus.BookStatus.READY_FOR_EXCHANGE), "Book not READY_FOR_EXCHANGE");
        userBook.setBookExchangeStatus(ExchangeStatus.BookStatus.AWAITING_APPROVAL);
        return;
      }
    }
    throw new RuntimeException("Pickup book does not exists");
  }

  public static class ExchangeData {
    private String exchangeId;
    private String pickupBookId;
    private String dropBookId;

    private ExchangeData() {}

    private ExchangeData(String exchangeId, String pickupBookId, String dropBookId) {
      this.exchangeId = exchangeId;
      this.pickupBookId = pickupBookId;
      this.dropBookId = dropBookId;
    }

    public String getExchangeId() {
      return exchangeId;
    }

    public String getPickupBookId() {
      return pickupBookId;
    }

    public String getDropBookId() {
      return dropBookId;
    }
  }
}
