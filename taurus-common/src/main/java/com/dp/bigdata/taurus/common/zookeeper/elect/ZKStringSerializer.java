package com.dp.bigdata.taurus.common.zookeeper.elect;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Author   mingdongli
 * 16/3/15  下午5:03.
 */
public class ZKStringSerializer implements ZkSerializer {

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {

        if (data instanceof String) {
            String value = (String) data;
            return value.getBytes(Charset.forName("UTF-8"));
        }
        throw new ZkMarshallingError("not String type");
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {

        if ( null == bytes ) {
            return null;
        } else {
            try {
                return new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ZkMarshallingError("error charset");
            }
        }
    }
}
