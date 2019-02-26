package com.example.rma.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.example.rma.classes.ConnectionManager;
import com.example.rma.layout.MainPageLayout;
import com.example.rma.classes.OnDemandFileDownloader;
import com.example.rma.classes.Entry;
import com.example.rma.classes.ObjectConstructor;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;

public class MainPage extends MainPageLayout {
	private ConnectionManager manager = new ConnectionManager();
	private ObjectConstructor constructor;
	
	private EntryForm entryForm;
	
	public MainPage(String user, ObjectConstructor constructor) {
		this.constructor = constructor;

		if (user.equals("Admin")) {
			entryForm = new EntryForm(this);
			layout.addComponent(entryForm);
		}
		
		prepareGrid(user);
		prepareButton();
		preparePanel();
		refreshView();
		addComponent(layout);

	}
	
	private void prepareButton() {
		refresh.addClickListener(e -> {
			refreshView();
			generateReport.setVisible(false);
		}
		);
		
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
			Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportTSC.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportQuarter.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
		});
		
		reportSupplier.addClickListener(e -> {
			//generateReport.setVisible(false);
			disableReportButtons();
			Notification.show("Notice", "Please wait while the report is generated", Notification.Type.TRAY_NOTIFICATION);
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
	}
	
	private void prepareGrid(String user) {
		filter.setPlaceholder("Search by RTS#");
		filter.addValueChangeListener(e -> filterView());
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		
		display_grid.setHeight("250px");
		display_grid.setWidth("1125px");
		display_grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		
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
		
		generateReport.setVisible(false);
		layout.addComponent(generateReport);
	}
	
	private void testGrid() {
		List<Entry> test = new ArrayList<>();
		test.add(emptyEntry());
		display_grid.setItems(test);
	}
	
	private Entry emptyEntry() {
		return new Entry(0, "", "", "", new Date(DateTime.now().getMillis()), "", "",
				"", new Date(DateTime.now().getMillis()), 0, "", new Date(DateTime.now().getMillis()), new Date(DateTime.now().getMillis()),
				0, 0, "", "", 0, 0,
				"", "", "", 0, 0);
	}
	
	public void refreshView() {
		display_grid.deselectAll();
		
		//System.out.println("-- Refresh --");
		manager.connect();
		List<Entry> update = constructor.constructEntry(manager);
		manager.disconnect();
		
		display_grid.setItems(update);
		
		//testGrid();
	}
	
	private void filterView() {
		display_grid.deselectAll();
		
		//System.out.println("-- Filter --");
		manager.connect();
		List<Entry> update = constructor.filterEntry(manager, filter.getValue());
		manager.disconnect();
		
		display_grid.setItems(update);
	}
	
	private void enableReportButtons() {
		report.setEnabled(true);
		reportTSC.setEnabled(true);
		reportQuarter.setEnabled(true);
		reportSupplier.setEnabled(true);
	}
	
	private void disableReportButtons() {
		report.setEnabled(false);
		reportTSC.setEnabled(false);
		reportQuarter.setEnabled(false);
		reportSupplier.setEnabled(false);
	}

	private boolean isDigit(String s) {
		if (s.matches("[0-9]+")) return true;
		else return false;
	}
}
