package com.example.myapplication

import com.example.myapplication.cache.AppDatabase
import com.example.myapplication.cache.Hello

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun addData(date : String, name : String, project : String) {
        dbQuery.insertHello(date, name, project)
    }

    internal fun selectAll() : List<Hello> {
        return dbQuery.selectAll(::mapLaunchSelecting).executeAsList()
    }

    private fun mapLaunchSelecting(
        id: Long,
        date: String,
        name: String,
        project: String,
    ): Hello {
        return Hello(
            id = id,
            date = date,
            name = name,
            project = project
        )
    }

    internal fun deleteData(id : Long){
        return dbQuery.DeleteSingleRecord(id)
    }

    internal fun updateData(date : String, name : String, project : String, id : Long) {
        dbQuery.UpdateData(date, name, project, id)
    }

}

