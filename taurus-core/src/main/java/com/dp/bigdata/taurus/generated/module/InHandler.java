package com.dp.bigdata.taurus.generated.module;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kirinli on 14-10-13.
 */
public  class InHandler implements TypeHandler {

    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        int [] arrParam = (int[]) parameter;
        String inString = "";
        for(int element : arrParam){
            inString = "," + element;
        }
        inString = inString.substring(1);
        ps.setString(i,inString);
    }

    @Override
    public Object getResult(ResultSet resultSet, String s) throws SQLException {
        return null;
    }

    @Override
    public Object getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public Object getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
