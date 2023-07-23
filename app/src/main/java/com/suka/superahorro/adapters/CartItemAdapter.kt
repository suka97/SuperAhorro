package com.suka.superahorro.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suka.superahorro.R
import com.suka.superahorro.dbclasses.Cart
import com.suka.superahorro.dbclasses.CartItem
import com.suka.superahorro.packages.*

class CartItemAdapter (
    var recyclerView: RecyclerView,
    var cart: Cart,
    var onItemClick: (Int) -> Unit,
    var onItemDelete: (Int) -> Unit,
    var onItemChange: (cartItem: CartItem) -> Unit,
    var sortPattern: SortPattern = SortPattern.NONE
) : RecyclerView.Adapter<CartItemAdapter.ItemHolder>() {
    enum class SortPattern {
        NONE, PRICE, NAME;

        companion object {
            fun fromString(str: String?): SortPattern {
                return values().firstOrNull { it.name.equals(str, true) } ?: SortPattern.NONE
            }
        }
    }

    init {
        sort(sortPattern)
    }


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

        fun setModelName (name: String?) {
            var txtName : TextView = view.findViewById(R.id.txtName_model)
            txtName.text = name
            txtName.visibility = if (name == null) View.GONE else View.VISIBLE
        }

        fun setPrice (price: Float?) {
            var txtPrice : TextView = view.findViewById(R.id.txtPrice_item)
            txtPrice.text = UnitValue(price, GLOBAL_UNIT_PRICE).toString()
        }

        fun setAmount (amount: Float?, unit: String?) {
            var txtAmount : TextView = view.findViewById(R.id.txtAmount_item)
            txtAmount.text = UnitValue(amount, unit).toString()
        }

        fun setChecked (checked: Boolean, onChange: (Boolean)->Unit) {
            var checkAdded : CheckBox = view.findViewById(R.id.checkAdded)
            checkAdded.isChecked = checked
            checkAdded.setOnCheckedChangeListener { _, isChecked ->
                onChange(isChecked)
            }
        }



        fun onCardViewLongClick (index: Int, onItemClick: (Int) -> Unit) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.card_longclick_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuDelete -> {
                        onItemClick(index)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

    }

    private var sortedIndexes: List<Int>? = null    // entro con el indice sort, y obtengo el indice original en la lista

    fun notifyItemAdded (position: Int) {
        val listPos = sortedIndexes?.indexOf(position)
        if (listPos == null || listPos < 0) {
            notifyDataSetChanged()
            return
        }
        notifyItemInserted(listPos)
        notifyItemRangeChanged(listPos, cart.size())

        recyclerView.post {
            val holder = recyclerView.findViewHolderForAdapterPosition(listPos) as ItemHolder?
            holder?.getCard()?.highlight()
        }
    }

    fun notifyDeleteItem (position: Int) {
        val listPos = sortedIndexes?.indexOf(position)
        if (listPos == null || listPos < 0) {
            notifyDataSetChanged()
            return
        }
        notifyItemRemoved(listPos)
        notifyItemRangeChanged(listPos, cart.size())
    }


    fun getCartDescription(): String {
        return "Items: $itemCount, Total: ${UnitValue(cart.data.total, GLOBAL_UNIT_PRICE)}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recicler_cart_items, parent, false)
        return (ItemHolder(view))
    }

    override fun getItemCount(): Int {
        return cart.size()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val index = sortedIndexes?.get(position) ?: position
        val item = cart.getItem(index)

        holder.setPicture(item.data.model?.img)
        holder.setName(item.data.name)
        holder.setModelName(item.data.model?.name)
        holder.setPrice(item.getTotalPrice())
        holder.setAmount(item.data.amount, item.getMeasureUnit())
        holder.setChecked(item.data.checked) { newValue ->
            item.data.checked = newValue
            onItemChange(item)
        }
        holder.getCard().setOnClickListener() {
            onItemClick(index)
        }
        holder.getCard().setOnLongClickListener() {
            holder.onCardViewLongClick(index, onItemDelete)
            true
        }
    }

    fun sort(sortPattern: SortPattern) {
        when (sortPattern) {
            SortPattern.NONE -> {
                sortedIndexes = null
            }
            SortPattern.PRICE -> sortByPrice()
            SortPattern.NAME -> sortByName()
        }
        if ( this.sortPattern != sortPattern ) {
            this.sortPattern = sortPattern
            notifyDataSetChanged()
        }
    }

    private fun sortByPrice() {
        val sortedItemsWithIndex = cart.data.items.withIndex()
            .sortedBy { it.value.unit_price }
            .map { it.value to it.index }
        // Obtener solo los elementos ordenados en una nueva lista
        val sortedItems = sortedItemsWithIndex.map { it.first }
        // Obtener los nuevos índices en una lista separada
        sortedIndexes = sortedItemsWithIndex.map { it.second }
        this.sortedIndexes = sortedIndexes
    }

    private fun sortByName() {
        val sortedItemsWithIndex = cart.data.items.withIndex()
            .sortedBy { it.value.name }
            .map { it.value to it.index }
        // Obtener solo los elementos ordenados en una nueva lista
        val sortedItems = sortedItemsWithIndex.map { it.first }
        // Obtener los nuevos índices en una lista separada
        val sortedIndexes = sortedItemsWithIndex.map { it.second }
        this.sortedIndexes = sortedIndexes
    }
}