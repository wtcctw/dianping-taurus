package com.dp.bigdata.taurus.core.structure;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author   mingdongli
 * 16/4/22  上午12:18.
 */
public class StringToListString implements StringTo<List<String>> {

    @Override
    public List<String> stringConvertTo(String lionValue) {
        if(StringUtils.isNotBlank(lionValue)){
            String[] array = StringUtils.split(lionValue, ",");
            return Arrays.asList(array);
        }
        return Collections.emptyList();
    }
}
