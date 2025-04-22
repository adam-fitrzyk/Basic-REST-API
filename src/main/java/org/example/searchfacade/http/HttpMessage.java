package org.example.searchfacade.http;

import java.util.Hashtable;

public abstract class HttpMessage {

    String protocol;

    Hashtable<String, String> headers;

    byte[] body;

    public HttpMessage() {
        this.headers = new Hashtable<String, String>();
    }

    public HttpMessage(String protocol, Hashtable<String, String> headers, byte[] body) {
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

}
