package com.dp.bigdata.taurus.core.structure;

/**
 * Author   mingdongli
 * 16/4/22  上午12:18.
 */
public class StringToInteger implements StringTo<Integer>{

    @Override
    public Integer stringConvertTo(String lionValue) {
        return Integer.parseInt(lionValue);
    }
}
