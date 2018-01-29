package bd.com.elites.ab.contactsyncapp.http;


public class HttpResponse {

    String responseBody = null;
    int responseCode = - 1;


    public HttpResponse() {

    }

    public HttpResponse(String responseBody, int responseCode) {
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return responseBody;
    }
}
