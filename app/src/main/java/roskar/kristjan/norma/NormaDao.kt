package roskar.kristjan.norma

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NormaDao {
    @Query("SELECT * FROM norma_table")
    fun getAll(): List<Norma>

    @Query("SELECT * FROM norma_table WHERE date LIKE :date")
    fun findByDate(date: Int): Norma

    @Query("SELECT * FROM norma_table WHERE date like :month")
    fun findByMonth(month: String) : List<Norma>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(norma: Norma)

    @Delete
    suspend fun delete(norma: Norma)

    @Query("DELETE FROM norma_table")
    suspend fun deleteAll()
}