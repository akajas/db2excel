

package com.ajs.mapper;


import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @version $Revision$ Jun 3, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class UserRowCallbackHandler extends AbstractRowResolve implements RowCallbackHandler {

	Object dto =null;
	
	public UserRowCallbackHandler(Object dto,Class clazz){
		this.dto =dto;
		this.clazz =clazz;
	}
	
	/** 
	 * @see RowCallbackHandler#processRow(ResultSet)
	 */
	public void processRow(ResultSet rs) throws SQLException {
		invoke(dto,rs);
	}

}
