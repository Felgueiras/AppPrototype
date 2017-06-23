package com.felgueiras.apps.geriatric_helper.Firebase;

import android.content.Context;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by felgueiras on 29/04/2017.
 */

public class CipherDecipherFiles {

    private final int keyLength;
    private final String algorithm;
    /**
     * Cipher object.
     */
    private Cipher cipher;
    /**
     * Secret key specification.
     */
    private SecretKeySpec secretKey;

    private static CipherDecipherFiles singleton = new CipherDecipherFiles();

    /**
     * Get Singleton instance.
     *
     * @return
     */
    public static CipherDecipherFiles getInstance() {
        return singleton;
    }

    /**
     * Constructor.
     */
    private CipherDecipherFiles() {
        keyLength = 16;
        String secret = "!@#$MySecr3tPassw0rd";
        algorithm = "AES";
//        byte[] key;
//        try {
//            key = fixSecret(secret, keyLength);
//            this.secretKey = new SecretKeySpec(key, algorithm);
//            this.cipher = Cipher.getInstance(algorithm);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Cipher contents of a file.
     *
     * @param f
     * @param context
     */
    void cipherFileContents(File f, Context context) {
        try {
            cipherFile(f, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Output a byte array of a certain length.
     *
     * @param s
     * @param length
     * @return byte[] of a certain length
     * @throws UnsupportedEncodingException
     */
    private byte[] fixSecret(String s, int length) {
        if (s.length() < length) {
            int missingLength = length - s.length();
            for (int i = 0; i < missingLength; i++) {
                s += " ";
            }
        }
        // convert string to byte array
        byte[] original = s.getBytes();

        return Arrays.copyOfRange(original, 0, 16);
    }


    /**
     * Cipher a file.
     *
     * @param f
     * @param context
     * @throws InvalidKeyException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private void cipherFile(File f, Context context)
            throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        // guarantee cipher is not null -> generate cipher
        String hashString = SharedPreferencesHelper.readHashString(context);
        generateSecretKeyCipher(hashString);

        this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
        writeToFile(f);
    }

    /**
     * Write ciphered contents to file.
     *
     * @param f
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private void writeToFile(File f) throws IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream in = new FileInputStream(f);
        byte[] input = new byte[(int) f.length()];
        in.read(input);

        FileOutputStream out = new FileOutputStream(f);
        byte[] output = this.cipher.doFinal(input);
        out.write(output);

        out.flush();
        out.close();
        in.close();
    }

    /**
     * Decipher a file.
     *
     * @param f
     * @param context
     */
    public void decipherFile(File f, Context context) {
        System.out.println("Decrypting file: " + f.getName());

        // guarantee cipher is not null -> generate cipher
        String hashString = SharedPreferencesHelper.readHashString(context);
        generateSecretKeyCipher(hashString);

        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            this.writeToFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the key for this cipher.
     *
     * @param userID
     * @param password
     * @param context
     */
    public void generateKey(String userID, String password, Context context) {
        // key = hash(userID+password)
        Log.d("Cipher", "Generating key");

        // concatenate userID with password
        String keyString = userID + password;

        // generate hash
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(keyString.getBytes());
        byte hashBytes[] = md.digest();
        String hashString = new String(hashBytes);

        // persiste hash string to shared preferences
        SharedPreferencesHelper.writeHashString(hashString, context);

        generateSecretKeyCipher(hashString);
    }

    /**
     * Generate secret key from hash string.
     *
     * @param hashString
     */
    private void generateSecretKeyCipher(String hashString) {
        byte[] key = fixSecret(hashString, 16);
        this.secretKey = new SecretKeySpec(key, algorithm);
        try {
            this.cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
