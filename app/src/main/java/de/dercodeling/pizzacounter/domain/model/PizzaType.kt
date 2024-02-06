package de.dercodeling.pizzacounter.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PizzaType(
    @PrimaryKey
    val name: String,
    val quantity: Int
)
