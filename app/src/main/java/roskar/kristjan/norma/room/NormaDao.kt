package roskar.kristjan.norma.room

import androidx.room.*
import roskar.kristjan.norma.room.Norma

@Dao
interface NormaDao {
    @Query("SELECT * FROM norma_table")
    fun getAll(): List<Norma>

    @Query("SELECT * FROM month_table")
    fun getAllMonth(): List<Month>

    @Query("SELECT * FROM norma_table WHERE datum LIKE :date")
    fun findByDate(date: Int): Norma

    @Query("SELECT * FROM norma_table WHERE datum like :month")
    fun findByMonth(month: String): List<Norma>

    @Query("DELETE FROM norma_table WHERE datum like :month")
    fun deleteByMonth(month: String): Int

    @Query("DELETE FROM month_table WHERE mesec like :month")
    fun deleteByMonth1(month: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert_norma(norma: Norma)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert_month(norma: Month)

    @Delete
    suspend fun delete_norma(norma: Norma)

    @Delete
    suspend fun delete_month(norma: Month)

    @Query("DELETE FROM norma_table")
    suspend fun deleteAll()
}