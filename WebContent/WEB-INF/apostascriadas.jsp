<%@include file="taglib.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aposta criada</title>
    </head>
    <body>
        <h2>Foram criadas as seguintes apostas</h2>
        <c:forEach items="${apostas}" var="aposta">
            <div>${aposta.numeros}</div>
        </c:forEach>
    </body>
</html>
