// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.naverApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NaverApi {
    private static final String CLIENT_ID = "3ckoTwx7lIvpPxOWSM0P";
    private static final String CLIENT_SECRET = "WJunB_PglA";
    private static final String URL_SHOPPING = "https://openapi.naver.com/v1/search/shop.json";

    private static final Gson gson = new Gson();

    public static SearchResult[] searchShopping(String query, Integer display, Integer start, String sort, String filter, String exclude) {
        try {
            StringBuilder apiUrl = new StringBuilder(URL_SHOPPING);
            apiUrl.append("?query=").append(URLEncoder.encode(query, "UTF-8"));

            if (display != null) apiUrl.append("&display=").append(display);
            if (start != null) apiUrl.append("&start=").append(start);
            if (sort != null) apiUrl.append("&sort=").append(sort);
            if (filter != null) apiUrl.append("&filter=").append(filter);
            if (exclude != null) apiUrl.append("&exclude=").append(exclude);

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl.toString()).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JsonObject response = gson.fromJson(reader, JsonObject.class);
                reader.close();

                return gson.fromJson(response.getAsJsonArray("items"), SearchResult[].class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // query(검색어) 값만 전달
    public static SearchResult[] searchShopping(String query) {
        return searchShopping(query, 100, 1, "sim", null, null);
    }

    // query(검색어), display(가져올 행의 수), start(시작 지점), sort(정렬 옵션) 전달
    public static SearchResult[] searchShopping(String query, Integer display, Integer start, String sort) {
        return searchShopping(query, display, start, sort, null, null);
    }
}
