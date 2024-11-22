🛍️ 쇼핑몰 프로젝트: FOURBLOCK
FOURBLOCK은 네이버 Open API와 데이터베이스를 활용한 쇼핑몰 프로젝트입니다. 검색, 장바구니, 회원 관리와 같은 주요 기능을 통해 사용자들에게 최적의 쇼핑 경험을 제공합니다.

📌 목차
팀원 소개 및 역할
사용된 서비스
데이터베이스 설계
플로우차트
코드별 주요 기능
구동 이미지 및 서비스 링크
👥 팀원 소개 및 역할
이름	역할	주요 담당 업무
이준환	팀장, Fullstack 개발	- DB 설계 및 통합
- cart.jsp, index.jsp 구현
- 핵심 기능 설계
이관용	데이터 분석 및 API 개발	- 네이버 데이터랩 활용
- 인기 검색어 분석
- DataLabAPI.java 구현
강유빈	DB 관리 및 자동화	- search_results 테이블 설계 및 초기화 관리
- NaverProductSaver 구현
정지원	네이버 API 및 보안 로직	- 네이버 쇼핑 검색 연동
- JSON 데이터 파싱 및 객체화
- Auth.java 구현
💻 사용된 서비스
서버 환경
클라우드 서비스: Oracle Cloud
운영체제: Ubuntu 22.04
웹서버: Apache Tomcat 9
DBMS: MySQL, MySQL Workbench
도메인: fourblock.kro.kr
개발 환경
클라우드 IDE: code-server (code2.junzzang.kro.kr)
GitHub Repository: GitHub 링크
🗄️ 데이터베이스 설계
1. users 테이블
컬럼명	데이터 타입	설명
user_id	INT (PK)	사용자 고유 ID
username	VARCHAR(50)	사용자 이름
password	VARCHAR(100)	비밀번호 해시값
email	VARCHAR(100)	이메일 주소
created_at	TIMESTAMP	가입 일자
salt	VARCHAR(24)	비밀번호 해싱 솔트값
2. cart 테이블
컬럼명	데이터 타입	설명
cart_id	INT (PK)	장바구니 고유 ID
user_id	INT (FK)	사용자 고유 ID
product_id	VARCHAR(50)	상품 고유 ID
product_name	VARCHAR(255)	상품 이름
price	INT	상품 가격
🔄 플로우차트


🔍 코드별 주요 기능
JSP 파일
index.jsp: 메인 페이지
검색 기능 및 추천 카테고리 제공
cart.jsp: 장바구니 페이지
사용자 장바구니 데이터 표시 및 관리
search.jsp: 검색 결과 페이지
네이버 쇼핑 API 호출 및 결과 저장
Java 파일
Auth.java: 사용자 인증 및 보안 처리
비밀번호 해싱 및 검증 (SHA-256)
DataLabAPI.java: 네이버 데이터랩 활용
인기 검색어 데이터 분석
NaverProductSaver.java: 검색 결과 DB 저장
상품 데이터를 DB에 저장
📸 구동 이미지 및 서비스 링크
메인 페이지: fourblock.kro.kr/index.jsp
로그인 페이지: fourblock.kro.kr/login.jsp
장바구니: fourblock.kro.kr/cart.jsp
회원가입: fourblock.kro.kr/signup.jsp
