<%@include file="/WEB-INF/taglib.jsp"%>

<html>
<head>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".btn").button();
        });
    </script>
<head>

<c:choose>
    <c:when test="${not empty permissaoNegada}">
        <h1>Sem permissão para acesso ao conteúdo solicitado.</h1>
    </c:when>
    <c:when test="${not empty erroLogin}">
        <h1>Não foi possível efetuar o login com os dados fornecidos.</h1>
    </c:when>
    <c:when test="${not empty erroServidorLogin}">
        <h1>Houve um erro no servidor durante o processo de login. O administrador foi avisado.</h1>
    </c:when>
    <c:when test="${not empty logout}">
        <h1>Você se desconectou com sucesso.</h1>
    </c:when>
    <c:when test="${not empty cadastro}">
        <h1>Cadastro efetuado com sucesso.</h1>
    </c:when>
    <c:when test="${not empty erroCadastro}">
        <h1>Erro de cadastro no servidor. O administrador foi avisado.</h1>
    </c:when>
    <c:otherwise>
        <h1>Seja bem vindo, faça o seu login ou cadastro.</h1>
    </c:otherwise>
</c:choose>

<form id="login_form" action="/login" method="POST">
    <input class="ui-corner-all" type="text" name="email" placeholder="email" />
    <input class="ui-corner-all" type="password" name="pword" placeholder="password" />
    <input class="ui-corner-all btn" type="submit" id="confirma_login" value="Enviar" />
</form>

<a href="/cadastro"><button class="btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="cadastro_btn">Cadastre-se</button></a>
