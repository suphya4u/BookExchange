package in.co.gamedev.server.bookexchange.api.messages;

import java.util.List;

import in.co.gamedev.server.bookexchange.data.storage.BookData;
import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus;

/**
 * Created by suhas on 2/21/2015.
 */
public class FetchExchangeDetailsResponse extends ServiceResponse {

  private List<Exchange> exchanges;

  public List<Exchange> getExchanges() {
    return exchanges;
  }

  public FetchExchangeDetailsResponse setExchanges(List<Exchange> exchanges) {
    this.exchanges = exchanges;
    return this;
  }

  public class Exchange {
    private BookData pickupBook;
    private BookData dropBook;
    private ExchangeStatus.ApprovalStatus myApprovalStatus;
    private ExchangeStatus.ApprovalStatus otherUserApprovalStatus;

    public BookData getPickupBook() {
      return pickupBook;
    }

    public Exchange setPickupBook(BookData pickupBook) {
      this.pickupBook = pickupBook;
      return this;
    }

    public BookData getDropBook() {
      return dropBook;
    }

    public Exchange setDropBook(BookData dropBook) {
      this.dropBook = dropBook;
      return this;
    }

    public ExchangeStatus.ApprovalStatus getMyApprovalStatus() {
      return myApprovalStatus;
    }

    public Exchange setMyApprovalStatus(ExchangeStatus.ApprovalStatus myApprovalStatus) {
      this.myApprovalStatus = myApprovalStatus;
      return this;
    }

    public ExchangeStatus.ApprovalStatus getOtherUserApprovalStatus() {
      return otherUserApprovalStatus;
    }

    public Exchange setOtherUserApprovalStatus(ExchangeStatus.ApprovalStatus otherUserApprovalStatus) {
      this.otherUserApprovalStatus = otherUserApprovalStatus;
      return this;
    }
  }
}
