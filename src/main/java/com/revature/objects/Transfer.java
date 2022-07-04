package com.revature.objects;

public class Transfer {
	private int transferId;
	private int transactionNumber;
	private int OriginId;
	private int destinationId;
	private int amount;
	private String transferType;
	public Transfer() {
		super();
	}
	public Transfer(int transferId, int transactionNumber, int originId, int destinationId, int amount,
			String transferType) {
		super();
		this.transferId = transferId;
		this.transactionNumber = transactionNumber;
		OriginId = originId;
		this.destinationId = destinationId;
		this.amount = amount;
		this.transferType = transferType;
	}
	public int getTransferId() {
		return transferId;
	}
	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public int getOriginId() {
		return OriginId;
	}
	public void setOriginId(int originId) {
		OriginId = originId;
	}
	public int getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	@Override
	public String toString() {
		return "Transfer [transferId=" + transferId + ", transactionNumber=" + transactionNumber + ", OriginId="
				+ OriginId + ", destinationId=" + destinationId + ", amount=" + amount + ", transferType="
				+ transferType + "]";
	}
	
}
