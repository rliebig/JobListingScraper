import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage
import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Popup
import models.Sentence
import java.io.File
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class WorldCloud : Application() {
    private var primaryStage : Stage? = null
    private var canvas : Canvas? = null


    fun main() {
        launch()
    }

    private fun checkCollusion(shapesList : MutableList<Text>, text : Text) : Boolean {
        for (otherText in shapesList) {
            if (otherText.intersects(text.boundsInLocal)) {
                return true
            }
        }

        return false
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Cellular Automata Tests"

        val root = Group()
        val zoomProperty = SimpleDoubleProperty(1.0)


        val scrollPane = ScrollPane()

        val pointSize = 2.0
        val limit = 200
        var radius : Double = 10.0

        val sampleData = Model.items

        val pointData = HashMap<Int, String>()
        for (sampleDatum in sampleData) {
            val key = sampleDatum.key
            val value = sampleDatum.value
            pointData[value] = key
        }

        val sortedData = pointData


        val shapesList = mutableListOf<Text>()
        sampleData.forEach { (t, u) ->
            val startingX = Random.nextDouble(100.0,1600.0)
            val startingY = Random.nextDouble(100.0,1600.0)

            //place text there
            val text : Text = Text(startingX, startingY, t)
            text.font = Font.font("Verdana", u.toDouble() / zoomProperty.get())

            zoomProperty.addListener(InvalidationListener {
                text.autosize()
                text.font = Font.font("Verdana", u.toDouble() / zoomProperty.get())
            })

            text.fill = Color.color(Math.random(), Math.random(), Math.random())
            val popup = Popup()
            text.onMouseEntered = EventHandler {
                val vbox = VBox()
                vbox.opacity = 0.8
                vbox.style = "-fx-background-color: rgba(211,211,211,80);"
                val set = Sentence(text.text)
                set.getSentences().forEach {
                    val hbox = HBox()
                    val firstOccurance = it.split(text.text)
                    hbox.children.add(Text(firstOccurance[0]))
                    val keyword = Text(text.text)
                    keyword.fill = Color.MEDIUMVIOLETRED
                    hbox.children.add(keyword)
                    hbox.children.add(Text(firstOccurance[1]))
                    vbox.children.add(hbox)
                }
                popup.content.add(vbox)
                popup.show(primaryStage)
            }

            text.onMouseExited = EventHandler {
                popup.hide()
            }

            text.onMouseClicked = EventHandler { event ->
                popup.hide()
                val item = (event.source as Text).text
                val dialog = Stage()
                dialog.initModality(Modality.APPLICATION_MODAL)
                dialog.initOwner(primaryStage)
                val dialogBox = VBox()

                //processing algorithm.
                val file = File(Configuration.WebPageDirectory + "/webpage.txt")
                file.readLines().forEach {
                    line ->
                    println(line)
                    try {
                        val string = line.split(";")[0]
                        val searchString = line.split(";")[2]
                        println(searchString)
                        if(searchString.contains(item)) {
                            val link = Hyperlink()
                            link.setText(string)
                            link.onAction = EventHandler {
                                val rt = Runtime.getRuntime()
                                println(string)
                                rt.exec("open $string")
                            }
                            dialogBox.children.add(link)
                        }
                    } catch(e : Exception) {
                        println(e.toString())
                    }
                }

                val dialogScene = Scene(dialogBox, 600.0, 600.0)
                dialog.scene= dialogScene
                dialog.show()
            }


            if (checkCollusion(shapesList, text)) {
                for (i in 0..limit) {
                    val theta = (6.0 * Math.PI * i.toDouble()) / limit.toDouble()
                    val x = startingX + radius * cos(theta)
                    val y = startingY + radius * sin(theta)
                    //canvas?.graphicsContext2D?.fillOval(x,y, pointSize,pointSize)
                    radius += 0.1


                    text.x = x
                    text.y = y

                    if (!checkCollusion(shapesList, text)) {
                        println("found new solution")
                        break
                    }
                }
            }


            //graphicsContext.font = Font.font("Tahoma", FontWeight.SEMI_BOLD, 10.0);
            shapesList.add(text)
            //intersect check
        }

        val listGroup = Group()
        shapesList.forEach {
            text ->
            listGroup.children.add(text)
        }

        scrollPane.content = listGroup
        scrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
        scrollPane.vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
        scrollPane.minHeight = 1600.0
        scrollPane.minWidth = 1600.0
        scrollPane.onZoom = EventHandler {
            zoomProperty.set(it.totalZoomFactor)
        }
        this.primaryStage = primaryStage
        primaryStage?.height = 800.0
        primaryStage?.width = 800.0
        primaryStage?.scene = Scene(scrollPane)
        primaryStage?.show()
    }
}
