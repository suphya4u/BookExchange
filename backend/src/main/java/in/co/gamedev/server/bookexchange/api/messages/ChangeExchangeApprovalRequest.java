package in.co.gamedev.server.bookexchange.api.messages;

import in.co.gamedev.server.bookexchange.data.storage.ExchangeStatus;

/**
 * Created by suhas on 2/27/2015.
 */
public class ChangeExchangeApprovalRequest {

  private String exchangeCycleId;
  private String userId;
  private ExchangeStatus.ApprovalStatus newApprovalStatus;

  public String getExchangeCycleId() {
    return exchangeCycleId;
  }

  public ChangeExchangeApprovalRequest setExchangeCycleId(String exchangeCycleId) {
    this.exchangeCycleId = exchangeCycleId;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public ChangeExchangeApprovalRequest setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public ExchangeStatus.ApprovalStatus getNewApprovalStatus() {
    return newApprovalStatus;
  }

  public ChangeExchangeApprovalRequest setNewApprovalStatus(
      ExchangeStatus.ApprovalStatus newApprovalStatus) {
    this.newApprovalStatus = newApprovalStatus;
    return this;
  }
}
