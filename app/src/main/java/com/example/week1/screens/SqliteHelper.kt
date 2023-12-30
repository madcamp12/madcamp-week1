package com.example.week1.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo (" +
                "num integer primary key, " +
                "image blob" +
                ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insertMemo(memo: Memo) {
        val values = ContentValues()
        values.put("image", memo.image)

        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()
    }

//    데이타베이스에 저장된 값을 가져와 list 형식으로 image 목록 반환
@SuppressLint("Range")
fun selectMemo(): MutableList<Memo> {
        val list = mutableListOf<Memo>()
        val select = "select * from memo order by datetime desc"    // 날짜순 정렬
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        DatabaseUtils.dumpCursor(cursor)

        while (cursor.moveToNext()) {
            val num = cursor.getLong(cursor.getColumnIndex("num"))
            val image: ByteArray? = cursor.getBlob(cursor.getColumnIndex("image")) ?:null

            list.add(Memo(num, image))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun updateMemo(memo: Memo) {
        val values = ContentValues()
        values.put("image", memo.image)

        val wd = writableDatabase
        wd.update("memo",values,"num = ${memo.num}", null)
        wd.close()
    }

    fun deleteMemo(memo: Memo) {
        val delete = "delete from memo where no = ${memo.num}"

        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }
}