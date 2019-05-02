package com.example.rma.content;

import java.sql.Date;

import org.vaadin.dialogs.ConfirmDialog;

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
	
    /**
     * Prepares the UI elements of the page.
     * @param parent
     */
	public EntryForm(MainPage parent) {
		this.parent = parent;
		
		bind_fields();
		prepare_buttons();
		
		layout.setSizeUndefined();
		setHeight("600px");
		setWidth("1150px");
		setContent(layout);
		
		this.save.setStyleName("primary");
        //this.save.setClickShortcut(13, new int[0]);
		
		setVisible(false);
	}
	
	/**
	 * Binds the given Entry for editing on the form.
	 * @param entry
	 */
	public void setEntry(Entry entry) {
		currentEntry = entry;
		
		delete.setVisible(currentEntry.getEntryID() != 0);
		binder.setBean(currentEntry);
		setVisible(true);
	}
	
	/**
	 * Prepares the UI elements for editing Entries.
	 */
	private void bind_fields() {
		status.setItems("Open", "Closed");
		trace.setPlaceholder("Leave as 0 if no parent");
		
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
		this.binder.bind(supplierPOS, Entry::getSupplierPOS, Entry::setSupplierPOS);
		this.binder.bind(supplierReturned, Entry::getSupplierReturned, Entry::setSupplierReturned);
		this.binder.bind(trace, Entry::getTraceStr, Entry::setTrace);
		
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
	
	/**
	 * Sets up the functions triggered by the save/cancel/delete buttons.
	 */
	private void prepare_buttons() {
		save.addClickListener(e -> save());
		cancel.addClickListener(e -> cancel());
		delete.addClickListener(e -> ConfirmDialog.show(this.getUI(), 
				"Confirmation", "Delete this entry?", "Yes", "No",
				new ConfirmDialog.Listener() {
					public void onClose(ConfirmDialog dialog) {
		        		if (dialog.isConfirmed()) {
		        			delete();
		        		}
		        		else {
		        			
		        		}
					}
				}
			)
		);
		//delete.setEnabled(true);
	}
	
	/**
	 * Sends entered data to the server to be added/edited into the database.
	 */
	private void save() {
		if (!validate()) {
			Notification.show("Error", "Please make sure your Quantity, Non-Working Days, Quantity Returned, and Trace# are all digits only", Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		//if entryID is 0, make a new entry
		if (currentEntry.getEntryID() == 0) {
			//22 fields
			String query = String.format(
					"InsertNewEntry\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f", 
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
					(Integer.parseInt(quantity.getValue()) - 
							Integer.parseInt(quantityReturned.getValue()) == 0) ? "Closed" : "Open",
					supplierPOS.getValue(),
					supplierReturned.getValue(),
					trace.getValue());

			System.out.println("-- Create Entry --");
			
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();
			

			Notification.show("Create New", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		//if it is not 0, then edit an existing entry
		else {
			//22+1 fields
			String query = String.format(
					"EditEntry\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f%s\f", 
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
					(Integer.parseInt(quantity.getValue()) - 
							Integer.parseInt(quantityReturned.getValue()) == 0) ? "Closed" : "Open",
					supplierPOS.getValue(),
					supplierReturned.getValue(),
					trace.getValue());
			
			System.out.println("-- Edit Entry --");
			manager.connect();
			String result = manager.send(query);
			manager.disconnect();

			Notification.show("Edit Entry", result, Notification.Type.HUMANIZED_MESSAGE);
		}
		
		parent.refreshView();
		parent.revealGrid();
		setVisible(false);
	}
	
	/**
	 * Hides the EntryForm.
	 */
	private void cancel() {
		parent.revealGrid();
		setVisible(false);
	}
	
	/**
	 * Sends query to the server for deletion of the current Entry.
	 * Should not work when EntryID is 0. 
	 */
	private void delete() {
		if (currentEntry.getEntryID() == 0) return;
		
		String query = String.format("DeleteEntry\f%s\f", id.getValue());
		
		System.out.println("-- Delete Entry --");
		//System.out.println(query);
		manager.connect();
		String result = manager.send(query);
		manager.disconnect();
		
		Notification.show("Delete Entry", result, Notification.Type.HUMANIZED_MESSAGE);
		
		parent.refreshView();
		parent.revealGrid();
		setVisible(false);
	}
	
	/**
	 * Checks if the given string contains only digits 0 to 9.
	 * @param s
	 * @return
	 */
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
	
	/** 
	 * Checks if all the given fields contain values that are only digits 0 to 9.
	 * @return
	 */
	private boolean validate() {
		if (!isDigit(quantity.getValue()) || !isDigit(nonWorkingDays.getValue()) || !isDigit(quantityReturned.getValue())
				|| !isDigit(trace.getValue())) return false;
		return true;
	}
}
