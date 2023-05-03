package roskar.kristjan.norma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "productivity_table")
data class Productivity (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "productivity_hours") var productivityHours: Double,
    @ColumnInfo(name = "working_hours") var workingHours: Double,
    //@ColumnInfo(name = "department") val department: String,
    //@ColumnInfo(name = "shift") val shift: Int
    )
