package roskar.kristjan.norma

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "norma_table")
data class Norma(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "date") var date: Int?,
    @ColumnInfo(name = "hours") var hours: Int?
    )
