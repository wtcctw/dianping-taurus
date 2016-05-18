package com.dp.bigdata.taurus.common.netty.codec;

import java.nio.charset.Charset;

/**
 * Author   mingdongli
 * 16/5/18  上午10:22.
 */
public abstract class RemotingSerializable {

    public String toJson() {
        return toJson(false);
    }


    public String toJson(final boolean prettyFormat) {
        return toJson(this, prettyFormat);
    }


    public static String toJson(final Object obj, boolean prettyFormat) {
        if(prettyFormat){
            return JsonBinder.getNonEmptyBinder().toPrettyJson(obj);
        }
        return JsonBinder.getNonEmptyBinder().toJson(obj);
    }


    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JsonBinder.getNonEmptyBinder().fromJson(json, classOfT);
    }


    public byte[] encode() {
        final String json = this.toJson();
        if (json != null) {
            return json.getBytes();
        }
        return null;
    }


    public static byte[] encode(final Object obj) {
        final String json = toJson(obj, false);
        if (json != null) {
            return json.getBytes(Charset.forName("UTF-8"));
        }
        return null;
    }

    public static <T> T decode(final byte[] data, Class<T> classOfT) {
        final String json = new String(data, Charset.forName("UTF-8"));
        T t = fromJson(json, classOfT);
        return t;
    }
}
