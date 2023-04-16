package roskar.kristjan.norma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_table")
data class Month (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "month") val month: String
    )
