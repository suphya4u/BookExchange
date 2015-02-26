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

  public void setExchangeCycleId(String exchangeCycleId) {
    this.exchangeCycleId = exchangeCycleId;
  }

  public List<UserBookInvolved> getUserBooksInvolved() {
    return userBooksInvolved;
  }

  public void setUserBooksInvolved(List<UserBookInvolved> userBooksInvolved) {
    this.userBooksInvolved = userBooksInvolved;
  }

  public class UserBookInvolved {
    private String userId;
    private String pickupBookId;
    private String dropBookId;
    private ExchangeStatus.ApprovalStatus approvalStatus;

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public String getPickupBookId() {
      return pickupBookId;
    }

    public void setPickupBookId(String pickupBookId) {
      this.pickupBookId = pickupBookId;
    }

    public String getDropBookId() {
      return dropBookId;
    }

    public void setDropBookId(String dropBookId) {
      this.dropBookId = dropBookId;
    }

    public ExchangeStatus.ApprovalStatus getApprovalStatus() {
      return approvalStatus;
    }

    public void setApprovalStatus(ExchangeStatus.ApprovalStatus approvalStatus) {
      this.approvalStatus = approvalStatus;
    }
  }
}
