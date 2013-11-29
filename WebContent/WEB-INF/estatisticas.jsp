<%@include file="taglib.jsp" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
            $(document).ready(function(){
                $("#graf-pares").highcharts({
                    chart: {
                        type: 'bar'
                    },
                    title: {
                        text: 'Divisão por paridade'
                    },
                    xAxis: {
                        categories: ['6 ímpares', '1 par e 5 ímpares', '2 pares e 4 ímpares', '3 pares e 3 ímpares', '4 pares e 2 ímpares', '5 pares e 1 ímpar', '6 pares']
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Total de números'
                        }
                    },
                    legend: {
                        backgroundColor: '#FFFFFF',
                        reversed: true
                    },
                    plotOptions: {
                        series: {
                            stacking: 'normal'
                        }
                    },
                    series: [{
                        name: 'Quantidade de resultados',
                        data: ${pares}
                    }]
                });

                $("#graf-total-aparecimento").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Número de vezes sorteado em concursos'
                    },
                    xAxis: {
                        categories: ${numeros}
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Total de vezes sorteado'
                        }
                    },
                    legend: {
                        backgroundColor: '#FFFFFF',
                        reversed: true
                    },
                    plotOptions: {
                        series: {
                            stacking: 'normal'
                        }
                    },
                    series: [{
                        name: 'Quantidade de números',
                        data: ${qtdNumeros}
                    }]
                });
                
                $("#graf-acertos").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Acertos em apostas'
                    },
                    xAxis: {
                        categories: ${acertos}
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Total de acertos'
                        }
                    },
                    legend: {
                        backgroundColor: '#FFFFFF',
                        reversed: true
                    },
                    plotOptions: {
                        series: {
                            stacking: 'normal'
                        }
                    },
                    series: [{
                        name: 'Apostas',
                        data: ${qtdAcertos}
                    }]
                });
                
            });
        </script>
    </head>
    <body>
        <div id="graf-pares">
        </div>
        <div id="graf-total-aparecimento">
        </div>
        <div id="graf-acertos">
        </div>
    </body>
</html>
