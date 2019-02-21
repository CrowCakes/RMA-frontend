package com.example.rma.content;

import java.sql.Date;

import com.example.rma.classes.ConnectionManager;
import com.example.rma.classes.Entry;
import com.example.rma.layout.EntryFormLayout;
import com.vaadin.data.Binder;
import com.vaadin.ui.Notification;

public class EntryForm extends EntryFormLayout {
	private ConnectionManager manager = new ConnectionManager();
	private Entry currentEntry;
	private MainPage parent;

    private Binder<Entry> binder = new Binder<>(Entry.class);
	
	public EntryForm(MainPage parent) {
		this.parent = parent;
		
		bind_fields();
		prepare_buttons();
		
		layout.setSizeUndefined();
		setHeight("300px");
		setWidth("1125px");
		setContent(layout);
		
		this.save.setStyleName("primary");
        //this.save.setClickShortcut(13, new int[0]);
		
		setVisible(false);
	}
	
	public void setEntry(Entry entry) {
		currentEntry = entry;
		
		delete.setVisible(currentEntry.getEntryID() != 0);
		binder.setBean(currentEntry);
		setVisible(true);
	}
	
	private void bind_fields() {
		status.setItems("Open", "Closed");
		
		this.binder.bind(id, Entry::getEntryIDStr, Entry::setEntryID);
		this.binder.bind(supplier, Entry::getSupplier, Entry::setSupplier);
		this.binder.bind(so, Entry::getSalesOrder, Entry::setSalesOrder);
		this.binder.bind(client, Entry::getClient, Entry::setClient);
		this.binder.bind(rts, Entry::getRts, Entry::setRts);
		this.binder.bind(description, Entry::getDescription, Entry::setDescription);
		this.binder.bind(serial, Entry::getSerial, Entry::setSerial);
		this.binder.bind(quantity, Entry::getQuantityStr, Entry::setQuantity);
		this.binder.bind(problem, Entry::getProblem, Entry::setProblem);
		this.binder.bind(nonWorkingDays, Entry::getNonWorkingDaysStr, Entry::setNonWorkingDays);
		this.binder.bind(turnaround, Entry::getTurnaroundStr, Entry::setTurnaround);
		this.binder.bind(quantityRemaining, Entry::getQuantityRemainingStr, Entry::setQuantityRemaining);
		this.binder.bind(pos, Entry::getPos, Entry::setPos);
		this.binder.bind(rtc, Entry::getRtc, Entry::setRtc);
		this.binder.bind(quantityReturned, Entry::getQuantityReturnedStr, Entry::setQuantityReturned);
		this.binder.bind(newSerial, Entry::getNewSerial, Entry::setNewSerial);
		this.binder.bind(status, Entry::getStatus, Entry::setStatus);
		
		this.binder.bind(remarks, Entry::getRemarks, Entry::setRemarks);

		this.binder.bind(receiveDate, Entry::getReceiveDateLocal, Entry::setReceiveDate);
		this.binder.bind(reportDate, Entry::getReportDateLocal, Entry::setReportDate);
		this.binder.bind(pullOutDate, Entry::getPullOutDateLocal, Entry::setPullOutDate);
		this.binder.bind(returnDate, Entry::getReturnDateLocal, Entry::setReturnDate);
		
		this.binder.bindInstanceFields(this);

		id.setEnabled(false);
		quantityRemaining.setEnabled(false);
		turnaround.setEnabled(false);
	}
	
	private void prepare_buttons() {
		save.addClickListener(e -> save());
		cancel.addClickListener(e -> cancel());
		delete.addClickListener(e -> delete());
	}
	
	private void save() {
		//if entryID is 0, make a new entry
		if (currentEntry.getEntryID() == 0) {
			//19 fields
			String query = String.format("InsertNewEntry\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f", 
					supplier.getValue(),
					so.getValue(),
					client.getValue(),
					Date.valueOf(receiveDate.getValue()),
					rts.getValue(),
					description.getValue(),
					serial.getValue(),
					Date.valueOf(reportDate.getValue()),
					quantity.getValue(),
					problem.getValue(),
					Date.valueOf(pullOutDate.getValue()),
					Date.valueOf(returnDate.getValue()),
					nonWorkingDays.getValue(),
					pos.getValue(),
					rtc.getValue(),
					quantityReturned.getValue(),
					newSerial.getValue(),
					remarks.getValue(),
					status.getValue());

			System.out.println("-- Create Entry --");
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			

			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//if it is not 0, then edit an existing entry
		else {
			//19+1 fields
			String query = String.format("EditEntry\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f", 
					currentEntry.getEntryID(),
					supplier.getValue(),
					so.getValue(),
					client.getValue(),
					Date.valueOf(receiveDate.getValue()),
					rts.getValue(),
					description.getValue(),
					serial.getValue(),
					Date.valueOf(reportDate.getValue()),
					quantity.getValue(),
					problem.getValue(),
					Date.valueOf(pullOutDate.getValue()),
					Date.valueOf(returnDate.getValue()),
					nonWorkingDays.getValue(),
					pos.getValue(),
					rtc.getValue(),
					quantityReturned.getValue(),
					newSerial.getValue(),
					remarks.getValue(),
					status.getValue());
			
			System.out.println("-- Edit Entry --");
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();

			Notification.show("Edit Entry", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		
		parent.refreshView();
		setVisible(false);
	}
	
	private void cancel() {
		setVisible(false);
	}
	
	private void delete() {
		parent.refreshView();
		setVisible(false);
	}
}
