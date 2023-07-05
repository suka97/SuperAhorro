package com.suka.superahorro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        init {
            this.view = v
        }

        fun getCard() : CardView {
            return view.findViewById(R.id.cardItem)
        }

        fun setNameLong (name: String?) {
            var txtName: TextInputLayout = view.findViewById(R.id.nameLong_txt)
            txtName.setText(name)
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
        val unit = units[position]

        holder.setNameLong(unit.name_long)
    }
}