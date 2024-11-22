//202145058 강유빈
package org.naverApi;

import org.db.DatabaseManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TableClearScheduler {

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 작업 정의
        Runnable clearOldRecordsTask = () -> {
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                // 'created_at' 값이 현재 시간으로부터 24시간 경과된 레코드를 삭제
                String deleteQuery = "DELETE FROM search_results WHERE created_at < NOW() - INTERVAL 24 HOUR;";
                int rowsAffected = stmt.executeUpdate(deleteQuery);
                System.out.println("24시간 경과된 레코드를 삭제 완료! 삭제된 행 개수: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        // 자정(00:00)에 작업 실행
        long initialDelay = calculateInitialDelayForMidnight();
        scheduler.scheduleAtFixedRate(clearOldRecordsTask, initialDelay, 24, TimeUnit.HOURS);
    }

    // 현재 시간 기준으로 자정까지 남은 시간을 계산
    private static long calculateInitialDelayForMidnight() {
        long currentMillis = System.currentTimeMillis();
        long midnightMillis = currentMillis - (currentMillis % (24 * 60 * 60 * 1000)) + (24 * 60 * 60 * 1000); // 자정까지 밀리초 계산
        return (midnightMillis - currentMillis) / 1000; // 초 단위로 반환
    }
}
