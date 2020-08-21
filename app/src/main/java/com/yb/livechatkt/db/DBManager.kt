package com.yb.livechatkt.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yb.livechatkt.LiveChatKtApplication
import com.yb.livechatkt.bean.Friends
import com.yb.livechatkt.bean.Session

@Database(entities = [Friends::class, Session::class],version = 2,exportSchema = false)
open abstract class DBManager : RoomDatabase() {

    abstract fun getFriendsDao():FriendsDao
    abstract fun getSessionDao():SessionDao

    companion object {

        @Volatile
        private var instance: DBManager? = null

        fun getDBInstance(): DBManager {

            if (instance == null) {

                synchronized(DBManager::class) {

                    if (instance == null) {

                        instance = Room.databaseBuilder(
                            LiveChatKtApplication.instanst,
                            DBManager::class.java,
                            "live.db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }

    }


}