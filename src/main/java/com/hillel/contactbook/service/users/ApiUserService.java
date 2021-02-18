package com.hillel.contactbook.service.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.dto.user.*;
import com.hillel.contactbook.factory.httprequest.HttpRequestFactory;
import com.hillel.contactbook.users.User;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@RequiredArgsConstructor
public class ApiUserService implements UserService {

    @PropAnnotation("api.base-uri")
    private String baseUri;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final HttpRequestFactory httpRequestFactory;
    private User user;


    @Override
    public String getToken() {
        return user.getToken();
    }

    @Override
    public boolean hasToken() {
        if (user == null) return false;
        long minutesAfterAuthorization = MINUTES.between(user.getAuthorizationTime(), LocalTime.now());
        return minutesAfterAuthorization < 55;
    }

    @Override
    public CheckInResponse checkIn(User user) {
        try {
            String request = objectMapper.writeValueAsString(new CheckInRequest(user.getLogin(), user.getPassword(), user.getDateBorn()));
            HttpRequest httpRequest = httpRequestFactory
                    .createPostRequest(request, baseUri + "/register");

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), CheckInResponse.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SingInResponse singIn(User user) {
        try {
            String request = objectMapper.writeValueAsString(new SingInRequest(user.getLogin(), user.getPassword()));
            HttpRequest httpRequest = httpRequestFactory
                    .createPostRequest(request, baseUri + "/login");

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            SingInResponse singInResponse = objectMapper.readValue(httpResponse.body(), SingInResponse.class);
            if (singInResponse.getToken() != null) {
                this.user = new User(user.getLogin(), singInResponse.getToken(), LocalTime.now());
            }
            return singInResponse;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void singOut() {
        user = null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        HttpRequest httpRequest = httpRequestFactory.createGetRequest(baseUri + "/users");

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            UserResponse userResponse = objectMapper.readValue(httpResponse.body(), UserResponse.class);
            users = userResponse.getUsers();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

}
