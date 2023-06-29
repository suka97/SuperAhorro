package com.suka.superahorro.dbclasses

import com.suka.superahorro.database.DbModel

class Model(var data: DbModel) {
    constructor(name: String) : this(DbModel(name=name))
}