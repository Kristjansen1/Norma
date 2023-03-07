package roskar.kristjan.norma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_table")
data class Month (
    @ColumnInfo(name = "month") val month: String
    ){
    @PrimaryKey(autoGenerate = true) val id: Int = 0
}