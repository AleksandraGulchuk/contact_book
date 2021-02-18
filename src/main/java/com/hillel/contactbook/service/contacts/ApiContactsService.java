package com.hillel.contactbook.service.contacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.contacts.ContactWorker;
import com.hillel.contactbook.dto.contact.ContactRequest;
import com.hillel.contactbook.dto.contact.ContactResponse;
import com.hillel.contactbook.dto.contact.SearchByNameContactRequest;
import com.hillel.contactbook.dto.contact.SearchByValueContactRequest;
import com.hillel.contactbook.factory.httprequest.HttpRequestFactory;
import com.hillel.contactbook.service.users.UserService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ApiContactsService implements ContactsService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private UserService apiUserService;
    @PropAnnotation("api.base-uri")
    private String baseUri;
    private final ContactWorker contactWorker;
    private final HttpRequestFactory httpRequestFactory;


    @Override
    public void setUserService(UserService userService) {
        this.apiUserService = userService;
    }

    @Override
    public UserService getUserService() {
        return apiUserService;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        HttpRequest httpRequest = httpRequestFactory
                .createAuthGetRequest(baseUri + "/contacts", apiUserService.getToken());

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
            HttpRequest httpRequest = httpRequestFactory
                    .createAuthPostRequest(request, baseUri + "/contacts/add", apiUserService.getToken());

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(httpResponse.body(), ContactResponse.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Contact> searchByName(String nameStartsWith) {
        List<Contact> contacts = new ArrayList<>();
        try {
            String request = objectMapper.writeValueAsString(new SearchByNameContactRequest(nameStartsWith));
            HttpRequest httpRequest = httpRequestFactory
                    .createAuthPostRequest(request, baseUri + "/contacts/find", apiUserService.getToken());

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
            HttpRequest httpRequest = httpRequestFactory
                    .createAuthPostRequest(request, baseUri + "/contacts/find", apiUserService.getToken());

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
        return apiUserService.hasToken();
    }

}
