package com.hillel.contact_book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contact_book.contacts.Contact;
import com.hillel.contact_book.contacts.ContactWorker;
import com.hillel.contact_book.dto.contact.ContactRequest;
import com.hillel.contact_book.dto.contact.ContactResponse;
import com.hillel.contact_book.dto.contact.SearchByNameContactRequest;
import com.hillel.contact_book.dto.contact.SearchByValueContactRequest;
import com.hillel.contact_book.dto.user.*;
import com.hillel.contact_book.users.AuthorizedUser;
import com.hillel.contact_book.users.User;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@RequiredArgsConstructor
public class ApiContactsService implements WithAuthorizationContactsService, ContactsService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private AuthorizedUser authorizedUser;
    private final String baseUri;
    ContactWorker contactWorker = new ContactWorker();


    @Override
    public CheckInResponse checkIn(String login, String password, String dateBorn) {
        try {
            String request = objectMapper.writeValueAsString(new CheckInRequest(login, password, dateBorn));
            HttpRequest httpRequest = createWithoutAuthorizationRequest(request, "/register");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), CheckInResponse.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SingInResponse singIn(String login, String password) {
        try {
            String request = objectMapper.writeValueAsString(new SingInRequest(login, password));
            HttpRequest httpRequest = createWithoutAuthorizationRequest(request, "/login");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            SingInResponse singInResponse = objectMapper.readValue(httpResponse.body(), SingInResponse.class);
            if (singInResponse.getToken() != null) {
                authorizedUser = new AuthorizedUser(login, singInResponse.getToken(), LocalTime.now());
            }
            return singInResponse;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest createWithoutAuthorizationRequest(String request, String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUri + uri))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
    }

    @Override
    public void singOut() {
        authorizedUser = null;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/contacts"))
                .GET()
                .header("Authorization", "Bearer " + authorizedUser.getToken())
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            contacts = objectMapper.readValue(httpResponse.body(), ContactResponse.class)
                    .getContacts().stream()
                    .map(contactWorker::getContactFromContactForResponse)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void remove(int index) {
        //TODO: get api
        System.out.println("Service not provided!");
    }

    @Override
    public ContactResponse add(Contact contact) {
        try {
            String request = objectMapper.writeValueAsString(
                    new ContactRequest(
                            contactWorker.defineTypeFromValue(contact.getValue()).getValue(),
                            contact.getValue(),
                            contact.getName()));
            HttpRequest httpRequest = createAuthorizedRequest(request, "/contacts/add");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), ContactResponse.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest createAuthorizedRequest(String request, String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUri + uri))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header("Authorization", "Bearer " + authorizedUser.getToken())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
    }

    @Override
    public List<Contact> searchByName(String nameStartsWith) {
        List<Contact> contacts = new ArrayList<>();
        try {
            String request = objectMapper.writeValueAsString(new SearchByNameContactRequest(nameStartsWith));
            HttpRequest httpRequest = createAuthorizedRequest(request, "/contacts/find");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            contacts = objectMapper.readValue(httpResponse.body(), ContactResponse.class)
                    .getContacts().stream()
                    .map(contactWorker::getContactFromContactForResponse)
                    .collect(Collectors.toList())
            ;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public List<Contact> searchByValue(String valuePart) {
        List<Contact> contacts = new ArrayList<>();
        try {
            String request = objectMapper.writeValueAsString(new SearchByValueContactRequest(valuePart));
            HttpRequest httpRequest = createAuthorizedRequest(request, "/contacts/find");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            contacts = objectMapper.readValue(httpResponse.body(), ContactResponse.class)
                    .getContacts().stream()
                    .map(contactWorker::getContactFromContactForResponse)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public boolean hasToken() {
        if (authorizedUser == null) return false;
        long minutesAfterAuthorization = MINUTES.between(authorizedUser.getAuthorizationTime(), LocalTime.now());
        return minutesAfterAuthorization < 55;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/users"))
                .GET()
                .header("Accept", "application/json")
                .build();
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
