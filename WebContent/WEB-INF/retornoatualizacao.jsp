<%@include file="taglib.jsp" %>
<%@include file="javascript.jsp" %>

<script type="text/javascript">
    $(document).ready(function(){
        $("#retorno-atualizacao", window.parent.document).text(${sucesso} == false ? "OCORRERAM ERROS NO UPLOAD DO ARQUIVO" : "OK");
    });
</script>
