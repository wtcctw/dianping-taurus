package com.dp.bigdata.taurus.springmvc.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

public class WebResult {
//	public static final String MESSAGE_BEAN_REF = "messageSource";
	private Boolean hasError = false;
	private String errorMsg = "";
	private String message = "";
	private Integer status = 200;
	private Date requestTime = new Date();
	private Map<String, Object> result = new HashMap<String, Object>();
	private Locale locale = Locale.getDefault();
	//private MessageSource messageSource = null;

	public WebResult() {

	}

	public WebResult(HttpServletRequest request) {
		locale = request.getLocale();
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		if (null == context) {
			context = RequestContextUtils.getWebApplicationContext(request);
		}
//		if (null != context) {
//			Object _messageSource = context.getBean(MESSAGE_BEAN_REF);
//			if (null != _messageSource
//					&& _messageSource instanceof MessageSource) {
//				messageSource = (MessageSource)_messageSource; 
//			}
//		}
	}

	public Boolean getHasError() {
		return hasError;
	}

	public void setHasError(Boolean hasError) {
		this.hasError = hasError;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
//		if (null == this.messageSource) {
//			setPlanErrorMsg(errorMsg);
//		} else {
//			String bundledMessage = messageSource.getMessage(errorMsg, null,
//					locale);
//			if (null != bundledMessage) {
//				this.errorMsg = bundledMessage;
//			} else {
//				this.errorMsg = errorMsg;
//			}
//		}
		this.errorMsg = errorMsg;
	}

//	public void setPlanErrorMsg(String msg) {
//		this.errorMsg = msg;
//	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

//	public MessageSource getMessageSource() {
//		return messageSource;
//	}
//
//	public void setMessageSource(MessageSource messageSource) {
//		this.messageSource = messageSource;
//	}

	public void addAttr(String key, Object value) {
		this.result.put(key, value);
	}
}