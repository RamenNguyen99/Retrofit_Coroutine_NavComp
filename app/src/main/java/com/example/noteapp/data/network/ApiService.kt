package com.example.noteapp.data.network

import com.example.noteapp.data.entity.Note
import retrofit2.http.*

/**
 * Created by tuong.nguyen2 on 09/09/2022.
 */
interface ApiService {
    @GET("/note")
    suspend fun getAllNote(): List<Note>

    @POST("/note")
    suspend fun addNote(@Body note: Note): Note

    @DELETE("/note/{id}")
    suspend fun deleteNote(@Path("id") id: Int): Note

    @PUT("/note/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body note: Note): Note
}
