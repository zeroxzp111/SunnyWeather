package com.example.sunnyweather.android.logic.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sunnyweather.android.logic.dao.PlaceDao
import com.example.sunnyweather.android.logic.model.DataPlace
import com.example.sunnyweather.android.logic.model.Location

@Database(version = 1, entities = [DataPlace::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun placeDao():PlaceDao
    companion object{
        val MIGRATION_1_2=object :Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("alter Place add column name text")
            }

        }
        private var instance:AppDatabase?=null;

        fun getDatabase(context: Context):AppDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"app_database")
                .addMigrations(MIGRATION_1_2)
                .build().apply {
                    instance=this
                }
        }
    }
}