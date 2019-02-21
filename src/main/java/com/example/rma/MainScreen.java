package com.example.rma;

import com.example.rma.Menu;
import com.example.rma.MyUI;
import com.example.rma.access.CurrentUser;
import com.example.rma.classes.ConnectionManager;
import com.example.rma.classes.ObjectConstructor;
import com.example.rma.content.MainPage;
import com.example.rma.Error;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class MainScreen extends HorizontalLayout {
	private Menu menu;
	
	public MainScreen(MyUI ui, ObjectConstructor constructor) {
		setSpacing(false);
		setStyleName("main-screen");
		
		CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();
        
        //set up system navigation
        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(Error.class);
        
        //set up the menu for system navigation
        menu = new Menu(navigator);
        
        // add the views
        menu.addView(new MainPage(CurrentUser.get(), constructor), "", "Main Page", VaadinIcons.CHART);
        
        // add listener for view change by user
        navigator.addViewChangeListener(viewChangeListener);
        
        //add all the things to menu bar
        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
	}
	
	ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
