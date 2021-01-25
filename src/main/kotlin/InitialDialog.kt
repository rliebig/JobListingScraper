import javafx.scene.Scene
import javafx.stage.Stage
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.control.Alert
import javafx.scene.control.Button
import java.io.File
import javafx.scene.control.ListView
import javafx.scene.layout.HBox

fun main() {
   val initialDialog = InitialDialog()
   initialDialog.main()
}
class InitialDialog : Application() {
    fun main() {
        launch()
    }


    fun all_files() : List<String> {
        val result = ArrayList<String>()
        val file = File(".")
        val pathnames = file.list()
        return pathnames.toList()
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "JobListingScraper - Initial Dialog"
        val root = Group()
        val list = ListView<String>()
        all_files().forEach {
            list.items.add(it)
        }

        val selectButton = Button("Open this campaign")
        selectButton.onMouseClicked = EventHandler {
            val selectedIndices = list.selectionModel.selectedIndices

            if (selectedIndices.size != 1) {
                val alert = Alert(Alert.AlertType.ERROR, "Select only one data set")
                alert.show()
            } else {
                val firstNumber = selectedIndices[0]
                val firstObject = list.items[firstNumber]

                println(firstObject)
                readCampaign(firstObject)
                Model.readModel()
                val wordCloud = WorldCloud()

                wordCloud.start(primaryStage)
            }
        }

        val box = HBox(list, selectButton)
        val scene = Scene(box, 300.0, 200.0)
        primaryStage?.scene = scene
        primaryStage?.show()
   }
}