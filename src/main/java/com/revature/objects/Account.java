package com.revature.objects;

public class Account {
	
	private int accountId;
	private int ownerId;
	private boolean approved;
	private int currentBalance;
	public Account(int accountId, int ownerId, boolean approved, int currentBalance) {
		super();
		this.accountId = accountId;
		this.ownerId = ownerId;
		this.approved = approved;
		this.currentBalance = currentBalance;
	}
	public Account() {
		super();
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public int getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", ownerId=" + ownerId + ", currentBalance=" + currentBalance + "]";
	}

	
}
