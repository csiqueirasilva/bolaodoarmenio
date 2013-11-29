<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="taglib.jsp" %>

<script type="text/javascript">
    var numerosSelecionados = [];
    $(document).ready(function() {

        $(".canhoto-slider").slider({
            range: "min",
            min: 0,
            step: 0.05,
            max: 100,
            animate: "fast",
            stop: function(ev, ui) {
                var id = $(this).attr('id');
                var diff = 100 - $("#slider-manual").slider("value") - $("#slider-randomica").slider("value") - $("#slider-estatistica").slider("value");

                $("#label-" + id).text("(" + ui.value.toFixed(1) + "%)");

                var max = null,
                        min = null;

                $.each($(".canhoto-slider"), function(k, v) {
                    var itId = $(v).attr('id'),
                            itVal = $(v).slider("value");

                    if (itId != id) {
                        if (max == null) {
                            max = $(v);
                        } else if (max.slider("value") >= itVal) {
                            min = $(v);
                        } else {
                            min = max;
                            max = $(v);
                        }
                    }
                });

                if (diff < 0 && Math.abs(diff / 2) > min.slider("value")) {
                    diff += min.slider("value");
                    min.slider("value", 0);
                } else {
                    min.slider("value", min.slider("value") + diff / 2);
                    diff -= diff / 2;
                }

                $("#label-" + min.attr("id")).html("(" + min.slider("value").toFixed(1) + "%)");

                max.slider("value", max.slider("value") + diff);
                $("#label-" + max.attr("id")).html("(" + max.slider("value").toFixed(1) + "%)");

                diff = 100 - parseFloat($("#label-slider-randomica").text().replace(/\((\d+\.\d)+%\)/gm, "$1"))
                        - parseFloat($("#label-slider-estatistica").text().replace(/\((\d+\.\d)+%\)/gm, "$1"))
                        - parseFloat($("#label-slider-manual").text().replace(/\((\d+\.\d)+%\)/gm, "$1"));

                if (diff != 0) {
                    if (parseFloat($("#label-slider-randomica").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) != 0) {
                        $("#label-slider-randomica").text("(" +
                                (parseFloat($("#label-slider-randomica").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) +
                                        parseFloat(diff.toFixed(1))).toFixed(1) + "%)");
                    } else if (parseFloat($("#label-slider-estatistica").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) != 0) {
                        $("#label-slider-estatistica").text("(" +
                                (parseFloat($("#label-slider-estatistica").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) +
                                        parseFloat(diff.toFixed(1))).toFixed(1) + "%)");
                    } else if (parseFloat($("#label-slider-manual").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) != 0) {
                        $("#label-slider-manual").text("(" +
                                (parseFloat($("#label-slider-manual").text().replace(/\((\d+\.\d)+%\)/gm, "$1")) +
                                        parseFloat(diff.toFixed(1))).toFixed(1) + "%)");
                    }
                }
            }
        });

        $("#slider-randomica").slider("value", 100);

        $("#canhoto-criacao").tooltip();
        $("#canhoto-lista-numeros").sortable();
        $("#canhoto-lista-numeros").disableSelection();

        $(".numero-canhoto").click(function(ev) {
            $(this).toggleClass("toggle-numero-canhoto");
            var thisVal = parseInt($(this).children("span").text()),
                    idx = numerosSelecionados.indexOf(thisVal);

            if (idx != -1) {
                numerosSelecionados.splice(idx, 1);
                $("#canhoto-lista-numeros").children("li[id='" + thisVal + "']").remove();
            } else {
                numerosSelecionados.push(thisVal);
                $("#canhoto-lista-numeros").append("<li class='ui-state-default' id='" + thisVal + "'>" + thisVal + "</li>");
            }

            if (numerosSelecionados.length > 0) {
                $("#canhoto-exibe-ordem-escolha").hide();
            } else {
                $("#canhoto-exibe-ordem-escolha").show();
            }

        });

    <c:if test="${not empty bolao}">
        $("#apostas-sbmt").button()
                .click(function() {

            $("#content").load("http://${header.host}/apostas/criar",
                    {
                        id_bolao: ${bolao.id},
                        numeros: $("#canhoto-lista-numeros").sortable("toArray"),
                        qtd: {
                            randomica: $("#label-slider-randomica").text().replace(/\((\d+\.\d)+%\)/gm, "$1"),
                            estatistica: $("#label-slider-estatistica").text().replace(/\((\d+\.\d)+%\)/gm, "$1"),
                            manual: $("#label-slider-manual").text().replace(/\((\d+\.\d)+%\)/gm, "$1")
                        }
                    });
        });
    </c:if>

    <c:if test="${bolao.valorDepositado/PRECO_APOSTA < 1}">
            $("#apostas-sbmt").button("disable");
    </c:if>

            $("#adicionar_membro").button();
            $("#menu-admin").accordion({heightStyle: "content", active: false, collapsible: true});
            $("#adicionar_membro").click(function() {
                $.ajax({
                    url: "http://${header.host}/grupo/membro/adicionar",
                    type: "POST",
                    data: {id_participante: $("#nao-membros").val(), id_grupo: ${grupo.id}}
                })

                        .done(function() {
                    var user = $("#nao-membros option:selected").text(),
                            grupo = "${grupo.nome}";

                    UI.redirect(function() {
                        $("#content").load("http://${header.host}/grupo?id=${grupo.id}");
                    });

                    alert(user + " adicionado ao grupo " + grupo + " com sucesso!");
                })

                        .fail(function() {
                    alert("Falha no servidor ao inserir usuário!");
                });
            });
        });
</script>

<h2>Grupo <i>${grupo.nome}</i></h2>
Administrador: <a href="mailto:${grupo.admin.email}">${grupo.admin.nome}</a>

<h2>Membros</h2>

<table id="membros">
    <thead>
        <tr>
            <th>
                Nome
            </th>
            <th>
                Email
            </th>            
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${membros}" var="membro">
            <tr>
                <td>
                    ${membro.nome}
                </td>
                <td>
                    ${membro.email}
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<c:if test="${grupo.admin.id == usuario.id}">
    <h3>Painel de administração</h3>

    <div id="menu-admin">
        <h3>Incluir participante</h3>
        <div>
            <c:choose>
                <c:when test="${naoMembros.size() > 0}">
                    <select id="nao-membros">
                        <c:forEach items="${naoMembros}" var="naoMembro">
                            <option value="${naoMembro.id}">${naoMembro.nome}</option>
                        </c:forEach>
                    </select>

                    <button id="adicionar_membro">OK</button>
                </c:when>
                <c:otherwise>
                    <span>Impossível adicionar mais participantes a este grupo. Todos os participantes do sistema estão nesse grupo.</span>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${not empty bolao}">
            <h3>Criar apostas de bolão aberto</h3>
            <div id='canhoto-criacao'>
                <h2>Data do sorteio: <fmt:formatDate value="${bolao.concurso.dataSorteio}" pattern="dd/MM/yyyy" /></h2>

                <h2>Saldo do bolão: <fmt:formatNumber value="${bolao.valorDepositado}" type="currency" /></h2>

                <h2>Quantidade de tentativas: <fmt:formatNumber value="${bolao.valorDepositado/PRECO_APOSTA}" maxFractionDigits="0" /></h2>

                <h2>Números preferenciais:</h2>
                <table id="canhoto-mega-sena">
                    <c:forEach var="j" begin="0" end="5">
                        <tr>
                            <c:forEach var="i" begin="1" end="10">
                                <td class="numero-canhoto">[<span><fmt:formatNumber minIntegerDigits="2" value="${i+j*10}" /></span>]</td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>

                <h2 style="float: left;">Ordem de escolha dos números</h2><div style="margin-top: 1.5em; margin-left: 1.0em; float:left;" class="ui-icon ui-icon-help" title='Números mais a esquerda superior tem preferência sobre os números a direita inferior' />
                <div style="clear:both;" />
                <h3 id="canhoto-exibe-ordem-escolha">Não há números escolhidos.</h3>
                <ul id='canhoto-lista-numeros'>
                </ul>

                <div style="clear:both;" />
                <h2 style="float: left;">Distribuição de apostas</h2><div style="margin-top: 1.5em; margin-left: 1.0em; float:left;" class="ui-icon ui-icon-help" title='Como o sistema vai fazer a distribuição das apostas' />
                <div style="clear:both;" />

                <h3>Randômica <span id="label-slider-randomica">(100.0%)</span></h3>

                <div class="canhoto-slider" id="slider-randomica" />

                <h3>Estatística <span id="label-slider-estatistica">(0.0%)</span></h3>

                <div class="canhoto-slider" id="slider-estatistica" />

                <h3>Manual <span id="label-slider-manual">(0.0%)</span></h3>

                <div class="canhoto-slider" id="slider-manual" />

                <div style="margin-top: 20px;">
                    <button id="apostas-sbmt">Confirmar criação de apostas</button>
                </div>
            </div>
        </c:if>
    </div>
</c:if>