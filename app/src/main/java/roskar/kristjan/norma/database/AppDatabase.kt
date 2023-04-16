package roskar.kristjan.norma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity

@Database(entities = [Productivity::class,Month::class], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun productivityDao(): ProductivityDao
    abstract fun monthDao(): MonthDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}