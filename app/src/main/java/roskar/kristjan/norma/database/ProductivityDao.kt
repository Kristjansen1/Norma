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
    suspend fun insert(productivity: List<Productivity>)

    @Query("SELECT * FROM productivity_table WHERE date LIKE :date")
    fun findByDate(date: String): List<Productivity>

    @Query("SELECT * FROM productivity_table WHERE date LIKE :month")
    fun findByMonth(month: String): LiveData<Productivity>

    @Query("SELECT * FROM productivity_table")
    fun getAllData(): LiveData<List<Productivity>>


    @Query("DELETE FROM productivity_table WHERE date like :month")
    fun deleteFromProductivityTable(month: String): Int


}