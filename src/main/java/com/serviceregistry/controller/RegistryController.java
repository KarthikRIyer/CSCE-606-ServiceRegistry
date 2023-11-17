package com.serviceregistry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serviceregistry.framework.annotation.GET;
import com.serviceregistry.framework.annotation.POST;
import com.serviceregistry.framework.annotation.RequestParam;
import com.serviceregistry.framework.controller.Controller;
import com.serviceregistry.framework.util.HttpResponse;
import com.serviceregistry.framework.util.JsonUtil;
import com.serviceregistry.registry.Registry;

public class RegistryController extends Controller {
    private Registry registry;

    public RegistryController(Registry registry) {
        this.registry = registry;
    }

    @GET(path = "/getServiceUrl")
    public HttpResponse getServiceUrl(@RequestParam("serviceName") String serviceName) {
        String url = registry.getServiceUrl(serviceName);
        return new HttpResponse(url, 200);
    }

    @POST(path = "/registerServiceUrl")
    public HttpResponse registerServiceUrl(@RequestParam("serviceName") String serviceName,
                                           @RequestParam("url") String url) {
        registry.registerServiceUrl(serviceName, url);
        return new HttpResponse("Registered", 200);
    }

    @GET(path = "/getRegistryJSON")
    public HttpResponse getRegistryJSON() throws JsonProcessingException {
        String json = JsonUtil.toJson(registry.getRegistryJSON());
        return new HttpResponse(json, 200);
    }
}
