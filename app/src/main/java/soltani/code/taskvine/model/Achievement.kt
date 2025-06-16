package soltani.code.taskvine.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Achievement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isAchieved: Boolean,

    )