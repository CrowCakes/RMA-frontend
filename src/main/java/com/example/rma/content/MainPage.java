package com.example.rma.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.example.rma.classes.ConnectionManager;
import com.example.rma.layout.MainPageLayout;
import com.example.rma.classes.OnDemandFileDownloader;
import com.example.rma.classes.Entry;
import com.example.rma.classes.ObjectConstructor;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;

public class MainPage extends MainPageLayout {
	private ConnectionManager manager = new ConnectionManager();
	private ObjectConstructor constructor;
	
	//Entry editing form
	private EntryForm entryForm;
	
	//Page variables
	int count = 0;
	int MAX_LIMIT = 20;
	int limit = MAX_LIMIT;
	int offset = 0;
	
	/**
	 * Prepares the UI elements of the page.
	 * @param user
	 * @param constructor
	 */
	public MainPage(String user, ObjectConstructor constructor) {
		this.constructor = constructor;

		if (user.equals("Admin")) {
			entryForm = new EntryForm(this);
			layout.addComponent(entryForm);
		}
		preparePagination();
		prepareGrid(user);
		prepareButton();
		preparePanel();
		refreshView();
		addComponent(layout);

	}
	
	/**
	 * Prepares the buttons needed to paginate database data
	 */
	private void preparePagination() {
		Button previous = new Button(String.format("Previous %d", MAX_LIMIT)); 
		Button next = new Button(String.format("Next %d", MAX_LIMIT));
		
		previous.addClickListener(e -> {
        	offset = (offset - limit < 0) ? 0 : offset - limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
		next.addClickListener(e -> {
        	offset = (offset + limit > count) ? offset : offset + limit;
        	limit = (offset + limit > count) ? count - offset : limit;
        	displayNew(offset, limit);
        	
        	display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
        	limit = MAX_LIMIT;
        });
		
		pagination.addComponents(previous, display_count, next);
	}
	
	/**
	 * Prepares the actions triggered by pressing the various buttons on the page
	 */
	private void prepareButton() {
		refresh.addClickListener(e -> {
			refreshView();
			generateReport.setVisible(false);
		}
		);
		
		viewOpen.addClickListener(e -> {
			viewOpenEntries();
			generateReport.setVisible(false);
		});
		
		create.addClickListener(e -> {
			generateReport.setVisible(false);
			entryForm.setEntry(emptyEntry());
		});
		
		openReport.addClickListener(e -> {
			year.setValue("");
			entryForm.setVisible(false);
			generateReport.setVisible(true);
		});
		
		report.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportTSC.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportQuarter.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportSupplier.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportOpen.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportIndividualSupplier.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please refresh the page after the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		OnDemandFileDownloader.OnDemandStreamResource resource = new  OnDemandFileDownloader.OnDemandStreamResource()
        {
            @Override
            public String getFilename()
            {
            	return String.format("RMA Summary - %s.xls", year.getValue());
            }

            @Override
            public InputStream getStream()
            {	
                return new ByteArrayInputStream(
                		constructor.generateReport(
                				manager, 
                				Integer.parseInt(year.getValue())
                		)
                );
            }
         };
         
         OnDemandFileDownloader downloader = new OnDemandFileDownloader(
        	        resource);
         downloader.extend(report);
         
         OnDemandFileDownloader.OnDemandStreamResource resourceQuarter = new  OnDemandFileDownloader.OnDemandStreamResource()
         {
             @Override
             public String getFilename()
             {
             	return String.format("RMA Quarterly Summary - %s.xls", year.getValue());
             }

             @Override
             public InputStream getStream()
             {	
                 return new ByteArrayInputStream(
                 		constructor.generateReportQuarter(
                 				manager, 
                 				Integer.parseInt(year.getValue())
                 		)
                 );
             }
          };
          
         OnDemandFileDownloader downloaderQuarter = new OnDemandFileDownloader(
         	        resourceQuarter);
         downloaderQuarter.extend(reportQuarter);
         
         OnDemandFileDownloader.OnDemandStreamResource resourceTSC = new  OnDemandFileDownloader.OnDemandStreamResource()
         {
             @Override
             public String getFilename()
             {
             	return String.format("RMA TSC Summary - %s.xls", year.getValue());
             }

             @Override
             public InputStream getStream()
             {	
                 return new ByteArrayInputStream(
                 		constructor.generateReportTSC(
                 				manager, 
                 				Integer.parseInt(year.getValue())
                 		)
                 );
             }
          };
          
          OnDemandFileDownloader downloaderTSC = new OnDemandFileDownloader(
         	        resourceTSC);
         downloaderTSC.extend(reportTSC);
         
         OnDemandFileDownloader.OnDemandStreamResource resourceSupplier = new  OnDemandFileDownloader.OnDemandStreamResource()
         {
             @Override
             public String getFilename()
             {
             	return String.format("RMA Supplier Performance - %s.xls", year.getValue());
             }

             @Override
             public InputStream getStream()
             {	
                 return new ByteArrayInputStream(
                 		constructor.generateReportSupplier(
                 				manager, 
                 				Integer.parseInt(year.getValue())
                 		)
                 );
             }
          };
          
          OnDemandFileDownloader downloaderSupplier = new OnDemandFileDownloader(
         	        resourceSupplier);
         downloaderSupplier.extend(reportSupplier);
         
         OnDemandFileDownloader.OnDemandStreamResource resourceOpen = new  OnDemandFileDownloader.OnDemandStreamResource()
         {
             @Override
             public String getFilename()
             {
             	return String.format("RMA Open Entries - %s.xls", year.getValue());
             }

             @Override
             public InputStream getStream()
             {	
                 return new ByteArrayInputStream(
                 		constructor.generateReportOpen(
                 				manager, 
                 				Integer.parseInt(year.getValue())
                 		)
                 );
             }
          };
          
          OnDemandFileDownloader downloaderOpen = new OnDemandFileDownloader(
         	        resourceOpen);
         downloaderOpen.extend(reportOpen);
         
         OnDemandFileDownloader.OnDemandStreamResource resourceIndividualSupplier = new  OnDemandFileDownloader.OnDemandStreamResource()
         {
             @Override
             public String getFilename()
             {
             	return String.format("RMA %s - %s.xls", supplier.getValue(), year.getValue());
             }

             @Override
             public InputStream getStream()
             {	
                 return new ByteArrayInputStream(
                 		constructor.generateReportIndividualSupplier(
                 				manager, 
                 				Integer.parseInt(year.getValue()),
                 				supplier.getValue()
                 		)
                 );
             }
          };
          
          OnDemandFileDownloader downloaderIndividualSupplier = new OnDemandFileDownloader(
         	        resourceIndividualSupplier);
         downloaderIndividualSupplier.extend(reportIndividualSupplier);
	}
	
	/**
	 * Prepares the filter functionality and the display grid's appearance and columns.
	 * Also prepares grid interaction with EntryForm.
	 * @param user
	 */
	private void prepareGrid(String user) {
		filter.setPlaceholder("Search RTS# or Supplier name");
		filter.addValueChangeListener(e -> {
			if (!filter.getValue().isEmpty()) filterView();
			else refreshView();
		});
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		
		display_grid.setHeight("200px");
		display_grid.setWidth("1125px");
		display_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		
		display_grid.addColumn(Entry::getEntryID).setCaption("Entry#");
		display_grid.addColumn(Entry::getSupplier).setCaption("Supplier");
		display_grid.addColumn(Entry::getSalesOrder).setCaption("SO#");
		display_grid.addColumn(Entry::getClient).setCaption("Client");
		display_grid.addColumn(Entry::getReceiveDate).setCaption("Date Received");
		display_grid.addColumn(Entry::getRts).setCaption("RTS#");
		display_grid.addColumn(Entry::getDescription).setCaption("Description");
		display_grid.addColumn(Entry::getReportDate).setCaption("Date Reported");
		display_grid.addColumn(Entry::getProblem).setCaption("Problem");
		display_grid.addColumn(Entry::getPullOutDate).setCaption("Pull Out Date");
		display_grid.addColumn(Entry::getReturnDate).setCaption("Return Date");
		display_grid.addColumn(Entry::getStatus).setCaption("Status");
		display_grid.addColumn(Entry::getAging).setCaption("Aging");
		
		display_grid.setFrozenColumnCount(1);
		
        if (user.equals("Admin")) {
            this.display_grid.asSingleSelect().addValueChangeListener(event -> {
                if (event.getValue() == null) {
                	entryForm.setVisible(false);
                } else {
                	entryForm.setEntry(event.getValue());
                }
            });
        }
	}
	
	/**
	 * Prepares the report generation component and all UI elements within it.
	 */
	private void preparePanel() {
		year.setPlaceholder("Example: 2018");
		disableReportButtons();
		
		year.addValueChangeListener(e -> {
			if (year.getValue().length() == 4 && isDigit(year.getValue())) {
				enableReportButtons();
			}
			else {
				disableReportButtons();
			}
		});
		
		supplier.addValueChangeListener(e -> {
			if (year.getValue().length() == 4 && isDigit(year.getValue()) && !supplier.getValue().isEmpty()) {
				reportIndividualSupplier.setEnabled(true);
			}
			else {
				reportIndividualSupplier.setEnabled(false);
			}
		});
		
		generateReport.setVisible(false);
		layout.addComponent(generateReport);
	}
	
	/**
	 * Prepares a blank list to be inserted into display grid. For debug purposes only.
	 */
	private void testGrid() {
		List<Entry> test = new ArrayList<>();
		test.add(emptyEntry());
		display_grid.setItems(test);
	}
	
	/**
	 * Returns a dummy Entry. To be used as the bean for Add New Entry.
	 * @return
	 */
	private Entry emptyEntry() {
		return new Entry(0, "", "", "", new Date(DateTime.now().getMillis()), "", "",
				"", new Date(DateTime.now().getMillis()), 0, "", new Date(DateTime.now().getMillis()), new Date(DateTime.now().getMillis()),
				0, 0, "", "", 0, 0,
				"", "", "", 0, "", "", 0);
	}
	
	/**
	 * Refreshes the display grid with the latest set of Entries from the server.
	 * Returns user to Entries 0-20.
	 */
	public void refreshView() {
		display_grid.deselectAll();
		offset = 0;
		
		manager.connect();
		count = constructor.getEntryCount(manager);
		manager.disconnect();
		
		limit = (offset + limit > count) ? count - offset : limit;
		
		//System.out.println("-- Refresh --");
		manager.connect();
		List<Entry> update = constructor.constructEntry(manager, offset, limit);
		manager.disconnect();
		
		List<String> updateSuppliers = constructor.constructSuppliers(manager);
		
		display_grid.setItems(update);
		display_count.setValue(String.format("%d-%d of %d", offset, offset+limit, count));
		supplier.setItems(updateSuppliers);
		
		limit = MAX_LIMIT;
		
		//testGrid();
	}
	
	/**
	 * Displays the next/previous page of Entries.
	 * @param offset
	 * @param limit
	 */
	private void displayNew(int offset, int limit) {
		display_grid.deselectAll();
		
		//System.out.println("-- Refresh --");
		manager.connect();
		List<Entry> update = constructor.constructEntry(manager, offset, limit);
		manager.disconnect();
		
		display_grid.setItems(update);
	}
	
	/**
	 * Refreshes the display grid with the latest set of Open-status Entries from the server.
	 */
	public void viewOpenEntries() {
		display_grid.deselectAll();
		
		//System.out.println("-- Refresh --");
		manager.connect();
		List<Entry> update = constructor.constructOpenEntry(manager);
		manager.disconnect();
		
		display_grid.setItems(update);
		
		//testGrid();
	}
	
	/**
	 * Refreshes the display grid with the latest set of input-matched Entries from the server.
	 */
	private void filterView() {
		display_grid.deselectAll();
		
		//System.out.println("-- Filter --");
		manager.connect();
		List<Entry> update = constructor.filterEntry(manager, filter.getValue());
		manager.disconnect();
		
		display_grid.setItems(update);
	}
	
	/**
	 * Sets all the report generation buttons to Enabled.
	 */
	private void enableReportButtons() {
		report.setEnabled(true);
		reportTSC.setEnabled(true);
		reportQuarter.setEnabled(true);
		reportSupplier.setEnabled(true);
		reportOpen.setEnabled(true);
		supplier.setEnabled(true);
	}
	
	/**
	 * Sets all the report generation buttons to be Disabled.
	 */
	private void disableReportButtons() {
		report.setEnabled(false);
		reportTSC.setEnabled(false);
		reportQuarter.setEnabled(false);
		reportSupplier.setEnabled(false);
		reportOpen.setEnabled(false);
		supplier.setEnabled(false);
		reportIndividualSupplier.setEnabled(false);
	}

	/**
	 * Checks if the given String contains only digits 0 to 9.
	 */
	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
}
