package models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ErrorTable : IntIdTable() {
    val url = varchar("url", 300)
    val errorMessage = varchar("errormessage",2000)
}

object Error {
    fun add(url : String, errorMessage : String) {
        transaction {
            addLogger(StdOutSqlLogger)
            ErrorTable.insert {
                it[ErrorTable.url] = url
                it[ErrorTable.errorMessage] = errorMessage
            }
        }
    }

    fun getErrors() : List<String> {
        val errors = mutableListOf<String>()
        transaction {
            val error_information = ErrorTable.selectAll()
            for (resultRow in error_information) {
                errors.add("${resultRow[ErrorTable.url]}: ${resultRow[ErrorTable.errorMessage]}")
            }
        }

        return errors
    }
}