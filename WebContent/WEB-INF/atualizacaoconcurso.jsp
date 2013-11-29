<%@include file="taglib.jsp" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Atualizar concurso</title>
    </head>
    <body>
        <h2 id="retorno-atualizacao"></h2>
        <form enctype="multipart/form-data" method="POST" action="http://${header.host}/concurso/efetivarAtualizacao" target="update">
            <div><input type="file" name="arquivo" /></div>
            <div><input type="submit" value="Enviar" /></div>
        </form>
        
        <iframe name="update" style="width: 0px; height: 0px; border: 0px; position: absolute;"/>
        
    </body>
</html>