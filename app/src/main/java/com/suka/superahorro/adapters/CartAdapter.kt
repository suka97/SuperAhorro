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
import com.suka.superahorro.R
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.packages.*

class CartAdapter (
    var carts: MutableList<Cart>,
    var onItemClick : (Int) -> Unit,
    var onItemDelete : (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ItemHolder>() {
    class ItemHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        init {
            this.view = v
        }

        fun getCard() : CardView {
            return view.findViewById(R.id.cardItem)
        }

        fun setPicture (picture_url: String?) {
            var img: ImageView = view.findViewById(R.id.img_item)
            if ( picture_url == null )
                img.setImageResource(R.drawable.default_item)
            else {
                Glide.with(img.context)
                    .load(picture_url)
                    .placeholder(R.drawable.default_item)
                    .into(img)
            }
        }

        fun setName (name: String?) {
            var txtName : TextView = view.findViewById(R.id.txtName_item)
            txtName.text = name
        }

        fun setPrice (price: Float?) {
            var txtPrice : TextView = view.findViewById(R.id.txtPrice_item)
            txtPrice.text = UnitValue(price, GLOBAL_UNIT_PRICE).toString()
        }

        fun setAmount (amount: Float?) {
            var txtAmount : TextView = view.findViewById(R.id.txtAmount_item)
            txtAmount.text = UnitValue(amount, GLOBAL_UNIT_AMOUNT).toString()
        }

        fun onCardViewLongClick (onItemClick: (Int) -> Unit) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.card_longclick_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuDelete -> {
                        onItemClick(adapterPosition)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

    }

    fun notifyDeleteItem (position: Int) {
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, carts.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recicler_carts, parent, false)
        return (ItemHolder(view))
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val cart = carts[position]

//        holder.setPicture(item.data.model?.img)
        holder.setName(cart.data.shop)
        holder.setPrice(cart.data.total)
        holder.setAmount(cart.data.items.size.toFloat())
        holder.getCard().setOnClickListener() {
            onItemClick(position)
        }
        holder.getCard().setOnLongClickListener() {
            holder.onCardViewLongClick(onItemDelete)
            true
        }
    }
}