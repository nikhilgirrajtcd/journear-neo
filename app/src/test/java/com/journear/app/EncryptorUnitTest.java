package com.journear.app;

import com.journear.app.core.IsValid;
import com.journear.app.core.utils.Encryptor;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

public class EncryptorUnitTest {

    // Test the validator IsValid
    @Test
    public final void plainShouldMatchDecrypted() {
        String plainText = "Going to Ibiza";
        System.out.println("Plain text length: " + plainText.length());
        String encrypted = Encryptor.encryptMsg(plainText);
        System.out.println("Encrypted length: " + encrypted.length());
        String decrypted = Encryptor.decryptMsg(encrypted);
        Assert.assertEquals("Plain text matched decrypted text", plainText, decrypted);
    }

}
