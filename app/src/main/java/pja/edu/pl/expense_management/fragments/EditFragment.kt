package pja.edu.pl.expense_management.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pja.edu.pl.expense_management.adapters.ItemsImagesAdapter
import pja.edu.pl.expense_management.Navigable
import pja.edu.pl.expense_management.data.ItemDatabase
import pja.edu.pl.expense_management.data.model.ItemEntity
import pja.edu.pl.expense_management.databinding.FragmentEditBinding
import kotlin.concurrent.thread


class EditFragment(id: Int?) : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ItemsImagesAdapter
    private val selectedItemId: Int? = id
    private var selectedIconId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var database: ItemDatabase? = null
        try {
            database = ItemDatabase.open(requireContext())

            if (selectedItemId === null) {

                binding.saveButton.visibility = View.VISIBLE
                binding.editConfirmButton.visibility = View.INVISIBLE

                adapter = ItemsImagesAdapter()


                binding.images.apply {
                    adapter = this@EditFragment.adapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
                requireActivity().runOnUiThread {
                    binding.saveButton.setOnClickListener {
                        val newItem = ItemEntity(
                            name = binding.name.text.toString(),
                            amount = binding.editTextAmount.text.toString(),
                            price = binding.editTextPrice.text.toString(),
                            description = binding.editTextDescription.text.toString(),
                            icon = adapter.selectedImagesId
                        )
                        thread {
                            database.items.addItem(newItem)
                            requireActivity().runOnUiThread {
                                (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
                            }
                        }
                    }
                }
                // }
            } else {
                binding.saveButton.visibility = View.INVISIBLE
                binding.editConfirmButton.visibility = View.VISIBLE
                thread {
                    val selectedItem = database.items.getSelected(selectedItemId)
//                    selectedItemId.let {
//                    ItemDatabase.open(requireContext()).items.getSelected(selectedItemId)
//                }
                    selectedIconId = selectedItem.icon

                    adapter = ItemsImagesAdapter()
//                    adapter.setSelectedPositionImages(selectedIconId)

                    requireActivity().runOnUiThread {
                        binding.images.apply {
                            adapter = this@EditFragment.adapter
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        }
//                val i: Int = selectedItem.icon
//                    activity?.runOnUiThread {
                        adapter.setSelectedPositionImages(selectedIconId)
                        binding.name.setText(selectedItem.name)
                        binding.editTextAmount.setText(selectedItem.amount)
                        binding.editTextPrice.setText(selectedItem.price)
                        binding.editTextDescription.setText(selectedItem.description)
//                    }
                    }
                }
                binding.editConfirmButton.setOnClickListener {
                    val updatedItem = ItemEntity(
                        id = selectedItemId,
                        name = binding.name.text.toString(),
                        amount = binding.editTextAmount.text.toString(),
                        price = binding.editTextPrice.text.toString(),
                        description = binding.editTextDescription.text.toString(),
                        icon = adapter.selectedImagesId
                    )
                    thread {
                        database.items.updateItem(updatedItem)
                        requireActivity().runOnUiThread {
                            (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
                        }
                    }
                }
                database.close()
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}
