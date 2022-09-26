package roskar.kristjan.norma.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "norma_table")
data class Norma(
    @PrimaryKey(autoGenerate = true) val norma_id: Int?,
    @ColumnInfo(name = "datum") var norma_date: String?,
    @ColumnInfo(name = "normaUre") var normaHours: Int?,
    @ColumnInfo(name = "delovneUre") var workingHours: Int?,
    @ColumnInfo(name = "delovnoMesto") var workplace: String?

)
