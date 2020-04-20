package com.journear.app;
import com.journear.app.core.IsValid;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

public class Registration {

    @Test
    public final void email_isNull(){
        Assert.assertFalse("Cannot Register without valid Email ID", IsValid.email(null));
    }

    @Test
    public final void email_isInvalid1(){
        Assert.assertFalse("Invalid Email address", IsValid.email("xyzgmail"));
    }

    @Test
    public final void email_isInvalid2(){
        Assert.assertFalse("Invalid Email address", IsValid.email("xyz@gmail"));
    }

    @Test
    public final void email_isValid(){
        Assert.assertTrue(" ", IsValid.email("xyz@gmail.com"));
    }

    @Test
    public final void MobileNo_isInvalid1(){
        Assert.assertFalse("Invalid Mobile number", IsValid.Mobile("12a45b"));
    }

    @Test
    public final void MobileNo_isInvalid2(){
        Assert.assertFalse("Invalid Mobile number", IsValid.Mobile("08925679"));
    }

    @Test
    public final void MobileNo_isInvalid3(){
        Assert.assertFalse("Invalid Mobile number", IsValid.Mobile("345894589231"));
    }

    @Test
    public final void MobileNo_isValid1(){
        Assert.assertTrue("Invalid Mobile number", IsValid.Mobile("0894589231"));
    }

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

    @Test
    public final void password_Invalid1() {
        Assert.assertFalse("password must contain a number, English letter and special character", IsValid.password("abcfksadfjdksl"));
    }

    @Test
    public final void password_Invalid2() {
        Assert.assertFalse("password cannot be less than 5 characters", IsValid.password("abc212dd"));
    }

    @Test
    public final void password_Invalid3() {
        Assert.assertFalse("Password invalid character", IsValid.password("abcDSFD:2"));
    }

    @Test
    public final void password_Valid1() {
        Assert.assertTrue("Correct Password", IsValid.password("BadB@ys2"));
    }

    @Test
    public final void password_Valid2() {
        Assert.assertTrue("Correct Password", IsValid.password("Abcdefg8"));
    }


    @Test
    public final void password_Valid3() {
        Assert.assertTrue("Correct Password", IsValid.password("abcdef!1"));
    }


}
