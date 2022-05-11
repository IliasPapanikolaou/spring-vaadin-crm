package com.example.application.it.elements;

import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "class", contains = "login-view")
public class LoginViewElement extends VerticalLayoutElement {

    public boolean login(String username, String password) {
        // $() is an element selector, we can grab elements from the browser
        LoginFormElement form = $(LoginFormElement.class).first(); // Get the first one that is visible

        form.getUsernameField().setValue(username);
        form.getPasswordField().setValue(password);
        form.getSubmitButton().click();

        // return assertion false that the login page still exist after login
        return !$(LoginFormElement.class).onPage().exists();
    }
}
