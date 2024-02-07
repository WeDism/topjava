<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="с" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
        padding: 5px;
    }
</style>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <caption>Meals</caption>
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <с:forEach items="${requestScope.meals}" var="mealTo" varStatus="status">
        <tr>
            <fmt:parseDate value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="mealToDateTime" type="date"/>
            <td ${mealTo.excess ? 'style="color: red"' : 'style="color: green"'}><fmt:formatDate value="${mealToDateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td ${mealTo.excess ? 'style="color: red"' : 'style="color: green"'}><c:out value="${mealTo.description}"/></td>
            <td ${mealTo.excess ? 'style="color: red"' : 'style="color: green"'}><c:out value="${mealTo.calories}"/></td>
            <td ${mealTo.excess ? 'style="color: red"' : 'style="color: green"'}><c:out value="${mealTo.calories}"/></td>
            <td><a href="${pageContext.request.contextPath}/edit_meal?id=${mealTo.id}">Update</a></td>
        </tr>
    </с:forEach>
</table>
</body>
</html>