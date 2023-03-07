package roskar.kristjan.norma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productivity_table")
data class Productivity (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "productivity hours") val productivityHours: Double,
    @ColumnInfo(name = "working hours") val workingHours: Double,
    //@ColumnInfo(name = "department") val department: String,
    //@ColumnInfo(name = "shift") val shift: Int
    ) {
    @PrimaryKey(autoGenerate = true) val id: Int = 0
}