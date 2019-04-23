package com.example.rma.layout;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class EntryFormLayout extends Panel {
	protected TextField id = new TextField("Entry#");
	protected TextField supplier = new TextField("Supplier");
	protected TextField so = new TextField("Sales Order");
	protected TextField client = new TextField("Client");
	protected TextField rts = new TextField("RTS");
	protected TextField description = new TextField("Description");
	protected TextField serial = new TextField("Serial#");
	protected TextField quantity = new TextField("Quantity");
	protected TextField problem = new TextField("Problem");
	protected TextField nonWorkingDays = new TextField("# of non-working days");
	protected TextField turnaround = new TextField("Turnaround (in days)");
	protected TextField quantityRemaining = new TextField("Qty Remaining");
	protected TextField pos = new TextField("Client POS");
	protected TextField rtc = new TextField("Client RTC");
	protected TextField quantityReturned = new TextField("Qty Returned");
	protected TextField newSerial = new TextField("New Serial#");
	protected NativeSelect<String> status = new NativeSelect<>("Status");
	protected TextField supplierPOS = new TextField("For Supplier Reference: Pull-out Slip");
	protected TextField supplierReturned = new TextField("For Supplier Reference: Return Slip");
	protected TextField trace = new TextField("Parent Entry#");
	
	protected TextArea remarks = new TextArea("Remarks");
	
	protected DateField receiveDate = new DateField("Date Received");
	protected DateField reportDate = new DateField("Date Reported");
	protected DateField pullOutDate = new DateField("Pull Out Date");
	protected DateField returnDate = new DateField("Return Date");
	
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button delete = new Button("Delete");
	
	protected HorizontalLayout fields = new HorizontalLayout(id, trace);
	protected HorizontalLayout fields1 = new HorizontalLayout(supplier,
			so, client, rts, description,
			serial, quantity);
	protected HorizontalLayout fields2 = new HorizontalLayout(problem, nonWorkingDays,
			turnaround, quantityRemaining, pos, rtc, quantityReturned);
	protected HorizontalLayout fields3 = new HorizontalLayout(newSerial, supplierPOS, supplierReturned);
	protected HorizontalLayout dates = new HorizontalLayout(receiveDate, reportDate,
			pullOutDate, returnDate, status);
	
	protected HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

	protected VerticalLayout layout = new VerticalLayout(fields, fields1, fields2, fields3, dates, 
			remarks,
			buttons);
}
