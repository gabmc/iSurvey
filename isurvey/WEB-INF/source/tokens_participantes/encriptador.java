/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tokens_participantes;



import javax.crypto.*;
import javax.crypto.spec.*;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
/**
 * Utilidades para encriptar y desencriptar
 * de manera simetrica usando TripleDES y SHA1
 * <br>
 */
public class encriptador {

   String algorithm = "PBEWithSHA1AndDESede";

   public String encrypt(String pwd, String text) throws Throwable {

       PBEKeySpec pbeKeySpec;
       PBEParameterSpec pbeParamSpec;
       SecretKeyFactory keyFac;

       // Salt
       byte[] salt = {
           (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
           (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
       };

       // Iteration count
       int count = 20;

       // Create PBE parameter set
       pbeParamSpec = new PBEParameterSpec(salt, count);

       // Prompt user for encryption password.
       // Collect user password as char array (using the
       // "readPasswd" method from above), and convert
       // it into a SecretKey object, using a PBE key
       // factory.
       pbeKeySpec = new PBEKeySpec(pwd.toCharArray());
       keyFac = SecretKeyFactory.getInstance(algorithm);
       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

       // Create PBE Cipher
       Cipher pbeCipher = Cipher.getInstance(algorithm);

       // Initialize PBE Cipher with key and parameters
       pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

       // Our cleartext
       byte[] cleartext = text.getBytes("ISO-8859-1");

       // Encrypt the cleartext
       byte[] ciphertext = pbeCipher.doFinal(cleartext);

       String output = dinamica.Base64.encodeToString(ciphertext, false);

       return output;

   }

   public String decrypt(String pwd, String data) throws Throwable {

       PBEKeySpec pbeKeySpec;
       PBEParameterSpec pbeParamSpec;
       SecretKeyFactory keyFac;

       // Salt
       byte[] salt = {
           (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
           (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
       };

       // Iteration count
       int count = 20;

       // Create PBE parameter set
       pbeParamSpec = new PBEParameterSpec(salt, count);

       // Prompt user for encryption password.
       // Collect user password as char array (using the
       // "readPasswd" method from above), and convert
       // it into a SecretKey object, using a PBE key
       // factory.
       pbeKeySpec = new PBEKeySpec(pwd.toCharArray());
       keyFac = SecretKeyFactory.getInstance(algorithm);
       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

       // Create PBE Cipher
       Cipher pbeCipher = Cipher.getInstance(algorithm);

       // Initialize PBE Cipher with key and parameters
       pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

       // Our encrypted text
       byte[] text = dinamica.Base64.decode(data);

       // Encrypt the cleartext
       byte[] ciphertext = pbeCipher.doFinal(text);

       String output = new String(ciphertext, "ISO-8859-1");

       return output;

   }
}
