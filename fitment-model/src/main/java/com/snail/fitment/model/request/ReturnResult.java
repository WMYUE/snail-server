package com.snail.fitment.model.request;


import java.io.Serializable;

import com.snail.fitment.common.code.CommonStatusCode;

public class ReturnResult<T> implements Serializable {

	private static final long serialVersionUID = 476412762724299172L;
	
	private String desc = null;
	private int code = 0;
	private T returnContent;

	public ReturnResult() {
		super();
	}

	public ReturnResult(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result
				+ ((returnContent == null) ? 0 : returnContent.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReturnResult other = (ReturnResult) obj;
		if (code != other.code)
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (returnContent == null) {
			if (other.returnContent != null)
				return false;
		} else if (!returnContent.equals(other.returnContent))
			return false;
		
		return true;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getReturnContent() {
		return returnContent;
	}

	public void setReturnContent(T returnContent) {
		this.returnContent = returnContent;
	}

	
	/**
	 * 将src中的数据覆盖或合并到dest list数据做合并，其他数据做覆盖
	 * 
	 * @param dest
	 * @param src
	 */
	@SuppressWarnings("unused")
	private static <T> void combineResult(ReturnResult<T> dest, ReturnResult<T> src) {
		dest.setCode(src.getCode());
		dest.setDesc(src.getDesc());
		dest.setReturnContent(src.getReturnContent());

	}
	
	public static <T> ReturnResult<T> getSuccessInstance(){
		return new ReturnResult<T>(0,"success");
	}
	
	public static <T> ReturnResult<T> getSuccessInstance(T content){
		ReturnResult<T> res = new ReturnResult<T>(0,"success");
		res.setReturnContent(content);
		return res;
	}

	public boolean isSucceed(){
		return code == 0;
	}

	public static <T> ReturnResult<T> getErrorResult(int code,String desc) {
		ReturnResult<T> result=new ReturnResult<T>();
		result.setCode(code);
		result.setDesc(desc);
		return result;
	}
	
	public static <T> ReturnResult<T> getParamsErrorResult(){
		return ReturnResult.getErrorResult(CommonStatusCode.COMMON_REQUEST_PARAM_ERROR, "普通错误");		
	}
	
	public static <T> ReturnResult<T> getParamsErrorResult(String desc){
		return ReturnResult.getErrorResult(CommonStatusCode.COMMON_REQUEST_PARAM_ERROR, desc);		
	}
	
	public static <T> ReturnResult<T> getPermissionErrorResult(String desc){
		return ReturnResult.getErrorResult(CommonStatusCode.COMMON_NO_PERMISSION, desc);		
	}
}
