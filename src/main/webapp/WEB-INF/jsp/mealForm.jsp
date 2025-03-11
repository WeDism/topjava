<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="meal.title"/></title>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <hr>
    <h2>${pageContext.request.contextPath.contains('add') ? 'Create meal' : 'Edit meal'}</h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action=${pageContext.request.contextPath.contains('add') ? 'create' : 'update'}>
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal_form.datetime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal_form.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal_form.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="meal_form.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="meal_form.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
