package com.hillel.contactbook.service.users;

import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.dto.user.*;
import com.hillel.contactbook.users.User;
import com.hillel.contactbook.util.JdbcTemplate;
import com.hillel.contactbook.util.RowMapper;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@RequiredArgsConstructor
public class DatabaseUserService implements UserService {

    private DataSource dataSource;
    private String login;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String getToken() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT token FROM authusers WHERE login = ?;";
        Object[] params = new Object[]{login};
        return jdbcTemplate.queryOne(sql, params, resultSet -> {
            try {
                return resultSet.getString("token");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public boolean hasToken() {
        User user = getAuthUser(dataSource);
        if (user == null) return false;
        long minutesAfterAuthorization = MINUTES.between(user.getAuthorizationTime(), LocalTime.now());
        if (minutesAfterAuthorization < 55) {
            return true;
        } else {
            singOut();
            return false;
        }
    }

    @Override
    public CheckInResponse checkIn(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "INSERT INTO users(login, password, dateborn) " +
                "VALUES (?, ?, ?);";
        Object[] params = new Object[]{user.getLogin(),
                user.getPassword(),
                LocalDate.parse(user.getDateBorn(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))};
        CheckInResponse checkInResponse = new CheckInResponse();
        User duplicateUser = getDuplicateLogin(dataSource, user);
        if (duplicateUser != null) {
            checkInResponse.setStatus("error");
            checkInResponse.setError("Пользователь с логином " + duplicateUser.getLogin() + " уже существует!");
        } else {
            checkInResponse.setStatus("ok");
            jdbcTemplate.update(sql, params);
        }
        return checkInResponse;
    }

    @Override
    public SingInResponse singIn(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        SingInResponse singInResponse = new SingInResponse();
        if (!chekUser(dataSource, user)) {
            singInResponse.setStatus("error");
            singInResponse.setError("Пользователь по логину и паролю не найден!");
            return singInResponse;
        }
        if (isAuthUserExist(dataSource, user)) {
            String sql = "DELETE FROM authusers WHERE login = ?;";
            Object[] params = new Object[]{user.getLogin()};
            jdbcTemplate.update(sql, params);
        }
        String sql = "INSERT INTO authusers(login, token, authorization_time) " +
                "VALUES (?, ?, ?);";
        Object[] params = new Object[]{user.getLogin(), "12345678900987654321", LocalDateTime.now()};
        singInResponse.setStatus("ok");
        login = user.getLogin();
        jdbcTemplate.update(sql, params);
        return singInResponse;
    }

    @Override
    public void singOut() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "DELETE FROM authusers WHERE login = ?;";
        Object[] params = new Object[]{login};
        jdbcTemplate.update(sql, params);
        login = null;
    }

    @Override
    public List<User> getAllUsers() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT users.login AS login, password, dateborn, token, authorization_time " +
                "FROM users LEFT JOIN authusers on users.login = authusers.login " +
                "ORDER BY login;";
        return jdbcTemplate.query(sql, getRowMapperUser());
    }


    private User getDuplicateLogin(DataSource dataSource, User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT login FROM users WHERE login = ?;";
        Object[] params = new Object[]{user.getLogin()};
        return (User) jdbcTemplate.queryOne(sql, params, getRowMapperLogin());
    }

    private boolean chekUser(DataSource dataSource, User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT login FROM users WHERE login = ? AND  password = ?;";
        Object[] params = new Object[]{user.getLogin(), user.getPassword()};
        return jdbcTemplate.queryOne(sql, params, getRowMapperLogin()) != null;
    }

    private boolean isAuthUserExist(DataSource dataSource, User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT login, token, authorization_time FROM authusers WHERE login = ?;";
        Object[] params = new Object[]{user.getLogin()};
        return jdbcTemplate.queryOne(sql, params, getRowMapperLogin()) != null;
    }

    private User getAuthUser(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT login, token, authorization_time FROM authusers WHERE login = ?;";
        Object[] params = new Object[]{login};
        return (User) jdbcTemplate.queryOne(sql, params, getRowMapperAuthUser());
    }

    private RowMapper getRowMapperLogin() {
        return resultSet -> {
            try {
                return new User(resultSet.getString("login"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new Contact();
        };
    }

    private RowMapper getRowMapperAuthUser() {
        return resultSet -> {
            try {
                return new User(resultSet.getString("login"),
                        resultSet.getString("token"),
                        resultSet.getTimestamp("authorization_time").toLocalDateTime().toLocalTime());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new Contact();
        };
    }

    private RowMapper getRowMapperUser() {
        return resultSet -> {
            try {
                return new User(resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getTimestamp("dateborn").toLocalDateTime().toLocalDate().toString(),
                        resultSet.getString("token"),
                        getTime(resultSet)
                );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new Contact();
        };
    }

    private LocalTime getTime(ResultSet resultSet) {
        try {
            Timestamp authorizationTime = resultSet.getTimestamp("authorization_time");
            if (authorizationTime != null)
                return authorizationTime.toLocalDateTime().toLocalTime();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
