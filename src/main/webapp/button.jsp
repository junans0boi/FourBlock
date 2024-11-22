<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!-- button.jsp -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>데이터베이스 초기화</title>
</head>
<body>
    <h2>데이터베이스 초기화 버튼</h2>
    
    <!-- POST 방식으로 ClearDatabaseServlet을 호출 -->
    <form action="clearDatabase" method="post">
        <button type="submit">데이터베이스 초기화</button>
    </form>
</body>
</html>
