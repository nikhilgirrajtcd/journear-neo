package com.journear.app.core.utils;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    public static SecretKey generateKey() {
        return new SecretKeySpec(AppConstants.EncryptionPassword.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        try {
            /* Encrypt the message. */
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] cipherTextBytes = cipher.doFinal(message.getBytes("UTF-8"));
            return cipherTextBytes;
        } catch (Exception ex) {
            //Log.e("Encryptor", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public static String decryptMsg(byte[] cipherTextBytes, SecretKey secret) {
        try {
            /* Decrypt the message, given derived encContentValues and initialization vector. */
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            String decryptString = new String(cipher.doFinal(cipherTextBytes), "UTF-8");
            return decryptString;
        } catch (Exception ex) {
           // Log.e("Encryptor", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
