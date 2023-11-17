package com.serviceregistry;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        Application.initApp(args);
        Application application = Application.getInstance();
        application.init();
    }
}
