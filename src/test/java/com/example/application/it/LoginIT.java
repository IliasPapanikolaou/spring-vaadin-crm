package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import org.junit.Assert;
import org.junit.Test;

public class LoginIT extends AbstractTest{

    public LoginIT() {
        super(""); // Testing the empty route
    }

    @Test
    public void loginAsValidUserSucceeds() {

        LoginViewElement loginView = $(LoginViewElement.class).first();
        Assert.assertTrue(loginView.login("user", "password"));
    }

    @Test
    public void loginAsInvalidUserSucceeds() {

        LoginViewElement loginView = $(LoginViewElement.class).first();
        Assert.assertFalse(loginView.login("user", "wrongPassword"));
    }
}
