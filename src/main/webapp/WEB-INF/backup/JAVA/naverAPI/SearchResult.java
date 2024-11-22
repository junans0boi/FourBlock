// 컴퓨터시스템공학과 2-B 202345047 정지원
package org.naverApi;

// 네이버 쇼핑 검색결과 전용
public class SearchResult {
    public String title;
    public String link;
    public String image;
    public String lprice;
    public String hprice;
    public String mallName;
    public String productId;
    public String productType;

    @Override
    public String toString() {
        return "Title: " + title + "\nLink: " + link + "\nImage: " + image +
                "\nLow Price: " + lprice + "\nHigh Price: " + hprice +
                "\nMall Name: " + mallName + "\nProduct ID: " + productId +
                "\nProduct Type: " + productType + "\n";
    }
}
