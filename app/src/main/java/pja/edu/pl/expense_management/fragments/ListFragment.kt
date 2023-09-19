package pja.edu.pl.expense_management.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import pja.edu.pl.expense_management.adapters.ItemsAdapter
import pja.edu.pl.expense_management.Navigable
import pja.edu.pl.expense_management.R
import pja.edu.pl.expense_management.data.DataSource
import pja.edu.pl.expense_management.data.ItemDatabase
import pja.edu.pl.expense_management.data.model.ItemEntity
import pja.edu.pl.expense_management.databinding.FragmentListBinding
import kotlin.concurrent.thread


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: ItemsAdapter? = null
    private lateinit var items: List<ItemEntity>

    /**Set true if you want to load a set of sample data*/
    private var exampleDataLoad: Boolean = true


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ItemsAdapter(this).apply {
            loadData()
        }
        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addButton.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add, null)
        }


        binding.azSortButton.setOnClickListener {
            adapter?.sortName()
        }

        binding.priceSortButton.setOnClickListener {
            adapter?.sortPrice()
        }
    }



    private fun loadData() = thread {

        val database = ItemDatabase.open(requireContext())

        items = database.items.getAll().map { entity ->
            ItemEntity(
                entity.id,
                entity.name,
                entity.amount,
                entity.price,
                entity.description,
                resources.getIdentifier(
                    entity.icon.toString(),
                    "drawable",
                    requireContext().packageName
                )
            )
        }
        items = items.sortedBy { it.name.lowercase() }
        if (exampleDataLoad) {
            loadExampleData()
        }
        adapter?.replace(items)
        calculateTotalPrice()

    }

    private fun loadExampleData() {
        val exampleItems = DataSource.exampleItems;
        val names = ItemDatabase.open(requireContext()).items.getNames()

        for (item in exampleItems) {
            if (!names.contains(item.name)) {
                try {
                    val newItem = ItemEntity(
                        name = item.name,
                        amount = item.amount,
                        price = item.price,
                        description = item.description,
                        icon = item.imageId
                    )
                    thread {
                        ItemDatabase.open(requireContext()).items.addItem(newItem)
                        showList()
                    }
                } catch (e: SQLiteConstraintException) {
                    activity?.runOnUiThread {
                        val errorMessage =
                            binding.root.context.getString(R.string.database_error)
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        exampleDataLoad = false
    }

    private fun showList() {
        items =
            ItemDatabase.open(requireContext()).items.getAll().map { entity ->
                ItemEntity(
                    entity.id,
                    entity.name,
                    entity.amount,
                    entity.price,
                    entity.description,
                    resources.getIdentifier(
                        entity.icon.toString(),
                        "drawable",
                        requireContext().packageName
                    )
                )
            }.sortedBy { it.name.lowercase() }
        activity?.runOnUiThread {
            adapter?.replace(items)
            calculateTotalPrice()
        }
    }

    fun calculateTotalPrice() {
        var summa = 0.0
        activity?.runOnUiThread {
            items.forEach { item ->
                if (item.price.isNotEmpty() && item.amount.isNotEmpty()) {
                    summa += (item.amount.toDouble() * item.price.toDouble())
                }
            }
            binding.summaryTextView.text = String.format("%.2f", summa)
        }
    }

    fun setTotalPrice(totalPrice: String){
        binding.summaryTextView.text = totalPrice
    }

    fun getTotalPrice(): Double {
        val returnedPrice = binding.summaryTextView.text.toString()
        return returnedPrice.replace(",", ".").toDouble()
    }


    override fun onStart() {
        super.onStart()
    }

}