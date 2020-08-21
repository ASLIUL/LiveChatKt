package com.yb.livechatkt.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yb.livechatkt.bean.Session

@Dao
interface SessionDao {

    @Query("SELECT * FROM session")
    fun getAllDataLiveData() : LiveData<List<Session>>

    @Insert()
    suspend fun insertOneSession(session: Session)

    @Insert()
    suspend fun insertSessionList(session: List<Session>)

    @Query("UPDATE session SET lastMsg = :lastMSg,lastMsgTime = :lastMsgTime WHERE accid = :accid")
    suspend fun updateSession(accid:String,lastMSg:String,lastMsgTime:Long)


}