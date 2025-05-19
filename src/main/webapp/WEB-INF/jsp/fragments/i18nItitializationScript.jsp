<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    const i18n = {}; // https://learn.javascript.ru/object
    i18n["addTitle"] = '<spring:message code="meal.add"/>';
    i18n["editTitle"] = '<spring:message code="meal.edit"/>';
    i18n["endDate.isEarlier"] = '<spring:message code="error.endDate.isEarlier"/>';
    i18n["startDate.isLater"] = '<spring:message code="error.startDate.isLater"/>';
    i18n["endTime.isEarlier"] = '<spring:message code="error.endTime.isEarlier"/>';
    i18n["startTime.isLater"] = '<spring:message code="error.startTime.isLater"/>';

    <c:forEach var="key" items='${["common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"]}'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>
