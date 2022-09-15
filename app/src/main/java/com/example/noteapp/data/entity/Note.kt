package com.example.noteapp.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_table")
class Note(
    @ColumnInfo(name = "title_col") var title: String = "",
    @ColumnInfo(name = "description_col") var description: String = "",
) : Parcelable {
    // them myId de tranh crash khi update
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "my_id")
    var myId: Int? = null

    @ColumnInfo(name = "note_id_col")
    var id: Int = 0

    override fun toString(): String {
        return "Note(myId='$myId', id='$id', title='$title', description='$description')"
    }
}
