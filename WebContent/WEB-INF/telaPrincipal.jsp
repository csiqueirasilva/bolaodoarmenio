<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bolão do Armênio</title>
<script type="text/javascript">
    $(document).ready(function(){
       $("#menu").accordion({heightStyle: "content", active: false, collapsible: true});
       UI.link("#menu > div", "#content");
    });
</script>
    
</head>
<body>
    <div id="menu">
        <h3>Grupos</h3>
        <div>
            <a value="/grupos">Meus grupos</a>
            <a value="/grupo/criar">Criar grupo</a>
        </div>
        
        <h3>Bolão</h3>
        <div>
            <a value="/bolao/listar">Em aberto</a>
            <a value="/bolao/criar">Criar bolão</a>
        </div>
        
        <h3>Concursos</h3>
        <div>
            <a value="/estatisticas">Verificar estatísticas</a>
            <c:if test="${usuario.email == 'root@bolaodoarmenio.com.br'}">
                <a value="/concurso/atualizar">Atualizar concursos</a>
            </c:if>
        </div>
    </div>
    <div id="content">
    </div>
    <div style="clear: both;"></div>
</body>
</html>