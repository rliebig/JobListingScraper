import models.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    //DbConnection.pruneDatabase()
    //DbConnection.initializeDatabase()
    startExposed()
}

object Keywords : IntIdTable() {
    //val id = integer("id").uniqueIndex()
    val word = varchar("word", 40)
    val count = integer("count")
    val sentences = reference("sentences",Sentences)
}

class Keyword(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Keyword>(Keywords)
    var word by Keywords.word

}

object Sentences : IntIdTable() {
    val keyword = reference("keyword",Keywords)
    val text = varchar("text", 300)

}






fun startExposed() {
    Database.connect("jdbc:sqlite:${Configuration.databaseFile}")
    transaction {
        addLogger (StdOutSqlLogger)
        SchemaUtils.create(Keywords)
        SchemaUtils.create(Sentences)
//        Keyword.insert {
//            it[word] = "test"
//            it[count] = 2
//        }
    }
}