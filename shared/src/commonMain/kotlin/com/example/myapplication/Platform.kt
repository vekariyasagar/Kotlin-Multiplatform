package com.example.myapplication

import com.squareup.sqldelight.db.SqlDriver

expect class Platform() {
    val platform: String
}

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

