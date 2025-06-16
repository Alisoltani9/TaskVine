package soltani.code.taskvine.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titleTask: String = "",
    val descriptionTask: String = "",
    val created_at: Long = System.currentTimeMillis(),
    val categoryTask: String? = null,
    var completedTask: Boolean = false,
    val reminderTime: Long? = null
)
