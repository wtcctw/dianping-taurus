package com.cip.crane.restlet.shared;

import java.io.Serializable;

/**
 * Created by mingdongli on 16/5/13.
 */
public class AlertTypeDTO implements Serializable {

    private String ch_status;

    private int status;

    public AlertTypeDTO( int status, String ch_status) {
        this.ch_status = ch_status;
        this.status = status;
    }

    public String getCh_status() {
        return ch_status;
    }

    public void setCh_status(String ch_status) {
        this.ch_status = ch_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
