// 컴퓨터시스템공학과 2-B 202345062 이관용
package org.naverApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DataLabAPI {

    public static void main(String[] args) {
        String clientId = "kZCLA0Nv1SsJLGGiqWnb"; // 애플리케이션 클라이언트 아이디
        String clientSecret = "4JZWYBIww7"; // 애플리케이션 클라이언트 시크릿

        String apiUrl = "https://openapi.naver.com/v1/datalab/search";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        requestHeaders.put("Content-Type", "application/json");

        String yesterDay = DateUtil.getDaysAgo(1); // 어제 날짜
        String twoDaysAgo = DateUtil.getDaysAgo(2); // 이틀 전 날짜

        // 모든 requestBody
        String[] requestBodies = {
                createRequestBody(twoDaysAgo, yesterDay, "패딩", "니트"),
                createRequestBody(twoDaysAgo, yesterDay, "스니커즈", "후드"),
                createRequestBody(twoDaysAgo, yesterDay, "후드집업", "코트"),
                createRequestBody(twoDaysAgo, yesterDay, "자켓", "어그"),
                createRequestBody(twoDaysAgo, yesterDay, "모자", "지갑")
        };

        // 결과를 저장할 리스트
        List<TitleAndRatio> results = new ArrayList<>();

        // API 호출 및 데이터 파싱
        for (String requestBody : requestBodies) {
            String responseBody = post(apiUrl, requestHeaders, requestBody);
            parseAndStore(responseBody, results);
        }

        // 정렬 (lastRatio 기준으로 내림차순)
        results.sort((o1, o2) -> Double.compare(o2.lastRatio, o1.lastRatio));

        // 결과 리스트에 title만 저장
        List<String> top10Titles = new ArrayList<>();
        for (TitleAndRatio entry : results) {
            top10Titles.add(entry.title);
        }

        // 출력 top10 리스트 : top10Titles
        System.out.println("top10 리스트: " + top10Titles);
    }

    private static String createRequestBody(String startDate, String endDate, String keyword1, String keyword2) {
        return "{\"startDate\":\"" + startDate + "\"," +
                "\"endDate\":\"" + endDate + "\"," +
                "\"timeUnit\":\"date\"," +
                "\"keywordGroups\":[" +
                "{\"groupName\":\"" + keyword1 + "\"," +
                "\"keywords\":[\"" + keyword1 + "\"]}," +
                "{\"groupName\":\"" + keyword2 + "\"," +
                "\"keywords\":[\"" + keyword2 + "\"]}]," +
                "\"device\":\"\"," +
                "\"ages\":[\"3\",\"4\"]," +
                "\"gender\":\"\"}";
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String requestBody) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(requestBody.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else { // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private static void parseAndStore(String responseBody, List<TitleAndRatio> results) {
        // JSON 파싱 시작
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        JsonArray responseResults = jsonObject.getAsJsonArray("results");

        for (int i = 0; i < responseResults.size(); i++) {
            JsonObject result = responseResults.get(i).getAsJsonObject();
            String title = result.get("title").getAsString();
            JsonArray data = result.getAsJsonArray("data");

            // 마지막 데이터 항목의 ratio 추출
            JsonObject lastData = data.get(data.size() - 1).getAsJsonObject();
            double lastRatio = lastData.get("ratio").getAsDouble();

            results.add(new TitleAndRatio(title, lastRatio));
        }
    }

    public static class TitleAndRatio {
        String title;
        double lastRatio;

        public TitleAndRatio(String title, double lastRatio) {
            this.title = title;
            this.lastRatio = lastRatio;
        }
    }

    public static class DateUtil {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public static String getDaysAgo(int days) {
            return LocalDate.now().minusDays(days).format(formatter);
        }
    }

    // 인기 검색어 데이터를 반환하는 메서드
    public static List<String> getTop10Keywords() {
        List<TitleAndRatio> results = new ArrayList<>();

        String yesterDay = DateUtil.getDaysAgo(1);
        String twoDaysAgo = DateUtil.getDaysAgo(2);

        // 모든 requestBody
        String[] requestBodies = {
                createRequestBody(twoDaysAgo, yesterDay, "패딩", "니트"),
                createRequestBody(twoDaysAgo, yesterDay, "스니커즈", "후드"),
                createRequestBody(twoDaysAgo, yesterDay, "후드집업", "코트"),
                createRequestBody(twoDaysAgo, yesterDay, "자켓", "어그"),
                createRequestBody(twoDaysAgo, yesterDay, "모자", "지갑")
        };

        String apiUrl = "https://openapi.naver.com/v1/datalab/search";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", "kZCLA0Nv1SsJLGGiqWnb");
        requestHeaders.put("X-Naver-Client-Secret", "4JZWYBIww7");
        requestHeaders.put("Content-Type", "application/json");

        for (String requestBody : requestBodies) {
            String responseBody = post(apiUrl, requestHeaders, requestBody);
            parseAndStore(responseBody, results);
        }

        results.sort((o1, o2) -> Double.compare(o2.lastRatio, o1.lastRatio));

        // Top 10 키워드 추출
        List<String> top10Titles = new ArrayList<>();
        for (int i = 0; i < Math.min(10, results.size()); i++) {
            top10Titles.add(results.get(i).title);
        }

        return top10Titles;
    }

}
