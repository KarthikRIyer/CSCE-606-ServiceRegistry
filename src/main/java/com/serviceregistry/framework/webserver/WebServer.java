package com.serviceregistry.framework.webserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;

public class WebServer {

    HttpServer httpServer;
    int port;

    public WebServer(Integer port) throws IOException {
        this.port = Optional.ofNullable(port).orElse(8081);
        httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        httpServer.setExecutor(null);
    }


    public void createContext(String path, HttpHandler handler) {
        httpServer.createContext(path, handler);
    }


    public void start() {
        httpServer.start();
    }

    public int getPort() {
        return port;
    }
}
