package roskar.kristjan.norma.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import roskar.kristjan.norma.model.Month

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(month: Month)


}