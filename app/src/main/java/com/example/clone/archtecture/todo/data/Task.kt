package com.example.clone.archtecture.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task constructor(
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "description") var description: String = "",
        @ColumnInfo(name = "completed") var isComplete: Boolean = false,
        @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {
    val a  = Result.Success(1)

    val titleForList: String
        get() = if(title.isNotEmpty()) title else description

    val isActive
        get() = !isComplete

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()

}