import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage
import javafx.scene.control.ScrollPane


class View : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Chart"
        val xAxis = CategoryAxis()
        val yAxis = NumberAxis()
        xAxis.label = "Wort:"
        yAxis.label = "Häufigkeit"
        val barChart = BarChart<String,Number>(xAxis, yAxis)

        barChart.title = "Debug"

        val series = XYChart.Series<String, Number>()
        //TODO("This should be outsourced into model")
        Model.items.toSortedMap().filter {
            it.value > 2 && !Model.bansWord.contains(it.key)
        }.forEach { (key, value) ->
            series.data.add(XYChart.Data<String,Number>(key, value))

        }

        barChart.data.add(series)
        barChart.barGap = 30.0
        barChart.categoryGap = 10.0
        barChart.minHeight = 600.0
        barChart.minWidth = 3200.0
        barChart.isAlternativeColumnFillVisible = true
        barChart.autosize()

        val scrollPane = ScrollPane()
        scrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
        scrollPane.content = barChart
        val scene = Scene(scrollPane,800.0,600.0)
        primaryStage?.scene = scene
        primaryStage?.show()

    }

    fun main() {
        launch()
    }
}