package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.DbItem
import com.suka.superahorro.database.DbItemRef
import kotlinx.parcelize.Parcelize

@Parcelize
class Item(var data: DbItem): Parcelable {
    constructor(name: String) : this(DbItem(name = name))
    constructor(id: Long, name: String) : this(DbItem(id = id, name = name))

    fun toRef(): DbItemRef {
        return DbItemRef(id = data.id, name = data.name)
    }


}