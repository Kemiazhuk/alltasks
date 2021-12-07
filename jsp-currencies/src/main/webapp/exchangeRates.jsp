<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>
<header class="header">
    <div class="container">
        <c:url value="exchangeRates.jsp" var="displayURL">
            <c:param name="nameParam" value="${param.from}"/>
        </c:url>
        <c:set var="currenciesFor" value="${param.from}"/>
        <h1>Exchange Rates for ${currenciesFor} </h1>
        <c:forEach var="cur" items="${currencies.getExchangeRates(currenciesFor)}">
            <c:if test="${cur.key != currenciesFor}">
                <p>
                    <c:out value="1 ${currenciesFor} = "/>
                    <c:out value="${cur.key}"/>
                    <c:out value="${cur.value}"/>
                </p>
            </c:if>
        </c:forEach>
    </div>
</header>
</body>
