package pja.edu.pl.expense_management

import androidx.recyclerview.widget.DiffUtil
import pja.edu.pl.expense_management.data.model.ItemEntity

class ItemCallback(val notSorted: List<ItemEntity>, val sorted: List<ItemEntity>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = notSorted.size

    override fun getNewListSize(): Int = sorted.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] === sorted[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        notSorted[oldItemPosition] == sorted[newItemPosition]


}