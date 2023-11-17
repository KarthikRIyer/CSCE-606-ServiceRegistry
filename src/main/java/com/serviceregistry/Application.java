package com.serviceregistry;

import com.serviceregistry.controller.RegistryController;
import com.serviceregistry.framework.controller.ControllerProcessor;
import com.serviceregistry.framework.webserver.WebServer;
import com.serviceregistry.registry.Registry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    private static Application instance;

    private Connection connection;
    private WebServer webServer;
    private ControllerProcessor controllerProcessor;
    private Registry registry;
    private RegistryController registryController;

    public static void initApp(String[] args) throws SQLException, IOException {
        if (Objects.isNull(instance)) {
            instance = new Application(args);
        }
    }

    public static Application getInstance() {
        return instance;
    }

    private Application(String[] args) throws IOException, SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Map<String, String> argsKeyVal = parseArgs(args);
        Integer port = Optional.ofNullable(argsKeyVal.get("port")).map(Integer::parseInt).orElse(null);
        webServer = new WebServer(port);

        registry = new Registry();
        registryController = new RegistryController(registry);

        controllerProcessor = new ControllerProcessor(webServer);
        controllerProcessor.process(registryController);

        registry.init();
    }

    public void init() {
        webServer.start();
        logger.info("Started application on port: " + webServer.getPort());
    }

    private static Map<String, String> parseArgs(String[] args) {
        if (args.length % 2 != 0)
            throw new RuntimeException("Unable to pass args. All args should be key value pairs");
        Map<String, String> argKeyVal = new HashMap<>();
        for (int i = 0; i < args.length; i+=2) {
            String key = args[i];
            if (!key.startsWith("--"))
                throw new RuntimeException("Unable to recognize argument: " + key + "\nArgument key should start with --\nEg: --port");
            key = key.replace("--", "");
            String val = args[i+1];
            argKeyVal.put(key, val);
        }
        return argKeyVal;
    }
}
