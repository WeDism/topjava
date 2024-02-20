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
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <с:forEach items="${requestScope.meals}" var="meal" varStatus="status">
        <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color:${meal.excess ? "red" : "green"}">
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="mealDateTime" type="date"/>
            <td><fmt:formatDate value="${mealDateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </с:forEach>
</table>
<br>
<a href="${pageContext.request.contextPath}/meals?action=create">
    <button type="button">Create new meal</button>
</a>
</body>
</html>