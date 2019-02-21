package com.example.rma.classes;

import java.sql.Date;
import java.time.LocalDate;

public class Entry {
	private int entryID;
	private String supplier;
	private String salesOrder;
	private String client;
	private Date receiveDate;
	private String rts;
	private String description;
	private String serial;
	private Date reportDate;
	private int quantity;
	private String problem;
	private Date pullOutDate;
	private Date returnDate;
	private int nonWorkingDays;
	private int turnaround;
	private int quantityRemaining;
	private String pos;
	private String rtc;
	private int quantityReturned;
	private String newSerial;
	private String remarks;
	private String status;
	private int aging;
	
	public Entry(int entryID, 
			String supplier, 
			String salesOrder, 
			String client, 
			Date receiveDate, 
			String rts, 
			String description,
			String serial, 
			Date reportDate, 
			int quantity, 
			String problem, 
			Date pullOutDate, 
			Date returnDate,
			int nonWorkingDays, 
			int turnaround, 
			String pos, 
			String rtc, 
			int quantityReturned, 
			int quantityRemaining,
			String newSerial, 
			String remarks, 
			String status, 
			int aging) {
		super();
		this.entryID = entryID;
		this.supplier = supplier;
		this.salesOrder = salesOrder;
		this.client = client;
		this.receiveDate = receiveDate;
		this.rts = rts;
		this.description = description;
		this.serial = serial;
		this.reportDate = reportDate;
		this.quantity = quantity;
		this.problem = problem;
		this.pullOutDate = pullOutDate;
		this.returnDate = returnDate;
		this.nonWorkingDays = nonWorkingDays;
		this.turnaround = turnaround;
		this.quantityRemaining = quantityRemaining;
		this.pos = pos;
		this.rtc = rtc;
		this.quantityReturned = quantityReturned;
		this.newSerial = newSerial;
		this.remarks = remarks;
		this.status = status;
		this.aging = aging;
	}
	
	public Entry() {
		
	}
	
	public int numberOfColumns() {
		//do not include entryID
		return 22;
	}
	
	public int getEntryID() {
		return entryID;
	}
	
	public String getEntryIDStr() {
		return String.valueOf(entryID);
	}

	public void setEntryID(int entryID) {
		this.entryID = entryID;
	}
	
	public void setEntryID(String entryID) {
		this.entryID = Integer.parseInt(entryID);
	}

	public int getTurnaround() {
		return turnaround;
	}
	public String getTurnaroundStr() {
		return String.valueOf(turnaround);
	}
	public void setTurnaround(String turnaround) {
		this.turnaround = Integer.valueOf(turnaround);
	}
	public int getAging() {
		return aging;
	}
	public int getQuantityRemaining() {
		return quantityRemaining;
	}
	public String getQuantityRemainingStr() {
		return String.valueOf(quantityRemaining);
	}
	public void setQuantityRemaining(String quantityRemaining) {
		this.quantityRemaining = Integer.parseInt(quantityRemaining);
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getSalesOrder() {
		return salesOrder;
	}
	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public LocalDate getReceiveDateLocal() {
		return receiveDate.toLocalDate();
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public void setReceiveDate(LocalDate receiveDate) {
		this.receiveDate = Date.valueOf(receiveDate);
	}
	public String getRts() {
		return rts;
	}
	public void setRts(String rts) {
		this.rts = rts;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public LocalDate getReportDateLocal() {
		return reportDate.toLocalDate();
	}
	public void setReportDate(LocalDate reportDate) {
		this.reportDate = Date.valueOf(reportDate);
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getQuantityStr() {
		return String.valueOf(quantity);
	}
	public void setQuantity(String quantity) {
		this.quantity = Integer.parseInt(quantity);
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public Date getPullOutDate() {
		return pullOutDate;
	}
	public void setPullOutDate(Date pullOutDate) {
		this.pullOutDate = pullOutDate;
	}
	public LocalDate getPullOutDateLocal() {
		return pullOutDate.toLocalDate();
	}
	public void setPullOutDate(LocalDate pullOutDate) {
		this.pullOutDate = Date.valueOf(pullOutDate);
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public LocalDate getReturnDateLocal() {
		return returnDate.toLocalDate();
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = Date.valueOf(returnDate);
	}
	public int getNonWorkingDays() {
		return nonWorkingDays;
	}
	public void setNonWorkingDays(int nonWorkingDays) {
		this.nonWorkingDays = nonWorkingDays;
	}
	public String getNonWorkingDaysStr() {
		return String.valueOf(nonWorkingDays);
	}
	public void setNonWorkingDays(String nonWorkingDays) {
		this.nonWorkingDays = Integer.parseInt(nonWorkingDays);
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getRtc() {
		return rtc;
	}
	public void setRtc(String rtc) {
		this.rtc = rtc;
	}
	public int getQuantityReturned() {
		return quantityReturned;
	}
	public void setQuantityReturned(int quantityReturned) {
		this.quantityReturned = quantityReturned;
	}
	public String getQuantityReturnedStr() {
		return String.valueOf(quantityReturned);
	}
	public void setQuantityReturned(String quantityReturned) {
		this.quantityReturned = Integer.parseInt(quantityReturned);
	}
	public String getNewSerial() {
		return newSerial;
	}
	public void setNewSerial(String newSerial) {
		this.newSerial = newSerial;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
