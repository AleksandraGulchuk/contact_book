CREATE DATABASE contactbook;

CREATE TABLE contacttypes
(
    type_id      SERIAL PRIMARY KEY,
    contact_type VARCHAR(15) NOT NULL
);

INSERT INTO contacttypes (contact_type)
VALUES ('phone'),
       ('email');

CREATE TABLE users
(
    login    VARCHAR(30) PRIMARY KEY,
    password VARCHAR(250) NOT NULL,
    dateBorn TIMESTAMP    NOT NULL
);

CREATE TABLE contacts
(
    contact_id    SERIAL PRIMARY KEY,
    contact_name  VARCHAR(80) NOT NULL,
    type_id       INT,
    contact_value VARCHAR(50),
    login         VARCHAR(30),
    CONSTRAINT contacts_contacttypes_type_id_fk
        FOREIGN KEY (type_id) REFERENCES contacttypes (type_id) ON DELETE CASCADE,
    CONSTRAINT contacts_users_login_fk
        FOREIGN KEY (login) REFERENCES users (login) ON DELETE CASCADE
);

CREATE TABLE authusers
(
    login              VARCHAR(30) PRIMARY KEY,
    token              VARCHAR(250) NOT NULL,
    authorization_time TIMESTAMP    NOT NULL
);