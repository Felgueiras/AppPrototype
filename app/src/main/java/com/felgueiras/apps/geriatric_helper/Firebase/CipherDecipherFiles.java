package com.felgueiras.apps.geriatric_helper.Firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by felgueiras on 29/04/2017.
 */

public class CipherDecipherFiles {

    private Cipher cipher;
    private SecretKeySpec secretKey;

    private static CipherDecipherFiles singleton = new CipherDecipherFiles();

    public static CipherDecipherFiles getInstance() {
        return singleton;
    }

    private CipherDecipherFiles() {
        int length = 16;
        String secret = "!@#$MySecr3tPassw0rd";
        String algorithm = "AES";

        byte[] key;
        try {
            key = fixSecret(secret, length);
            this.secretKey = new SecretKeySpec(key, algorithm);
            this.cipher = Cipher.getInstance(algorithm);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void cipherFileContents(File f) {
        try {
            cipherFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] fixSecret(String s, int length) throws UnsupportedEncodingException {
        if (s.length() < length) {
            int missingLength = length - s.length();
            for (int i = 0; i < missingLength; i++) {
                s += " ";
            }
        }
        return s.substring(0, length).getBytes("UTF-8");
    }


    private void cipherFile(File f)
            throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
        writeToFile(f);
    }

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

    public void decipherFile(File f) {
        System.out.println("Decrypting file: " + f.getName());
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            this.writeToFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
