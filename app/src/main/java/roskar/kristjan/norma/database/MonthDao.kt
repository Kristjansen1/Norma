package roskar.kristjan.norma.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(month: Month)

    @Query("SELECT * FROM month_table WHERE month LIKE :month")
    fun getMonthData(month: String): LiveData<Month>

    @Query("UPDATE month_table SET monthly_progress=:monthlyProgress WHERE month LIKE :date")
    fun updateMonthlyProgress(date: String, monthlyProgress: Double) : Int

    @Query("UPDATE month_table SET worked_hours=:workedHours WHERE month LIKE :date")
    fun updateWorkedHours(date: String, workedHours: Double) : Int


}