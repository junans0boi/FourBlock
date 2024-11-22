package org.naverApi;

import org.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NaverProductSaver {

    public static void saveSearchResults(int userId, String query, SearchResult[] results) {
        if (results == null || results.length == 0) {
            System.out.println("저장할 상품 데이터가 없습니다.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) { // DatabaseManager를 통해 연결
            String sql = "INSERT INTO search_results "
                       + "(title, link, image, lprice, hprice, mall_name, product_id, product_type, search_query, userid) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
                
            }
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
