<%@include file="taglib.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h2>Grupo: ${bolao.grupo.nome}</h2>
        
        <h2>Saldo: <fmt:formatNumber value="${bolao.valorDepositado}" type="currency" /></h2>
        
        <h2>Data do concurso: <fmt:formatDate value="${bolao.getConcurso().getDataSorteio()}" pattern="dd/MM/yyyy" /></h2>
        
        <h2>Apostas para este bolão:</h2>
        
        <c:choose>
            <c:when test="${fn:length(bolao.listaApostas) > 0}">
                <c:forEach items="${bolao.listaApostas}" var="aposta">
                    <div>${aposta.numeros}</div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                Não há apostas para este bolão.
            </c:otherwise>
        </c:choose>        
    </body>
</html>
