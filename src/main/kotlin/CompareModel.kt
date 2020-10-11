import models.createKeywordString

fun main() {
    findAllCampaigns().forEach {
        val isCorrupted = campaignIsCorrupted(it)
        println("$it: $isCorrupted")
    }
}

fun otherFun() {
    val firstCampaignName = "2020-10-11T12-18-36.975803München-Softwareentwickler-4"
    val secondCampaignName = "2020-10-10T12-29-44.978996München-Softwareentwickler-4"

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

    for(key in hashMap.keys) {
        println("$key: ${hashMap[key]}")
    }
}