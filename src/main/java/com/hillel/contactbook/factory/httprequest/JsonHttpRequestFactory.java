package com.hillel.contactbook.factory.httprequest;

import java.net.URI;
import java.net.http.HttpRequest;


public class JsonHttpRequestFactory implements HttpRequestFactory{

    private static final String MIME_TYPE_JSON = "application/json";
    private static final String NAME_ACCEPT = "Accept";
    private static final String NAME_CONTENT_TYPE = "Content-Type";
    private static final String NAME_AUTHORIZATION = "Authorization";

    @Override
    public HttpRequest createGetRequest(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .header(NAME_ACCEPT, MIME_TYPE_JSON)
                .build();
    }

    @Override
    public HttpRequest createAuthGetRequest(String uri, String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .header(NAME_AUTHORIZATION, "Bearer " + token)
                .header(NAME_ACCEPT, MIME_TYPE_JSON)
                .build();
    }

    @Override
    public HttpRequest createPostRequest(String request, String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header(NAME_ACCEPT, MIME_TYPE_JSON)
                .header(NAME_CONTENT_TYPE, MIME_TYPE_JSON)
                .build();
    }

    @Override
    public HttpRequest createAuthPostRequest(String request, String uri, String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .header(NAME_AUTHORIZATION, "Bearer " + token)
                .header(NAME_ACCEPT, MIME_TYPE_JSON)
                .header(NAME_CONTENT_TYPE, MIME_TYPE_JSON)
                .build();
    }
}
