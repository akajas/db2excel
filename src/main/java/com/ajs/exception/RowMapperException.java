/** 
 * @(#)RowMapperException.java Jun 2, 2010
 * 
 * Copyright (c) 1995-2010 Wonders Information Co.,Ltd. 
 * 1518 Lianhang Rd,Shanghai 201112.P.R.C.
 * All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Wonders Group.
 * (Social Security Department). You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with Wonders Group. 
 *
 * Distributable under GNU LGPL license by gnu.org
 */

package com.ajs.exception;

/**
 * @version $Revision$ Jun 2, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class RowMapperException extends RuntimeException{
	
	public RowMapperException(String msg,Throwable cause){
		super(cause);
		this.msg=msg;
	}
	
	private	String msg;
	 
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	
}
