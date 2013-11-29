<!DOCTYPE html>
<html>
    <head>
        <title>Cadastro</title>
        <script type='text/javascript'>
            
            function verificarEmail(){
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: 'http://${header.host}/ajax/userexists',
                        data: { email: $('input[name="email"]').val() },
                        complete: function(){
                            if ($('input[name="email"]').val()) {
                                $('#btn_verificar_disponibilidade_email')
                                .attr('disabled', false)
                                .removeClass('ui-state-disabled'); 
                            }
                            
                            if ($("#icon_email_ok").is(":visible")) {
                                $('#submit_btn')
                                    .attr('disabled', false)
                                    .removeClass('ui-state-disabled');
                            } else {
                                $('#submit_btn')
                                    .attr('disabled', true)
                                    .addClass('ui-state-disabled');
                            }
                        }
                    })
                    <% /*
                     * O efeito de load pode ser feito com as callbacks beforeSend e complete
                     */ %>
                    .done(function(ret) {
                        var o = $('input[name="email"]');
                
                        if (o.val()) {
                            if(ret == true) {
                                $("#icon_email_error").show();
                                $("#icon_email_ok").hide();
                            } else {
                                $("#icon_email_ok").show();
                                $("#icon_email_error").hide();
                            }
                        }
                    })

                    .fail(function() {
                        alert("Não foi possível verificar disponibilidade de email!");
                    });
            }// FUNCTION
            
            function verificarCampos () {
                
                $('#errList').empty();
                $('.verificar').removeClass('ipt_erro');
                $('input[name="email"]').removeClass("ipt_erro");
                                        
                if($('input[name="nome"]').val() ||
                    $('input[name="email"]').val() ||
                    $('#pword1').val() ||
                    $('#pword2').val() )
                    {

                    var submit_btn = $('#submit_btn');
                    errList = [] ;

                    $('.verificar').each(function(i,elem) {
                        if (!$(elem).val()) {
                            $(elem).addClass("ipt_erro");
                            errList.push("Campo " + $(elem).attr('placeholder') + " vazio.");
                        }
                    });

                    if (!$('input[name="email"]').val()) {
                        $("#icon_email_ok").hide();
                        $("#icon_email_error").hide();
                        $('#btn_verificar_disponibilidade_email')
                            .attr('disabled', true)
                            .addClass('ui-state-disabled');
                        $('input[name="email"]').addClass("ipt_erro");
                        errList.push("Campo email vazio.");
                    } else {
                        $('#btn_verificar_disponibilidade_email')
                            .attr('disabled', false)
                            .removeClass('ui-state-disabled');
                        $('input[name="email"]').removeClass("ipt_erro");
                    }

                    if($("#pword1").val() !== $("#pword2").val()) {
                        $("input[type='password']").addClass("ipt_erro");
                        errList.push("O campo senha e confirmação de senha não estão iguais.");
                    } else {
                        $("input[name='pword']").val($("#pword1").val());
                    }// ELSE

                    if(errList.length > 0 || $("#icon_email_error").is(":visible")) {
                        strError = '';
                        for(k in errList) {
                            strError += errList[k] + '<br />';
                        }// FOR
                        $('#errList').html(strError);
                        submit_btn.attr('disabled', true);
                        submit_btn.addClass('ui-state-disabled');
                    } else {
                        submit_btn.attr('disabled', false);
                        submit_btn.removeClass('ui-state-disabled');
                    }// ELSE
                }
            }// FUNCTION
            
            $(document).ready(function(){
                $(".btn").button();
                $('#submit_btn').attr('disabled', true)
                .addClass('ui-state-disabled');
                $('input[name="email"]')
                        .focus(function(){
                            $('#submit_btn')
                                    .attr('disabled', true)
                                    .addClass('ui-state-disabled');
                        })
                        .blur(verificarEmail)
                        .blur(verificarCampos);
                $('.verificar').bind('keyup', verificarCampos);
                $('.verificar').bind('click', verificarCampos);
                $('.verificar').bind('change', verificarCampos);
                $('#btn_verificar_disponibilidade_email').click(verificarCampos)
                .attr('disabled', true)
                .addClass('ui-state-disabled');
            });
        </script>
    </head>
    <body>
        <h1 class='ipt_error' id='errList'></h1>
        <form id="cadastro_form" action='/cadastro/efetuar' method='POST' style='margin-bottom: 20px;'>
            <input maxlength='30' name='nome' class='verificar' type='text' placeholder='nome' />
            <div>
                <input style="float:left;" maxlength='30' name='email' type='text' placeholder='email' />
                <span style="margin-top:4px; float:left;" id="icon_email_ok" class="hidden ui-icon ui-icon-circle-check"></span>
                <div id='icon_email_error' class='ui-state-error hidden' style="margin-top:4px; float:left;">
                    <span style='float:left;' class="ui-icon ui-icon-circle-close"></span>
                    <span style='float: left;'>E-mail indisponível</span>
                </div>
            </div>
            <div style="clear: both;" />
            <button onclick="return false;" class="btn" id="btn_verificar_disponibilidade_email">Verificar disponibilidade do email</button>
            <input maxlength='8' id='pword1' class='verificar' type='password' placeholder='senha' />
            <input maxlength='8' id='pword2' class='verificar' type='password' placeholder='confirmação de senha' />
            <input name='pword' type='hidden' />
            <input type='submit' value='Enviar' class="btn" id='submit_btn'/>
        </form>
        <a href='/index'>Voltar a página de login</a>
    </body>
</html>
