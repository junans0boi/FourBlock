// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.auth;

public class User {
    private int userId;
    private String username;
    private String name;
    private String email;
    private String createdAt;

    public User(int userId, String username, String name, String email, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}