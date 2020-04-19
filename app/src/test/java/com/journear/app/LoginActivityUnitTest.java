package com.journear.app;

import com.journear.app.core.IsValid;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

public class LoginActivityUnitTest {

    // Test the validator IsValid
    @Test
    public final void password_isNull() {
        Assert.assertFalse("Password cannot be empty", IsValid.password(null));
    }

    @Test
    public final void password_isEmpty() {
        Assert.assertFalse("Password cannot be empty", IsValid.password(""));
    }

    @Test
    public final void password_isshort() {
        Assert.assertFalse("Password cannot be less than 8 characters", IsValid.password("abc"));
    }

    public final void password_Invalid1() {
        Assert.assertFalse("password must contain a number, English letter and special character", IsValid.password("abcfksadfjdksl"));
    }

    public final void password_Invalid2() {
        Assert.assertFalse("password cannot be less than 5 characters", IsValid.password("abc212dd"));
    }

    public final void password_Invalid3() {
        Assert.assertFalse("Password invalid character", IsValid.password("abcDSFD:2"));
    }

    public final void password_Valid1() {
        Assert.assertTrue("Correct Password", IsValid.password("BadB@ys2"));
    }

    public final void password_Valid2() {
        Assert.assertTrue("Correct Password", IsValid.password("Abcdefg8"));
    }

    public final void password_Valid3() {
        Assert.assertTrue("Correct Password", IsValid.password("Abcdefg!"));
    }

    public final void password_Valid4() {
        Assert.assertTrue("Correct Password", IsValid.password("abcdef!1"));
    }

    @Test
    public final void username_isCorrect() {
        Assert.assertTrue("Acceptable Username", IsValid.userName("username"));
    }

    @Test
    public final void username_isNull() {
        Assert.assertFalse("username cannot be null", IsValid.userName(null));
        Assert.assertFalse("Username cannot be blank", IsValid.userName(""));
    }

    @Test
    public final void username_shorterThan4Char() {
        Assert.assertFalse("username cannot be shorter than 4 characters", IsValid.userName("use"));
        Assert.assertFalse("username cannot be shorter than 4 characters", IsValid.userName("Nick"));
    }

    @Test
    public final void username_cannotStartWithNumber() {
        Assert.assertFalse("username cannot start with a number", IsValid.userName("4aaa"));
        Assert.assertTrue("username cannot start with a number", IsValid.userName("Batm4n"));
    }

    @Test
    public final void username_InvalidCharacters() {
        Assert.assertFalse("Username cannot have non-digits and non-english characters", IsValid.userName("u$ern@me"));
    }

}
