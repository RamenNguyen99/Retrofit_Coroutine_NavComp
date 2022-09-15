package com.example.noteapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapp.data.entity.Note
import com.example.noteapp.data.source.local.dao.LocalDataSource

@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): LocalDataSource

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, NoteDatabase::class.java, "NoteDatabase")
                        .build()
            }
            return instance!!
        }
    }

}
