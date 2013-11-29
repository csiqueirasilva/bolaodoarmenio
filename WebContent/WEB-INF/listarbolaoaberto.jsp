<%@include file="taglib.jsp" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bolões em aberto</title>
        <script type="text/javascript">
            function checkSbmt(src, ele) {
                if(src.val() && !isNaN(src.val())) {
                    ele.button("enable");
                } else {
                    ele.button("disable");
                }
            }
            
            function sbmt(id_bolao) {
                $.ajax({
                    url: "http://${header.host}/bolao/atualizar",
                    data: {id_bolao: id_bolao, valor: parseFloat($("#sbmt-txt-"+id_bolao).val())},
                    type: "POST"
                })
                
                .fail(function(){
                    alert("Falha ao enviar dados para o servidor!");
                })
                
                .done(function(){
                    UI.redirect(function(){
                       $("#content").load("http://${header.host}/bolao/listar");
                    });
                });
            }
            
            $(document).ready(function(){
               $(".submit-pagamento").button();
               $(".submit-pagamento").button("disable");
               UI.link("#table-bolao-aberto > tbody > tr > td", "#content");
               $(".onlyNumbers").keypress(function(ev) {
                    if(!String.fromCharCode(ev.keyCode).match(/\d{1}|\.{1}/g) && 
                        ev.keyCode != 8 ||
                        $(this).val().match(/\.{1}/g) != null && String.fromCharCode(ev.keyCode).match(/\.{1}/g) != null) {
                        ev.preventDefault();
                        return false;
                    }
               });
               
            });
        </script>
    </head>
    <body>
        <c:choose>
            <c:when test="${boloesAbertos.size() > 0}">
            <h2>Data do próximo sorteio: <fmt:formatDate value="${boloesAbertos[0].getConcurso().getDataSorteio()}" pattern="dd/MM/yyyy" /></h2>
            <table id="table-bolao-aberto">
                <thead>
                    <tr>
                        <th>Grupo</th>
                        <th>Número de apostas concretizadas</th>
                        <th>Saldo atual</th>
                        <th>Adicionar saldo</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${boloesAbertos}" var="bolao">
                    <tr>
                        <td>
                            <a value="/bolao?id_bolao=${bolao.id}">${bolao.getGrupo().getNome()}</a>
                        </td>
                        <td>
                           ${fn:length(bolao.listaApostas)}
                        </td>
                        <td>
                            <fmt:formatNumber value="${bolao.getValorDepositado()}" type="currency" />
                        </td>
                        <td>
                            <input id="sbmt-txt-${bolao.id}" class="onlyNumbers" onkeyup="checkSbmt($(this), $('#sbmt-btn-${bolao.id}'));" type="text" placeholder="Valor para ser adicionado" />
                            <button id="sbmt-btn-${bolao.id}" onclick="sbmt(${bolao.id});" class="submit-pagamento">OK</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:when>
            <c:otherwise>
                <h2>Não há bolões abertos para o concurso atual.</h2>
            </c:otherwise>
        </c:choose>
    </body>
</html>
