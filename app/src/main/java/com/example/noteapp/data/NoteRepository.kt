package com.example.noteapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.noteapp.data.entity.Note
import com.example.noteapp.data.network.ApiService
import com.example.noteapp.data.network.AppConfig
import com.example.noteapp.data.source.local.NoteDatabase
import com.example.noteapp.data.source.local.dao.LocalDataSource

class NoteRepository(app: Application) {

    private val noteDao: LocalDataSource
    private val apiClient = AppConfig.createService().create(ApiService::class.java)

    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(app)
        noteDao = noteDatabase.getNoteDao()
    }

//    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
//    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
//    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    fun getAllNote(): LiveData<List<Note>> = noteDao.getAllNote()

    suspend fun insertAllNoteToDatabase(notes: List<Note>) = noteDao.insertAllNote(notes)

    suspend fun deleteAllNoteFromDatabase() = noteDao.deleteAllNote()

    suspend fun getNotesFromApi() = apiClient.getAllNote()

    suspend fun addNoteToServer(note: Note) = apiClient.addNote(note)

    suspend fun updateNoteFromServer(id: Int, note: Note) = apiClient.updateNote(id, note)

    suspend fun deleteNoteFromServer(id: Int) = apiClient.deleteNote(id)
}
