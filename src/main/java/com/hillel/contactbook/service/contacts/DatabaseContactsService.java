package com.hillel.contactbook.service.contacts;

import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.contacts.Type;
import com.hillel.contactbook.dto.contact.ContactResponse;
import com.hillel.contactbook.service.users.DatabaseUserService;
import com.hillel.contactbook.service.users.UserService;
import com.hillel.contactbook.util.JdbcTemplate;
import com.hillel.contactbook.util.RowMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
public class DatabaseContactsService implements ContactsService {

    private UserService userService;
    private DataSource dataSource;

    @PropAnnotation("database.dsn")
    private String dsn;
    @PropAnnotation("database.user")
    private String user;
    @PropAnnotation("database.password")
    private String password;


    public void setDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dsn);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(8);
        config.setMinimumIdle(4);
        dataSource = new HikariDataSource(config);

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setUserService(UserService userService) {
        this.userService = userService;

    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public List<Contact> getAllContacts() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT contact_id, contact_name, contact_value, contact_type " +
                "FROM contacts JOIN contacttypes ON contacts.type_id = contacttypes.type_id " +
                "WHERE login = ? ORDER BY contact_name;";
        Object[] params = new Object[]{getLogin()};
        return jdbcTemplate.query(sql, params, getRowMapper());
    }

    @Override
    public void remove(int index) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "DELETE FROM contacts WHERE contact_id = ? AND login = ?;";
        Object[] params = new Object[]{index, getLogin()};
        jdbcTemplate.update(sql, params);
    }

    @Override
    public ContactResponse add(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "INSERT INTO contacts (contact_name, contact_value, login, type_id) " +
                "SELECT ?, ?, ?, type_id FROM contacttypes WHERE contact_type = ?;";
        Object[] params = new Object[]{
                contact.getName(),
                contact.getValue(),
                getLogin(),
                contact.getContactType().getValue()};
        Contact duplicate = getDuplicateContact(dataSource, contact);
        if (duplicate != null) {
            contactResponse.setStatus("error");
            contactResponse.setMessage(duplicate + " уже существует в телефонной книге!");
            contactResponse.setError(contactResponse.getMessage());
        } else {
            contactResponse.setStatus("ok");
            jdbcTemplate.update(sql, params);
        }
        return contactResponse;
    }


    @Override
    public List<Contact> searchByName(String nameStartsWith) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT contact_id, contact_name, contact_value, contact_type " +
                "FROM contacts JOIN contacttypes ON contacts.type_id = contacttypes.type_id " +
                "WHERE contact_name LIKE ? AND login = ? ORDER BY contact_name;";
        Object[] params = new Object[]{nameStartsWith + "%", getLogin()};
        return jdbcTemplate.query(sql, params, getRowMapper());
    }

    @Override
    public List<Contact> searchByValue(String valuePart) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT contact_id, contact_name, contact_value, contact_type " +
                "FROM contacts JOIN contacttypes ON contacts.type_id = contacttypes.type_id " +
                "WHERE contact_value LIKE ? AND login = ? ORDER BY contact_name;";
        Object[] params = new Object[]{"%" + valuePart + "%", getLogin()};
        return jdbcTemplate.query(sql, params, getRowMapper());
    }

    @Override
    public boolean hasToken() {
        return userService.hasToken();
    }

    private String getLogin() {
        DatabaseUserService databaseUserService = (DatabaseUserService) userService;
        return databaseUserService.getLogin();
    }

    private Contact getDuplicateContact(DataSource dataSource, Contact contact) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT contact_id, contact_name, contact_value, contact_type " +
                "FROM contacts JOIN contacttypes ON contacts.type_id = contacttypes.type_id " +
                "WHERE contact_name = ? AND contact_value = ?;";
        Object[] params = new Object[]{contact.getName(), contact.getValue()};
        return (Contact) jdbcTemplate.queryOne(sql, params, getRowMapper());
    }

    private RowMapper getRowMapper() {
        return resultSet -> {
            try {
                return new Contact(
                        resultSet.getInt("contact_id"),
                        resultSet.getString("contact_name"),
                        Type.valueOf(resultSet.getString("contact_type").toUpperCase()),
                        resultSet.getString("contact_value"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new Contact();
        };
    }

}

