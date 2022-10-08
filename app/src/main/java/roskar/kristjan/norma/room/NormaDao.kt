package roskar.kristjan.norma.room

import androidx.room.*

@Dao
interface NormaDao {

    /**
     * GET ALL FROM MONTH AND NORMA TABLE
     */

    @Query("SELECT * FROM norma_table")
    fun getAll(): List<Norma>

    @Query("SELECT * FROM month_table")
    fun getAllMonth(): List<Month>

    /**
     * FIND SPECIFIC ENTY
     */


    @Query("SELECT * FROM norma_table WHERE datum LIKE :date")
    fun findByDate(date: Int): Norma

    @Query("SELECT * FROM norma_table WHERE datum like :month")
    fun findByMonth(month: String): List<Norma>


    /**
     * DELETE SPECIFIC ENTRY
     */

    @Query("DELETE FROM norma_table WHERE datum like :month")
    fun deleteByMonthFromNormaTable(month: String): Int

    @Query("DELETE FROM month_table WHERE mesec like :month")
    fun deleteByMonthFromMonthTable(month: String): Int

    /**
     * INSERT DATA
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_norma(norma: Norma)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_month(norma: Month)

    /**
     * UPDATE DATA
     */

    @Query("UPDATE norma_table SET normaUre=:nUre, delovneUre=:dUre,delovnoMesto=:dMesto WHERE datum = :date")
    fun update(date: String, nUre: Double, dUre: Double, dMesto: String)

    /**
     *  svasta
     */

    @Delete
    suspend fun delete_norma(norma: Norma)

    @Delete
    suspend fun delete_month(norma: Month)

    @Query("DELETE FROM norma_table")
    suspend fun deleteAll()
}