package com.example.week1.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SqliteHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {

    companion object{
        private const val TABLE_NAME = "imgTable"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table " + TABLE_NAME + " (" +
                "id integer primary key autoincrement, " +
                "image text not null," +
                "datetime text" +
                ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }


//    fun uriToByteArray(uri: Uri): ByteArray? {
//
//        val bitmapDrawable = drawable as BitmapDrawable?
//        val bitmap = mediastore.
//        val stream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//
//        return byteArray
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertImg(image: String): Boolean {
        val select = "select image from $TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        DatabaseUtils.dumpCursor(cursor)

        while (cursor.moveToNext()) {
            val imageExist: String = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            if (imageExist == image){
                return false
            }
        }
        cursor.close()
        rd.close()

        val values = ContentValues()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        values.put("image", image)
        values.put("datetime", LocalDateTime.now().format(formatter))

        val wd = writableDatabase
        wd.insert(TABLE_NAME, null, values)
        wd.close()

        return true
    }

//    데이타베이스에 저장된 값을 가져와 list 형식으로 image 목록 반환
    @SuppressLint("Range")
    fun selectImg(): MutableList<imgClass> {
        val list = mutableListOf<imgClass>()
        val select = "select * from $TABLE_NAME order by datetime asc"    // 날짜순 정렬
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        DatabaseUtils.dumpCursor(cursor)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val image: String = cursor.getString(cursor.getColumnIndex("image"))
            val datetime: String = cursor.getString(cursor.getColumnIndex("datetime"))

            list.add(imgClass(id, image, datetime))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun updateImg(img: imgClass) {
        val values = ContentValues()
        values.put("image", img.image)

        val wd = writableDatabase
        wd.update(TABLE_NAME,values,"id = ${img.id}", null)
        wd.close()
    }

    fun deleteImg(img: imgClass) {
        val delete = "delete from $TABLE_NAME where id = ${img.id}"

        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }
}