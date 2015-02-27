package in.co.gamedev.server.bookexchange.data.storage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhas on 2/25/2015.
 */
@Entity
public class ExchangeCycle {

  @Id
  private String exchangeCycleId;
  private List<UserBookInvolved> userBooksInvolved;

  public ExchangeCycle() {
    userBooksInvolved = new ArrayList();
  }

  public String getExchangeCycleId() {
    return exchangeCycleId;
  }

  public ExchangeCycle setExchangeCycleId(String exchangeCycleId) {
    this.exchangeCycleId = exchangeCycleId;
    return this;
  }

  public List<UserBookInvolved> getUserBooksInvolved() {
    return userBooksInvolved;
  }

  public ExchangeCycle setUserBooksInvolved(List<UserBookInvolved> userBooksInvolved) {
    this.userBooksInvolved = userBooksInvolved;
    return this;
  }

  public static class UserBookInvolved {
    private String userId;
    private String pickupBookId;
    private String dropBookId;
    private ExchangeStatus.ApprovalStatus approvalStatus;

    public String getUserId() {
      return userId;
    }

    public UserBookInvolved setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    public String getPickupBookId() {
      return pickupBookId;
    }

    public UserBookInvolved setPickupBookId(String pickupBookId) {
      this.pickupBookId = pickupBookId;
      return this;
    }

    public String getDropBookId() {
      return dropBookId;
    }

    public UserBookInvolved setDropBookId(String dropBookId) {
      this.dropBookId = dropBookId;
      return this;
    }

    public ExchangeStatus.ApprovalStatus getApprovalStatus() {
      return approvalStatus;
    }

    public UserBookInvolved setApprovalStatus(ExchangeStatus.ApprovalStatus approvalStatus) {
      this.approvalStatus = approvalStatus;
      return this;
    }
  }
}
