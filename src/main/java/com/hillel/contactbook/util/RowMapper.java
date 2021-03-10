package com.hillel.contactbook.util;

import java.sql.ResultSet;

public interface RowMapper<T> {

    T map(ResultSet resultSet);

}
