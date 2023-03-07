package roskar.kristjan.norma.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import roskar.kristjan.norma.model.Productivity

@Dao
interface ProductivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productivity: Productivity)

    @Query("SELECT * FROM productivity_table WHERE date LIKE :date")
    suspend fun findByDate(date: String): LiveData<Productivity>

    @Query("SELECT * FROM productivity_table WHERE date LIKE :month")
    suspend fun findByMonth(month: String): LiveData<List<Productivity>>

    @Query("SELECT * FROM productivity_table")
    suspend fun getAllData(): LiveData<List<Productivity>>


    @Query("DELETE FROM productivity_table WHERE date like :month")
    fun deleteFromProductivityTable(month: String): Int


}