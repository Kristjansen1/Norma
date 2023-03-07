package roskar.kristjan.norma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import roskar.kristjan.norma.model.Productivity

@Database(entities = [Productivity::class], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun productivity(): ProductivityDao

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