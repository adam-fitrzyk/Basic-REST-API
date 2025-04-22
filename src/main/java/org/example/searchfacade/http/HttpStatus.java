package org.example.searchfacade.http;

public enum HttpStatus {

    // SUCCESSES
    SUCCESS_200_OK(200, "OK"),

    // CLIENT ERRORS
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not Allowed"),
    CLIENT_ERROR_403_FORBIDDEN(403, "Forbidden"),
    CLIENT_ERROR_404_PAGE_NOT_FOUND(404, "Page Not Found"),

    // SERVER ERRORS
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

    public final int STATUS_CODE;
    public final String REASON_PHRASE;

    HttpStatus(int STATUS_CODE, String REASON_PHRASE) {
        this.STATUS_CODE = STATUS_CODE;
        this.REASON_PHRASE = REASON_PHRASE;
    }

}
