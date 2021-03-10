package com.hillel.contactbook.util;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JdbcTemplate {

    private final DataSource dataSource;

    public <T> List<T> query(String sql, Object[] params, RowMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T o = mapper.map(resultSet);
                results.add(o);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return results;
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper) {
        return query(sql, new Object[]{}, mapper);
    }


    public <T> T queryOne(String sql, Object[] params, RowMapper<T> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapper.map(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public <T> T queryOne(String sql, RowMapper<T> mapper) {
        return queryOne(sql, new Object[]{}, mapper);
    }


    public void update(String sql, Object[] params) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(String sql) {
        update(sql, new Object[]{});
    }
}
