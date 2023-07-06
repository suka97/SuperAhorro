package com.suka.superahorro.dbclasses

import android.os.Parcelable
import com.suka.superahorro.database.DbCartItem
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.database.DbModelRef
import com.suka.superahorro.packages.GLOBAL_UNIT_NAME
import com.suka.superahorro.packages.GLOBAL_UNIT_PRICE
import kotlinx.parcelize.Parcelize

@Parcelize
class CartItem (var data: DbCartItem, var cartPos: Int) : Parcelable {
    constructor(name : String) : this(DbCartItem(name = name), 0)
    constructor(item: DbItemRef) : this(DbCartItem(
        id = item.id,
        name = item.name,
    ), 0)


    fun getTotalPrice() : Float? {
        // sin datos
        if ( data.unit_price == null || data.amount == null ) return null
        val genericTotal = data.unit_price!! * data.amount!!
        // sin modelo asociado
        if ( data.model == null ) return genericTotal
        // sin unidad asociada en el modelo
        val unit = DbGlobals.user.getUnit(data.model!!.base_unit)
        if ( unit == null ) return genericTotal
        // sin contenido neto asociado en el modelo
        if ( data.model!!.content == null ) return genericTotal
        // calculo completo
        return  (data.amount!! * data.model!!.content!!) / unit.sell_mult * data.unit_price!!
    }


    fun calcUnitPriceFromTotal(total: Float?) {
        // sin datos
        if ( data.amount == null || total == null ) return
        // sin modelo asociado
        val genericUnitPrice = total / data.amount!!
        if ( data.model == null ) {
            data.unit_price = genericUnitPrice
            return
        }
        // sin unidad asociada en el modelo
        val unit = DbGlobals.user.getUnit(data.model!!.base_unit)
        if ( unit == null ) {
            data.unit_price = genericUnitPrice
            return
        }
        // sin contenido neto asociado en el modelo
        if ( data.model!!.content == null ) {
            data.unit_price = genericUnitPrice
            return
        }
        // calculo completo
        data.unit_price = total / (data.amount!! * data.model!!.content!!) * unit.sell_mult
    }


    fun linkModel(model: Model) {
        data.model = model.toCartRef()
        val lastBuy = model.getLastBuy()
        if ( lastBuy != null ) {
            data.unit_price = lastBuy.price
            data.amount = lastBuy.amount
        }
    }


    fun toItemRef(): DbItemRef {
        return DbItemRef(
            id = data.id,
            name = data.name
        )
    }


    fun toModelRef(): DbModelRef? {
        if (data.model == null) return null
        return DbModelRef(
            id = data.model!!.id,
            name = data.model!!.name,
            sku = data.model!!.sku
        )
    }


    fun getMeasureUnit(): String? {
        // sin modelo asociado
        if (data.model == null) return null
        // sin unidad asociada en el modelo
        val unit = DbGlobals.user.getUnit(data.model!!.base_unit)
        if (unit == null) return null
        // sin contenido neto asociado en el modelo
        if (data.model!!.content == null) return unit.name_short
        // todos los datos, cuento en unidades
        return GLOBAL_UNIT_NAME
    }
    fun getBaseUnit() : String? {
        // sin modelo asociado
        if (data.model == null) return null
        // sin unidad asociada en el modelo
        val unit = DbGlobals.user.getUnit(data.model!!.base_unit)
        if (unit == null) return null
        // sin contenido neto asociado en el modelo
        return unit.name_short
    }
    fun getUnitPrice() : String {
        // sin modelo asociado
        if (data.model == null) return GLOBAL_UNIT_PRICE
        // sin unidad asociada en el modelo
        val unit = DbGlobals.user.getUnit(data.model!!.base_unit)
        if (unit == null) return GLOBAL_UNIT_PRICE
        // todos los datos, cuento en sell_unit
        return "$GLOBAL_UNIT_PRICE/${unit.sell_unit}"
    }
}