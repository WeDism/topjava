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
<table>
    <tr>
        <td><label for="datetime">Datetime:</label></td>
        <td><label for="description">Description:</label></td>
        <td><label for="calories">Calories:</label></td>
    </tr>
    <tr>
        <td><input id="datetime" type="datetime-local"/></td>
        <td><input id="description" type="text"></td>
        <td><input id="calories" type="text"></td>
    </tr>
</table>
</body>
</html>