package roskar.kristjan.norma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_table")
data class Month (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "month") val month: String,

    // total number of work days in a month
    @ColumnInfo(name = "working_Days") val workingDays: Int,

    // sum of productivity for whole month
    @ColumnInfo(name = "monthly_progress") var monthlyProgress: Double,

    // total # of hours worked in specified month to date
    @ColumnInfo(name = "worked_hours") var workedHours: Double

    )

