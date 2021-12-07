<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>
<header class="header">
    <div class="container">
        <c:url value="exchangeRates.jsp" var="displayURL">
            <c:param name="from" value="${param.from}"/>
            <c:param name="to" value="${param.to}"/>
            <c:param name="amount" value="${param.amount}"/>
        </c:url>
        <c:set var="currenciesFrom" value="${param.from}"/>
        <c:set var="currenciesTo" value="${param.to}"/>
        <c:set var="amount" value="${param.amount}"/>
        <p>
        <h1>Converting ${currenciesFrom} to ${currenciesTo} </h1>
        </p
        <p>
            <c:out value="${amount}"/>
            <c:out value="${currenciesFrom} = "/>
            <c:out value="${currencies.convert(amount,currenciesFrom,currenciesTo)}"/>
            <c:out value="${currenciesTo}"/>
        </p>
    </div>
</header>
</body>