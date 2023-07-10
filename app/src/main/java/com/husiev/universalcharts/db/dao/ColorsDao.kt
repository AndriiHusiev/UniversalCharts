package com.husiev.universalcharts.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.husiev.universalcharts.db.entity.ColorsEntity

@Dao
interface ColorsDao: BaseDao<ColorsEntity> {
    @Query("SELECT * FROM colors")
    fun loadColorsList(): LiveData<List<ColorsEntity?>>

    @Query("SELECT * FROM colors where uid = :uid")
    fun loadColor(uid: Int): LiveData<ColorsEntity?>

    @Query("SELECT * FROM colors where title = :title")
    fun loadColor(title: String): LiveData<ColorsEntity?>
}