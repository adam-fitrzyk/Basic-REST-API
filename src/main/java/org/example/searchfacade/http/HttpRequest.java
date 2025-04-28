package org.example.searchfacade.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest extends HttpMessage {

    private String method;
    private String target;
    private String parameters;

    private final Logger logger;

    public HttpRequest() { this.logger = LoggerFactory.getLogger(HttpRequest.class); }

    public HttpRequest(String method, String target, String parameters, String protocol, Hashtable<String, String> headers, byte[] body) {
        super(protocol, headers, body);
        this.method = method;
        this.target = target;
        this.parameters = parameters;
        this.logger = LoggerFactory.getLogger(HttpRequest.class);
    }

    public String getMethod() {
        return method;
    } public void setMethod(String method) {
        this.method = method;
    } public String getTarget() {
        return target;
    } public void setTarget(String target) {
        this.target = target;
    } public String getParameters() {
        return parameters;
    } public void setParameters(String parameters) {
        this.parameters = parameters;
    } public String getProtocol() {
        return protocol;
    } public void setProtocol(String protocol) {
        this.protocol = protocol;
    } public Hashtable<String, String> getHeaders() {
        return headers;
    } public void setHeaders(Hashtable<String, String> headers) {
        this.headers = headers;
    } public byte[] getBody() {
        return body;
    } public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public byte[] getBytes() {
        var request = new ByteArrayOutputStream();

        try {
            // Form request line
            String request_line = method +
                    " " +
                    target +
                    "?" +
                    parameters +
                    " " +
                    protocol +
                    "\r\n";
            request.write(request_line.getBytes());

            // From header field
            if (!headers.isEmpty()) {
                var header_field = new StringBuilder();
                for (var header_key : headers.keySet()) {
                    header_field.append(header_key)
                            .append(": ")
                            .append(headers.get(header_key))
                            .append("\r\n");
                }
                header_field.append("\r\n");
                request.write(header_field.toString().getBytes());
            }

            // Form body
            if (body != null) {
                request.write(body);
                request.write("\r\n".getBytes());
            }
        }
        catch (IOException e) {
            logger.error("Unable to convert HttpRequest into binary", e);
        }

        return request.toByteArray();
    }

}
