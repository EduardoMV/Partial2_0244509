package up.edu;

import java.util.Map;

public abstract class ApiCaller {
    String url;
    String method;
    Map<String, String> headers;
    String body;


    public ApiCaller(String url, String method, Map<String, String> headers, String body) {
        setUrl(url);
        setMethod(method);
        setHeaders(headers);
        setBody(body);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
