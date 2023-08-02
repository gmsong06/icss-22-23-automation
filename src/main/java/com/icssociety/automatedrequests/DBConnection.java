package com.icssociety.automatedrequests;


import org.javalite.activejdbc.Base;

//connects to the database, can be opened or closed

public class DBConnection {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // private static final String DRIVER = "com.mysql.jdbc.Driver";
    // private static final String URL = "jdbc:mysql://100.26.10.112:3306/ICSSData";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/icssdata";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "P6iHQb^a6*p";

    public static void open() {
        Base.open(DRIVER, URL, USERNAME, PASSWORD); // establishes a connection to the database
    }

    public static void close() {
        Base.close(); // closes the connection to the database
    }
}