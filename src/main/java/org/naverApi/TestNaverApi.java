// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.naverApi;

// NaverApi TEST
public class TestNaverApi {
    public static void main(String[] args) {
        SearchResult[] searchResults = NaverApi.searchShopping("로지텍 k380");
        if (searchResults != null) {
            for (SearchResult product : searchResults) {
                System.out.println(product);
            }
        } else {
            System.out.println("검색 결과를 가져오지 못했습니다.");
        }
    }
}
