package com.hillel.contactbook.factory.httprequest;

import java.net.http.HttpRequest;

public interface HttpRequestFactory {

    HttpRequest createGetRequest(String uri);

    HttpRequest createAuthGetRequest(String uri, String token);

    HttpRequest createPostRequest(String request, String uri);

    HttpRequest createAuthPostRequest(String request, String uri, String token);

}
