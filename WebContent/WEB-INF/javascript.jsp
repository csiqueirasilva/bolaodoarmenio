<script type="text/javascript" src="/jqueryui/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="/js/highcharts.js"></script>
<script type="text/javascript">
    var UI = {
        link: function(container, target) {

            $(container).children("a").click(function(){
                var el = this;
                
                $.ajax({
                   url: "http://${header.host}/ajax/checklogin",
                   type: "POST",
                   dataType: "json"
               })

               .fail(function(){
                    alert("Erro inesperado ao verificar autenticação do usuário! Redirecionando para página inicial.");
                    window.location.replace("http://${header.host}/index");
               })

               .done(function(ret){
                    if(ret == false) {
                        alert("Você não tem permissão para ver o conteúdo solicitado!");
                        window.location.replace("http://${header.host}/index");
                    } else {
                        $(target).load($(el).attr("value"));
                    }
               });
            });

        },
        redirect: function(cb){
            $.ajax({
                url: "http://${header.host}/ajax/checklogin",
                type: "POST",
                dataType: "json"
            })

            .fail(function(){
                 alert("Erro inesperado ao verificar autenticação do usuário! Redirecionando para página inicial.");
                 window.location.replace("http://${header.host}/index");
            })

            .done(function(ret){
                 if(ret == false) {
                     alert("Você não tem permissão para ver o conteúdo solicitado!");
                     window.location.replace("http://${header.host}/index");
                 } else {
                     cb();
                 }
            });
        }
    };
</script>