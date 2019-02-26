package com.example.rma.layout;

import com.example.rma.classes.Entry;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainPageLayout extends CssLayout implements View {
	protected Grid<Entry> display_grid = new Grid<>();
	
	protected TextField filter = new TextField();
	
	protected Button refresh = new Button("Refresh");
	protected Button create = new Button("Create New");
	protected Button openReport = new Button("Generate Summary");
	
	protected TextField year = new TextField("Enter year:");
	protected Button report = new Button("Generate Monthly Summary");
	protected Button reportTSC = new Button("Generate TSC Summary");
	protected Button reportQuarter = new Button("Generate Quarterly Summary");
	protected Button reportSupplier = new Button("Generate Supplier Performance Report");
	protected FormLayout generateReport = new FormLayout(year, report, reportTSC, reportQuarter, reportSupplier);
	
	protected VerticalLayout layout = new VerticalLayout(new HorizontalLayout(filter, refresh, create, openReport), display_grid);
}
