// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://fourblock.kro.kr:3306/FourBlock?characterEncoding=UTF-8&useUnicode=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0825";

    private static Connection connection;

    // 데이터베이스 연결을 반환하는 메서드
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
}
