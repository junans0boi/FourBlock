// 컴퓨터시스템공학과 2-B 202045066 이준환
package org.cart;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartUtil {

    public static int getUserId(Connection conn, String username) throws SQLException {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                throw new SQLException("User not found");
            }
        }
    }

    public static void addOrUpdateCart(Connection conn, int userId, String productId, String productName, String productPrice, int quantity, String imageUrl, String link) throws SQLException {
        String query = "INSERT INTO cart (user_id, product_id, product_name, price, quantity, image_url, link) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE quantity = quantity + ?, image_url = ?, link = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, productId);
            stmt.setString(3, productName != null ? productName : ""); // Use default empty string if null
            stmt.setInt(4, productPrice != null ? Integer.parseInt(productPrice) : 0); // Use 0 if null
            stmt.setInt(5, quantity);
            stmt.setString(6, imageUrl != null ? imageUrl : ""); // Use default empty string if null
            stmt.setString(7, link != null ? link : ""); // Use default empty string if null
            stmt.setInt(8, quantity);
            stmt.setString(9, imageUrl != null ? imageUrl : ""); // Use default empty string if null
            stmt.setString(10, link != null ? link : ""); // Use default empty string if null
            stmt.executeUpdate();
        }
    }
}
