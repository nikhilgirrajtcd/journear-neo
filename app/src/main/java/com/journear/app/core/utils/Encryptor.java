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

    private static SecretKey _key = null;

    private static SecretKey generateKey() {
        if (_key == null)
            _key = new SecretKeySpec(AppConstants.EncryptionPassword.getBytes(), "AES");
        return _key;
    }

    public static String encryptMsg(String message) {
        try {
            /* Encrypt the message. */
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            byte[] cipherTextBytes = cipher.doFinal(message.getBytes("UTF-8"));
            return Base64.encodeToString(cipherTextBytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Log.e("Encryptor", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public static String decryptMsg(String cipherText) {
        try {
            /* Decrypt the message, given derived encContentValues and initialization vector. */
            Cipher cipher = null;
            byte[] cipherTextBytes = Base64.decode(cipherText, Base64.DEFAULT);

            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, generateKey());
            String decryptString = new String(cipher.doFinal(cipherTextBytes), "UTF-8");
            return decryptString;
        } catch (Exception ex) {
             Log.e("Encryptor", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
