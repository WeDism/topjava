<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>
<form action="${pageContext.request.contextPath}/meals" method="post">
    <table>
        <tr>
            <td><label for="dateTime">Datetime:</label></td>
            <td><input id="dateTime" name="dateTime" type="datetime-local" value="${requestScope.meal.dateTime}"/>
            </td>
        </tr>
        <tr>
            <td><label for="description">Description:</label></td>
            <td><input id="description" name="description" type="text" value="${requestScope.meal.description}"/></td>
        </tr>
        <tr>
            <td><label for="calories">Calories:</label></td>
            <td><input id="calories" name="calories" type="number" value="${requestScope.meal.calories}"/></td>
        </tr>
        <tr>
            <td>
                <button>Save</button>
                <label hidden for="id"></label>
                <input hidden id="id" name="id" value="${requestScope.meal.id}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/meals">
                    <button type="button">Cancel</button>
                </a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>