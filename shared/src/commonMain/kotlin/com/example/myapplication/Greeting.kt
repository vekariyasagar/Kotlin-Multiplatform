package com.example.myapplication

import com.example.myapplication.cache.Hello

class Greeting(databaseDriverFactory: DatabaseDriverFactory) {

    var database = Database(databaseDriverFactory)

    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }

    fun insertData(date : String, name : String, project : String) {
        println(date)
        println(name)
        println(project)
        database.addData(date,name,project)
    }

    fun updateData(id : Long, date : String, name : String, project : String) {
        database.updateData(date,name,project,id)
    }

    fun getData() : List<Hello>{
        return database.selectAll()
    }

    fun deleteData(id : Long) {
        database.deleteData(id)
    }

}