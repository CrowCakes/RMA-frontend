package com.example.rma.layout;

import com.example.rma.classes.Entry;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainPageLayout extends CssLayout implements View {
	protected Grid<Entry> display_grid = new Grid<>();
	
	protected TextField filter = new TextField();
	
	protected Button refresh = new Button("Refresh");
	protected Button create = new Button("Create New");
	protected Button openReport = new Button("Generate Summary");
	protected Button viewOpen = new Button("View Open");
	
	protected TextField year = new TextField("Enter year:");
	protected Button report = new Button("Generate Monthly Summary");
	protected Button reportTSC = new Button("Generate TSC Summary");
	protected Button reportQuarter = new Button("Generate Quarterly Summary");
	protected Button reportSupplier = new Button("Generate Supplier Performance Report");
	protected Button reportOpen = new Button("Generate Summary of Open Entries");
	
	protected NativeSelect<String> supplier = new NativeSelect<>("Suppliers");
	protected Button reportIndividualSupplier = new Button("Generate Summary for Selected Supplier");
	
	protected HorizontalLayout generateReport = new HorizontalLayout(year,
			new VerticalLayout(report, reportTSC, reportQuarter, reportSupplier, reportOpen),
			new VerticalLayout(supplier, reportIndividualSupplier));
	
	protected HorizontalLayout pagination = new HorizontalLayout();
	protected Label display_count = new Label("");
	
	protected VerticalLayout layout = new VerticalLayout(
			new HorizontalLayout(filter, refresh, create, openReport, viewOpen), display_grid, pagination);
}
