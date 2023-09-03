package es.apps.laos.mybunker

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 0 to avoid passing an id when creating this object
    val title: String?,
    val user: String?,
    val password: String?,
    val extraInfo: String?
)