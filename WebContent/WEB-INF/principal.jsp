<%@include file="taglib.jsp"%>

<html>
<head>
        <%@include file="css.jsp"%>
        <%@include file="javascript.jsp"%>

</head>
<body>
    <div id="header">
        <c:if test="${not empty participante}">
            <div style="float:left;">
        </c:if>

        <c:if test="${not empty participante}">
            <a id='linkhome' href="/principal">
        </c:if>
                <img src="../img/bola.png" />
                <h2>Bolão do Armênio</h2>
        <c:if test="${not empty participante}">
            </a>
        </c:if>
        <c:if test="${not empty participante}">
            </div>
            <div style="float:right;">
                <h4>Olá ${participante.nome}.</h4>
                <a href="/logout">Desconectar</a>
            </div>
            <div style="clear:both;"></div>
        </c:if>        
    </div>
    <div class="ui-corner-all" id="principal">
        <c:choose>
            <c:when test="${not empty page}">
                <jsp:include page="${page}" />
            </c:when>
            <c:otherwise>
                <%@include file="index.jsp"%>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>