package pja.edu.pl.expense_management.adapters

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pja.edu.pl.expense_management.ItemCallback
import pja.edu.pl.expense_management.Navigable
import pja.edu.pl.expense_management.R
import pja.edu.pl.expense_management.data.ItemDatabase
import pja.edu.pl.expense_management.data.model.ItemEntity
import pja.edu.pl.expense_management.databinding.ListItemBinding
import pja.edu.pl.expense_management.fragments.ListFragment
import kotlin.concurrent.thread

class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ItemEntity) {
        binding.name.text = item.name
        binding.amountTextfield.text = item.amount
        binding.priceTextfield.text = item.price
        binding.imageView.setImageResource(item.icon)
    }
}

class ItemsAdapter(
    private val listFragment: ListFragment) :
    RecyclerView.Adapter<ItemViewHolder>() {
    private val data = mutableListOf<ItemEntity>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding).also { vh ->
            binding.root.setOnClickListener { view ->
                val itemPosition = vh.layoutPosition
                val itemId = data[itemPosition].id
                (view.context as? Navigable)?.navigate(Navigable.Destination.Details, itemId)
            }
            binding.root.setOnLongClickListener { view ->
                val itemPosition = vh.layoutPosition

                val item = data[itemPosition]
                val deleteQuestion = binding.root.context.getString(R.string.delete_question)
                val deleteConfirm = binding.root.context.getString(R.string.delete_confirm)
                val deleteCancel = binding.root.context.getString(R.string.delete_cancel)

                AlertDialog.Builder(view.context)
                    .setMessage(deleteQuestion + " " + item.name)
                    .setPositiveButton(deleteConfirm) { _, _ ->
                        thread {
                            if (data.size > 0) {
                                val itemId = data[itemPosition].id
                                val database = ItemDatabase.open(view.context)
                                val itemTotalPrice = if(data[itemPosition].price.isEmpty() || data[itemPosition].amount.isEmpty()) {
                                    0.0
                                } else  {
                                    data[itemPosition].price.toDouble() * data[itemPosition].amount.toDouble()
                                }
                                database.items.deleteItem(itemId)
                                data.removeAt(itemPosition)
                                handler.post {
                                    notifyItemRemoved(itemPosition)
                                    listFragment.setTotalPrice(String.format("%.2f", (listFragment.getTotalPrice()-itemTotalPrice)))
                                }
                            }
                        }
                    }
                    .setNegativeButton(deleteCancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun replace(newData: List<ItemEntity>) {
        data.clear()
        data.addAll(newData)
        handler.post {
            notifyDataSetChanged()
        }
    }

    fun sortName() {
        val notSorted = data.toList()
        data.sortBy { it.name.lowercase() }
        if (notSorted.isNotEmpty()) {
            val callback = ItemCallback(notSorted, data)
            val result = DiffUtil.calculateDiff(callback)
            handler.post {
                result.dispatchUpdatesTo(this)
            }
        }
    }

    fun sortPrice() {
        val notSorted = data.toList()
        data.sortBy { it.price.toDoubleOrNull() }
        val callback = ItemCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }
}



