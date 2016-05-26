package com.cip.crane.common.structure;

/**
 * Author   mingdongli
 * 16/4/22  上午12:18.
 */
public class IntegerConverter implements Converter<Integer> {

    @Override
    public Integer convertTo(String lionValue) {
        return Integer.parseInt(lionValue);
    }
}
