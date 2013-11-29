<%@include file="taglib.jsp" %>

<%-- 
    Document   : criarbolao
    Created on : Nov 19, 2013, 8:45:10 PM
    Author     : csiqueira
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Criar bolão</title>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#btn-criar-bolao")
                    .button()
                    .click(function(){
                        $.ajax({
                            url: "http://${header.host}/bolao/registrar",
                            type: "POST",
                            data: { "iuh987yohlnJKW>N@!JKHSD": ${proximoConcurso.id},
                                "id_grupo": $("#cria-bolao-grupo").val() }
                        })
                        
                        .fail(function(){
                            alert("Falha ao criar bolão!");
                        })
                        
                        .done(function(){
                            var grupo = $("#cria-bolao-grupo").children("option:selected").html();
                            UI.redirect(function(){
                                $("#content").load("http://${header.host}/bolao/criar");
                                alert("Bolão criado para o grupo "+grupo+" com sucesso!");
                            });
                        })
                    });
            });
        </script>
    </head>
    <body>
        
        <c:choose>
            <c:when test="${fecharCriacaoBolao == true}">
                <h2>Data do próximo sorteio: <fmt:formatDate value="${proximoConcurso.dataSorteio}" pattern="dd/MM/yyyy" /></h2>

                <h2>Grupos sem bolão para o próximo sorteio</h2>
                <c:choose>
                    <c:when test="${adminGrupos.size() > 0}">
                        <select id="cria-bolao-grupo">
                            <c:forEach items="${adminGrupos}" var="grupo">
                                <option value="${grupo.id}">${grupo.nome}</option>
                            </c:forEach>
                        </select>
                        <button id="btn-criar-bolao">Criar Bolão</button>
                    </c:when>
                    <c:otherwise>
                        <h3>
                            Não há grupos disponíveis para criação de bolão
                        </h3>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <h2>Inscrições para bolão temporariamente encerradas enquanto o sorteio de <fmt:formatDate value="${proximoConcurso.dataSorteio}" pattern="dd/MM/yyyy" /> não é computado. Bolões abertos foram encerrados. Bolões com saldo tiveram suas apostas geradas.</h2>
            </c:otherwise>
        </c:choose>

    </body>
</html>
