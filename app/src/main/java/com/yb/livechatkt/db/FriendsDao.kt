package com.yb.livechatkt.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yb.livechatkt.bean.Friends

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends")
    fun getAllFriendsData():LiveData<MutableList<Friends>>

    @Insert
    suspend fun insertFriendsData(friends: Friends)

    @Insert
    suspend fun insertFriendsData(friends: List<Friends>)

    @Query("DELETE FROM friends")
    suspend fun deleteAllData()
}