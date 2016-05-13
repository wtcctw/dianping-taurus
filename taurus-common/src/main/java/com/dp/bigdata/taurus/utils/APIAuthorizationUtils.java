package com.dp.bigdata.taurus.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpRequestBase;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Author   mingdongli
 * 16/4/25  下午4:08.
 */
public class APIAuthorizationUtils {

    private static final Log serviceLog = LogFactory.getLog("service");
    public static final String HTTP_HEADER_DATE = "Date";
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    public static final String MEITUAN_AUTH_METHOD = "MWS";
    public static final String HTTP_HEADER_TIME_ZONE = "GMT";
    public static final String HTTP_HEADER_DATE_FORMAT = "EEE\', \'dd\' \'MMM\' \'yyyy\' \'HH:mm:ss\' \'z";
    public static final String ALGORITHM_HMAC_SHA1 = "HmacSHA1";
    public static final String BA_REQUST_ATTRIBUTE_CLIENTID="clientId";

    public static void generateAuthAndDateHeader(HttpRequestBase request, String client, String secret) {
        Date sysdate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(HTTP_HEADER_DATE_FORMAT, Locale.US);
        df.setTimeZone(TimeZone.getTimeZone(HTTP_HEADER_TIME_ZONE));
        String date = df.format(sysdate);
        String string_to_sign = request.getMethod().toUpperCase() + " " + request.getURI().getPath() + "\n" + date;
        String sig = secret;
        String encoding;

        try {
            encoding = getSignature(string_to_sign.getBytes(), sig.getBytes());
        } catch (Exception var10) {
            return;
        }

        String authorization = MEITUAN_AUTH_METHOD + " " + client + ":" + encoding;
        request.addHeader(HTTP_HEADER_AUTHORIZATION, authorization);
        request.addHeader(HTTP_HEADER_DATE, date);
    }

    public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, ALGORITHM_HMAC_SHA1);
        Mac mac = Mac.getInstance(ALGORITHM_HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return new String(Base64.encodeBase64(rawHmac));
    }

    public static String generateSignature(String method, String reqUrl, String date, String secret) {
        StringBuilder sign = new StringBuilder();
        sign.append(method);
        sign.append(" ");
        sign.append(reqUrl);
        sign.append("\n");
        sign.append(date);
        byte[] sha1 = hmacSha1(sign.toString(), secret);

        try {
            String signature = new String(Base64.encodeBase64(sha1), "UTF-8");
            return signature;
        } catch (UnsupportedEncodingException var8) {
            serviceLog.error("generate signature error", var8);
            return "";
        }
    }

    private static byte[] hmacSha1(String value, String key) {
        try {
            byte[] e = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(e, ALGORITHM_HMAC_SHA1);
            Mac mac = Mac.getInstance(ALGORITHM_HMAC_SHA1);
            mac.init(signingKey);
            return mac.doFinal(value.getBytes());
        } catch (Exception var5) {
            serviceLog.error("hmac_sha1 error", var5);
            return null;
        }
    }

}
