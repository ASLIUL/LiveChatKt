package com.yb.livechatkt.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "friends")
class Friends(
        @PrimaryKey()
        val accid:String,
        val heamImg: String,
        val id: Int,
        val name: String,
        val sex: Int,
        val signature: String
):Serializable {
    override fun toString(): String {
        return "Friends(accid='$accid', heamImg='$heamImg', id=$id, name='$name', sex=$sex, signature='$signature')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Friends
        if (accid != other.accid) return false
        return true
    }

}