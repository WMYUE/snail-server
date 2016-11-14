package com.snail.fitment.model.userinfo;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.snail.fitment.model.BaseModel;
import com.snail.fitment.model.IIdAware;

@Component
public class LoginInfo extends BaseModel implements Serializable, IIdAware {
	private static final long serialVersionUID = -6560759493819199214L;
	
	public static final int REGISTER_STATUS_UNCHECKED = 0;
	public static final int REGISTER_STATUS_CHECKED = 1;
	
	private Long id;
	
	private String loginName;
	
	private String password;
	
	private String email;
	
	private String mobile;
	
	private long userId;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	private int checked = REGISTER_STATUS_UNCHECKED;
	
	public LoginInfo(){
		
	}

	public LoginInfo(String loginName, String password, String  email, String  mobile){
		this.loginName = loginName;
		this.password = password;
		this.email = email;
		this.mobile = mobile;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
