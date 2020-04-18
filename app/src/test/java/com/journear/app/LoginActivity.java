package com.journear.app;
import org.junit.Assert;
import org.junit.Test;

public class LoginActivity {
    @Test
    public final void password_isCorrect() {
        Assert.assertEquals("password", "password");
    }

    @Test
    public final void password_isIncorrect() {
        Assert.assertEquals("Incorrect password, please try again", "password", "username");
    }

    @Test
    public final void password_isNull() {
        Assert.assertEquals("Password cannot be empty", "password", " ");
    }

    @Test
    public final void password_InvalidCharacter() {
        Assert.assertEquals("incorrect password", "password", "pa$$word");
    }

    @Test
    public final void password_isshort() {
        Assert.assertEquals("password cannot be less than 5 characters", "password", "abc");
    }

    @Test
    public final void username_isCorrect() {
        Assert.assertEquals("username", "username");
    }

    @Test
    public final void username_isNull() {
        Assert.assertEquals("username cannot be empty", "username", " ");
    }

    @Test
    public final void username_InvalidCharacters() {
        Assert.assertEquals("Incorrect username, please try again", "username", "u$ern@me");
    }

}
