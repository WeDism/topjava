<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="с" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meal</h2>
<form action="${pageContext.request.contextPath}/edit_meal" method="post">
    <table>
        <tr>
            <td><label for="datetime">Datetime:</label></td>
            <td><input id="datetime" name="datetime" type="datetime-local" value="${requestScope.mealTo.dateTime}"/>
            </td>
        </tr>
        <tr>
            <td><label for="description">Description:</label></td>
            <td><input id="description" name="description" type="text" value="${requestScope.mealTo.description}"/></td>
        </tr>
        <tr>
            <td><label for="calories">Calories:</label></td>
            <td><input id="calories" name="calories" type="text" value="${requestScope.mealTo.calories}"/></td>
        </tr>
        <tr>
            <td>
                <button>Save</button>
                <label hidden for="id"></label>
                <input hidden id="id" name="id" value="${requestScope.mealTo.id}"/>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/meals" method="get">
                    <button>Cancel</button>
                </form>
            </td>
        </tr>
    </table>
</form>
</body>
</html>