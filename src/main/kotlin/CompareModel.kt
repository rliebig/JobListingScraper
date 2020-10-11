import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.stage.Stage
import models.createKeywordString
import java.io.File
import java.io.FileWriter

fun main() {
    val campaigns = findAllCampaigns()
    var previous =  campaigns.get(0)

    val directory = "Diffs/${dateString()}"
    File(directory).mkdir()

    campaigns.forEach {
        val corrupted = campaignIsCorrupted(it)
        if (!corrupted) {
            previous = it
            return@forEach
        }
    }

    campaigns.forEach {
        val isCorrupted = campaignIsCorrupted(it)
        println("$it: $isCorrupted")

        if (previous != it && !isCorrupted) {
            compareCampaigns(previous, it, directory)
            previous = it
        }
    }
    val compareView = CompareView()
    compareView.main()
}

fun compareCampaigns(firstCampaignName : String,
                     secondCampaignName : String,
                     directory : String) {
    val firstModel = ModelInstance()
    val secondModel = ModelInstance()

    firstModel.readModel("$firstCampaignName/model.txt")
    secondModel.readModel("$secondCampaignName/model.txt")

    val hashMap = HashMap<String, Int>()

    for (key in firstModel.items.keys) {
         if(secondModel.items.keys.contains(key)) {
             val diff = firstModel.items[key]?.minus(secondModel.items[key]!!)
             hashMap[key] = diff!!
         } else {
             hashMap[key] = firstModel.items[key]!!
         }
    }
    val fileWriter = FileWriter("$directory/$firstCampaignName-$secondCampaignName.txt")
    for(key in hashMap.keys) {
        val string = "$key: ${hashMap[key]}"
        fileWriter.write("$string\n")
    }
}

fun createDiffHistory(directory : String) {

}

class CompareView : Application() {
    override fun start(primaryStage : Stage?) {
        val hbox = HBox()
        primaryStage?.title = "Relative Chart Menu"
        val scene = Scene(hbox, 800.0, 600.0)
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun main() {
        launch()
    }
}