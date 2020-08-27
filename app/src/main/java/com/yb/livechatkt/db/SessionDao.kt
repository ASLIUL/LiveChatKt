package com.yb.livechatkt.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yb.livechatkt.bean.Session
import retrofit2.http.DELETE

@Dao
interface SessionDao {

    @Query("SELECT * FROM session")
    fun getAllDataLiveData() : LiveData<List<Session>>

    @Insert()
    suspend fun insertOneSession(session: Session)

    @Query("SELECT * FROM session WHERE accid = :accid")
    suspend fun selectOneSessionByAccId(accid: String):Session

    @Insert()
    suspend fun insertSessionList(session: List<Session>)

    @Query("UPDATE session SET lastMsg = :lastMSg,lastMsgTime = :lastMsgTime WHERE accid = :accid")
    suspend fun updateSession(accid:String,lastMSg:String,lastMsgTime:Long)

    @Query("DELETE FROM session WHERE accid = :accid")
    suspend fun deleteOneSession(accid:String)

    @Delete
    suspend fun deleteSession(session: Session)


    @Query("DELETE FROM session")
    suspend fun deleteAllData()


}