package org.naverApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NaverProductSaver {

    private static final String DB_URL = "jdbc:mysql://fourblock.kro.kr:3306/FourBlock?characterEncoding=UTF-8&useUnicode=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0825";

    public static void saveSearchResults(int userId, String query, SearchResult[] results) {
        if (results == null || results.length == 0) {
            System.out.println("저장할 상품 데이터가 없습니다.");
            return;
        }
    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            String sql = "INSERT INTO search_results "
                       + "(title, link, image, lprice, hprice, mall_name, product_id, product_type, search_query, userid) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
            PreparedStatement stmt = conn.prepareStatement(sql);
    
            for (SearchResult result : results) {
                try {
                    // 각 파라미터를 정확히 매핑
                    stmt.setString(1, result.title);
                    stmt.setString(2, result.link);
                    stmt.setString(3, result.image);
                    
                    // lprice와 hprice를 적절히 변환
                    stmt.setInt(4, parsePrice(result.lprice));
                    stmt.setInt(5, parsePrice(result.hprice));
                    
                    stmt.setString(6, result.mallName);
                    stmt.setString(7, result.productId);
                    stmt.setString(8, result.productType);
                    stmt.setString(9, query);
                    stmt.setString(10, String.valueOf(userId)); // userId를 문자열로 변환
    
                    stmt.addBatch(); // 배치 추가
                } catch (SQLException e) {
                    System.out.println("상품 저장 중 오류 발생: " + result.toString());
                    e.printStackTrace();
                }
            }
    
            int[] resultCounts = stmt.executeBatch(); // 배치 실행
            System.out.println(resultCounts.length + "개의 상품이 저장되었습니다.");
    
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("데이터베이스 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
    
    // 가격 변환 헬퍼 메서드
    private static int parsePrice(String price) {
        try {
            if (price != null && !price.isEmpty()) {
                return Integer.parseInt(price);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price: " + price);
        }
        return 0; // 유효하지 않은 가격은 0으로 처리
    }
}    