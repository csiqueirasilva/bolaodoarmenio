<%@include file="taglib.jsp"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
            $(document).ready(function(){
                UI.link("#content > table > tbody > tr > td", "#content");
            });
        </script>
    </head>
    <body>
        
        <h2>Administrar grupos</h2>
        
        <c:choose>
            <c:when test="${adminGrupos.size() != 0}">
                <table class="grupos" cellpadding="0" cellspacing="0">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Saldo</th>
                    </tr>
                    <tbody>
                <c:forEach var="grupo" items="${adminGrupos}">
                    <tr>
                        <td><a value="/grupo?id=${grupo.id}">${grupo.nome}</a></td>
                        <td>${grupo.saldo}</td>
                    </tr>
                </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <h3>Não sou administrador de nenhum grupo</h3>
            </c:otherwise>
        </c:choose>
    
        <h2>Grupos do qual sou apenas membro</h2>
        
        <c:choose>
            <c:when test="${membroGrupos.size() != 0}">
                <table class="grupos" cellpadding="0" cellspacing="0">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Saldo</th>
                    </tr>
                    <tbody>
                <c:forEach var="grupo" items="${membroGrupos}">
                    <tr>
                        <td><a value="/grupo?id=${grupo.id}">${grupo.nome}</a></td>
                        <td><fmt:formatNumber value="${grupo.saldo}" type="currency" /></td>
                    </tr>
                </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <h3>Não sou apenas membro de nenhum grupo</h3>
            </c:otherwise>
        </c:choose>
    
</body>
</html>
