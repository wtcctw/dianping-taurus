package com.dp.bigdata.taurus.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Author   mingdongli
 * 16/4/27  下午3:02.
 */
public class ReWriteToStringModel {

    public  String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
