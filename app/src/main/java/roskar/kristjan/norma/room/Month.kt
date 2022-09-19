package roskar.kristjan.norma.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_table")
data class Month(
    @PrimaryKey(autoGenerate = true) val month_id: Int?,
    @ColumnInfo(name = "mesec")  var month: Int?
)
