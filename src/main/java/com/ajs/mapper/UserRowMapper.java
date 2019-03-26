

package com.ajs.mapper;


import com.ajs.util.InstanceUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @version $Revision$ Jun 2, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class UserRowMapper extends AbstractRowResolve implements RowMapper {

	public UserRowMapper(Class clazz) {
		this.clazz = clazz;
	}

	public Object mapRow(ResultSet rs, int pos) throws SQLException {
		Object dto = null;
		dto = InstanceUtils.getInstance(clazz);
		invoke(dto, rs);

		return dto;
	}

}
