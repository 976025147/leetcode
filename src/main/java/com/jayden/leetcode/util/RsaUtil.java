package com.jayden.leetcode.util;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtil {

    //默认公钥
    public static final String PUBLIC_KEY_DEFAULT = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjLFTaYTCKLwtS9UUwoeZBjegRcQ1eQshEaS36g0iVBU5pCjHqA43Cs8LrlE/MYzlihqFP0XqPpOHyHcBq39ceuyy2zhAVV5G2oZv1xVkLAyThvl/jN93cLoHo708wyg3MwplPGzz2ragvdQIedWhb8YZSSU1mQm4tofAKBsxymrfOU65K87Ja3te3mNGqC4L74fQsQflIv7MNsmgbVaGrDsaS5QNo9Poje2+kkKlzxcR+nduJJj+Ap3f+FL58DpwnsDQbExXURm851f7iomtNIwPARdmnLucoOk9GkK50KuuZOLylnceaJ6iLjT2HkXdNipkco1YjxtS4Y3uzO4A2wIDAQAB";
    //默认私钥
    public static final String PRIVATE_KEY_DEFAULT = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCMsVNphMIovC1L1RTCh5kGN6BFxDV5CyERpLfqDSJUFTmkKMeoDjcKzwuuUT8xjOWKGoU/Reo+k4fIdwGrf1x67LLbOEBVXkbahm/XFWQsDJOG+X+M33dwugejvTzDKDczCmU8bPPatqC91Ah51aFvxhlJJTWZCbi2h8AoGzHKat85Trkrzslre17eY0aoLgvvh9CxB+Ui/sw2yaBtVoasOxpLlA2j0+iN7b6SQqXPFxH6d24kmP4Cnd/4UvnwOnCewNBsTFdRGbznV/uKia00jA8BF2acu5yg6T0aQrnQq65k4vKWdx5onqIuNPYeRd02KmRyjViPG1Lhje7M7gDbAgMBAAECggEAJstSm7ruqrrQpqNXuRgYut0lxXcH45uM/8dyqwf6GkNMBJb2DsH0rXdP2j4u9aPLO1x6t2q4345gl4cxH6/buU2BN+931dJMKT9+oUkVuQuytjof0KFh1uSE7MJmec2iAuHv7kyApSNMiWto9udQP3jZShEHiW5jz3j5JFXzUcPjMqp+Ei5Ctqy1AIt41405M8PgYAXuH+E/r+QzJ45zIjUeRIOakXdupJnSyDfDtochEWqdSOKmlTvpSXFnOxTuEU1J6Y1mmzUWK5YpbserG94rhqcIc26hKg9g+AjMzv/XjEz6n3iVLXei9a5s0jqkkT5Zu2UiLgHnnFGFfk1ZUQKBgQDQOxJmhgHknhfazPIrri/f4zyqA2lo7xa3uNXmx8Vdf3gSf1aCEqB4NyzjWs5wa2qzgmrKUhcEaLrkl+ij1m+kXulVP239JM2gHweMSPMBGWHa2vs8nmeY132rvJf9Ha7qVW6PMayXNxnmEuSMlYaXwzYvQ64r/gAHG3DfY76x2QKBgQCs9+S8brBHfGhvqj9ZArfpFXjkaNKvoRhvmAFsYw/ItPe496I9ByoAowSxieTx/vPtz3lxEkV2wyyumqv5P2bLpq8wZLU1/2w1QJwuh+/r/R01JJ1YcDXU//BR+tTl+5SmTxxYc2+rjsc2He3vU3YBfqRv+h5DSaLfkXSRgL7j0wKBgDaZRpnbSWPIkDiuxFDvu13ZF278tgbtqedCb9OzTezWH4W6HdS9TZY34W07wiV4BOJMr/Oc0qw1ScvmiPlsoJlYyZWwrAZUQBZcDH6cIhuHrEDWaknavw80cZ82NCQKRHLtM7AYNkjwCbpp+cNi8YeZh/uco26lixxR9aadYMMBAoGAc4p8Lys5cec21DDDdDAOrhJ72Qf0hQF1k0XU3ZQmvQm12McChwW8FwTOY9IbL1QBmb/X/aIY6MZjmJcVfb/OaQeFSyxaqCFhJQltyt9fcyJ3WkKFIMiEDS6SWBDYGnC90YJTCvrBg7YyeIHxqMb1svXn3i0T/hq4Q+OkHHYGDaUCgYB5iDKkuCVJOrLvuAcf7MF+rvSAgPuhRmHQjYBd+Lv9jAUf0gI+SVhToa5uyUVfPNwdJlsK+oM2zDYptU/7JVV8IJRtw8rThbnYClsCVwFZE+RfYr0TAvN1zxY6eqT67PIqbMIW226EbCGa9LaGzzr4R6BgTduT5bCBQnaaCv3tPA==";
    //日志
    private static Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        String[] keys = genKeyPair();
        //加密字符串
        String publicKey = keys[0];
        String privatekey = keys[1];
        String message = "测试字符串";
        logger.info("随机生成的公钥为:{}\n", publicKey);
        logger.info("随机生成的私钥为:{}\n", privatekey);
        String messageEn = encrypt(message, publicKey);
        logger.info("原始字符串:{}\n", message);
        logger.info("加密后的字符串为:{}\n", messageEn);
        String messageDe = decrypt(messageEn, privatekey);
        logger.info("还原后的字符串为:{}\n", messageDe);
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static String[] genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(2048, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        return new String[]{publicKeyString, privateKeyString};
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encrypt(String str) {
        try {
            return encrypt(str, PUBLIC_KEY_DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    public static String decrypt(String str) {
        try {
            return decrypt(str, PRIVATE_KEY_DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }


}

