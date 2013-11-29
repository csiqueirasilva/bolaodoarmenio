<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
            $(document).ready(function(){
                $(".btn").button({disabled: true})
                .click(function(){
                    $.ajax({
                        url: 'http://${header.host}/CadGrupo',
                        type: 'POST',
                        data: {grupo: $("input[name='nomegrupo']").val()}
                    })
                    
                    .fail(function(){
                        alert("Falha ao inserir o grupo!");
                    })
                    
                    .done(function(a){
                        $('#insert_log_grupo').html('Grupo <b>'+$('input[name="nomegrupo"]').val()+'</b> criado com sucesso.');
                        $('#btn_criar_grupo').button("disable");
                        $('input[name="nomegrupo"]').val('');
                    });
                });
                    
                $("input[name='nomegrupo']").focus(function() {
                    $("#btn_criar_grupo").button("disable");
                });
                
                $("input[name='nomegrupo']").blur(function() {
                    $("#insert_log_grupo").empty();
                    $.ajax({
                        url: 'http://${header.host}/ajax/groupexists',
                        type: 'POST',
                        dataType: 'json',
                        data: {grupo: $("input[name='nomegrupo']").val()}
                    })
                    
                    .fail(function(a, err){
                        alert('Falha de conexao ao verificar disponibilidade de nome de grupo!');
                        console.log(err);
                    })
                    
                    .done(function(a){
                        if(a == true) {
                            $("#btn_criar_grupo").button("disable");
                            $("#icon_err_nome").show();
                        } else {
                            $("#btn_criar_grupo").button("enable");
                            $("#icon_err_nome").hide();
                        }
                    });

                });
            });
        </script>
    </head>
    <body>
        <span id="insert_log_grupo"></span>
        <div>
            <input placeholder="nome do grupo" style="float: left;" type="text" name="nomegrupo"></input>
            <div id="icon_err_nome" class="hidden" style="float: left;">
                <div style="float: left;" class="ui-icon ui-icon-circle-close" />
                <span style="float: right;">Este nome já está sendo utilizado.</span>
            </div>
            <div style="clear: both;" />
        </div>
       
        <button id="btn_criar_grupo" class="btn">Enviar</button>
    </body>
</html>
