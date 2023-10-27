package pl.damianujma.service.prediction

fun formatPredictionToHTMLString(predictionList: List<Prediction>): String{
    var htmlString = """
    <table style="font-family: arial, sans-serif; border-collapse: collapse; width: 100%;">
    <tr>
        <th style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>Day</th>
        <th style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>Temperature</th>
        <th style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>Description</th>
    </tr>
    """;

    for(prediction in predictionList){
        val predictionTr = """
        <tr>
        <td style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>${prediction.date}</td>
        <td style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>${prediction.temp}</td>
        <td style="border: 1px solid #dddddd;"text-align: left;padding: 8px;>${prediction.description}</td>
        </tr>
        """;
        htmlString = htmlString.plus(predictionTr)
    }

    var htmlEndString = """
    </table>
    """;
    htmlString.plus(htmlEndString)

    return htmlString;
}