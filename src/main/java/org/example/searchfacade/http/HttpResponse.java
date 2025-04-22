package org.example.searchfacade.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class HttpResponse extends HttpMessage {

    private HttpStatus status;

    public HttpResponse() { }

    public HttpResponse(String protocol, HttpStatus status, Hashtable<String, String> headers, byte[] body) {
        super(protocol, headers, body);
        this.status = status;
    }

    public int getStatusCode() {
        return status.STATUS_CODE;
    } public String getReasonPhrase() {
        return status.REASON_PHRASE;
    } public void setStatus(HttpStatus status) {
        this.status = status;
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
        var response = new ByteArrayOutputStream();

        try {
            // Form status line
            String status_line = protocol +
                    " " +
                    status.STATUS_CODE +
                    " " +
                    status.REASON_PHRASE +
                    "\r\n";
            response.write(status_line.getBytes());

            // Form header field
            if (!headers.isEmpty()) {
                var header_field = new StringBuilder();
                for (var header_key : headers.keySet()) {
                    header_field.append(header_key)
                            .append(": ")
                            .append(headers.get(header_key))
                            .append("\r\n");
                }
                header_field.append("\r\n");
                response.write(header_field.toString().getBytes());
            }

            // Form body
            if (body != null) {
                response.write(body);
                response.write("\r\n".getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response.toByteArray();
    }
}
