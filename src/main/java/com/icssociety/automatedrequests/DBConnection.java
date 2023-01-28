package com.icssociety.automatedrequests;


import org.javalite.activejdbc.Base;

public class DBConnection {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://100.26.10.112:3306/ICSSData";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "P6iHQb^a6*p";

    public static void open() {
        Base.open(DRIVER, URL, USERNAME, PASSWORD);
    }

    public static void close() {
        Base.close();
    }
}