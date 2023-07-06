package com.suka.superahorro.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.textfield.TextInputLayout
import com.suka.superahorro.R
import com.suka.superahorro.database.DbItemRef
import com.suka.superahorro.database.DbUnit
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.Item
import com.suka.superahorro.packages.*

class UnitAdapter (
    var units: MutableList<DbUnit>
) : RecyclerView.Adapter<UnitAdapter.ItemHolder>() {
    class ItemHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        private lateinit var unit: DbUnit
        init {
            this.view = v
        }

        fun setTexts (unit: DbUnit) {
            this.unit = unit

            val nameLong: TextInputLayout = view.findViewById(R.id.nameLong_txt)
            nameLong.setText(unit.name_long)

            val nameShort: TextInputLayout = view.findViewById(R.id.nameShort_txt)
            nameShort.setText(unit.name_short)

            val sellUnit: TextInputLayout = view.findViewById(R.id.sellUnit_txt)
            sellUnit.setText(unit.sell_unit)

            val sellMult: TextInputLayout = view.findViewById(R.id.sellMult_txt)
            sellMult.setText(unit.sell_mult.toString())
        }


        fun setTextWatchers(callback: (unit: DbUnit)->Unit) {
            val nameLong: TextInputLayout = view.findViewById(R.id.nameLong_txt)
            nameLong.editText?.addTextChangedListener(SimpleTextWatcher {
                callback(getUnit())
            })

            val nameShort: TextInputLayout = view.findViewById(R.id.nameShort_txt)
            nameShort.editText?.addTextChangedListener(SimpleTextWatcher {
                callback(getUnit())
            })

            val sellUnit: TextInputLayout = view.findViewById(R.id.sellUnit_txt)
            sellUnit.editText?.addTextChangedListener(SimpleTextWatcher {
                callback(getUnit())
            })

            val sellMult: TextInputLayout = view.findViewById(R.id.sellMult_txt)
            sellMult.editText?.addTextChangedListener(SimpleTextWatcher {
                callback(getUnit())
            })
        }


        fun getUnit(): DbUnit {
            val nameLong: TextInputLayout = view.findViewById(R.id.nameLong_txt)
            val nameShort: TextInputLayout = view.findViewById(R.id.nameShort_txt)
            val sellUnit: TextInputLayout = view.findViewById(R.id.sellUnit_txt)
            val sellMult: TextInputLayout = view.findViewById(R.id.sellMult_txt)

            unit.name_long = nameLong.text()
            unit.name_short = nameShort.text()
            unit.sell_unit = sellUnit.text()
            unit.sell_mult = sellMult.number()
            return unit
        }

    }

    fun notifyDeleteItem (position: Int) {
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, units.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recicler_unit, parent, false)
        return (ItemHolder(view))
    }

    override fun getItemCount(): Int {
        return units.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setTexts(units[position])
        holder.setTextWatchers {
            units[position] = it
        }
    }
}