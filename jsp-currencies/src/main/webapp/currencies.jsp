<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>
<header class="header">
    <div class="container">
        <h1>Currencies</h1>
        <c:forEach var="cur" items="${currencies.getCurrencies()}">
            <p><c:out value="${cur}"/></p>
        </c:forEach>
    </div>
</header>
</body>