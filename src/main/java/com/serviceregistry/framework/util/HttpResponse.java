package com.serviceregistry.framework.util;

public class HttpResponse {
    public String responseText;
    public int responseCode;

    public HttpResponse(String responseText, int responseCode) {
        this.responseCode = responseCode;
        this.responseText= responseText;
    }
}
