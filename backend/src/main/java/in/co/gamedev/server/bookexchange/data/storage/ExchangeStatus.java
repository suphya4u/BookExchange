package in.co.gamedev.server.bookexchange.data.storage;

/**
 * Created by suhas on 2/25/2015.
 */
public class ExchangeStatus {

  public enum BookStatus {
    READY_FOR_EXCHANGE,
    AWAITING_APPROVAL,
    APPROVED_EXCHANGE,
  };

  public enum ApprovalStatus {
    WAITING,
    APPROVED,
    CANCELLED
  };

  public enum BookListType {
    MY_BOOK,
    EXPECTED_BOOK
  }
}
