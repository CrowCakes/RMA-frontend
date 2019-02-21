package com.example.rma;

import javax.servlet.annotation.WebServlet;

import com.example.rma.access.AccessControl;
import com.example.rma.access.ServerAccessControl;
import com.example.rma.classes.ConnectionManager;
import com.example.rma.classes.ObjectConstructor;
import com.example.rma.access.LoginForm;
import com.example.rma.access.LoginForm.LoginListener;
import com.example.rma.MainScreen;
import com.example.rma.MyUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	private AccessControl accessControl = new ServerAccessControl();
	private ObjectConstructor constructor = new ObjectConstructor();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	Responsive.makeResponsive(this);
    	setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Returned Merchandise");
        
        if (!accessControl.isUserSignedIn()) {
        	//add the login form if not signed in
        	setContent(new LoginForm(accessControl, new LoginListener() {
                @Override
                //let the user in if login was successful
                public void loginSuccessful() {
                    getMainPage(constructor);
                }
            }));
        } else {
        	//already logged in
            getMainPage(constructor);
        }
    }
    
    //set the system view
    protected void getMainPage(ObjectConstructor constructor) {
    	addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(MyUI.this, constructor));
        getNavigator().navigateTo(getNavigator().getState());
    }
    
    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
