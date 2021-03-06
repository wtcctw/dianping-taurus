package com.cip.crane.restlet.shared;

import java.io.Serializable;

/**
 * UserDTO
 * 
 * @author damon.zhu
 */
public class UserDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -271557894670140723L;
    private int id;
    private String name;
    private String mail;
    private String tel;
    private String group;
    private String qq;
    
    public UserDTO(){
    }

    public UserDTO(int id, String name, String mail, String tel,String qq) {
        super();
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.tel = tel;
        this.qq = qq;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getTel() {
        return tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setId(int id) {
		this.id = id;
	}

}
