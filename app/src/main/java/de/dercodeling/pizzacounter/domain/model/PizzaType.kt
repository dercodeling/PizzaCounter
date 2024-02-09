package de.dercodeling.pizzacounter.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizzaType(
    @PrimaryKey
    val name: String,
    val quantity: Int,
    @ColumnInfo(name = "notes", defaultValue = "")
    val notes: String = ""
)
