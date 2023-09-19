package pja.edu.pl.expense_management.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pja.edu.pl.expense_management.Navigable
import pja.edu.pl.expense_management.adapters.ItemsImagesAdapter
import pja.edu.pl.expense_management.data.ItemDatabase
import pja.edu.pl.expense_management.data.model.ItemEntity
import pja.edu.pl.expense_management.databinding.FragmentDetailsBinding
import kotlin.concurrent.thread


class DetailsFragment(id: Int?) : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var adapter: ItemsImagesAdapter
    private val selectedItemId: Int? = id
    private lateinit var selectedItem: ItemEntity

//    private var selectedIconId: Int = 0

    constructor() : this(null) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (selectedItemId != null) {
            super.onViewCreated(view, savedInstanceState)
            adapter = ItemsImagesAdapter()
            thread {
                selectedItem = selectedItemId.let {
                    ItemDatabase.open(requireContext()).items.getSelected(selectedItemId)
                }
                val i: Int = selectedItem.icon
                activity?.runOnUiThread {
                    binding.iconImage.setImageResource(i)
                    binding.nameTextView.text = selectedItem.name
                    binding.amountTextView.text = selectedItem.amount
                    binding.priceTextView.text = selectedItem.price
                    binding.descTextView.text = selectedItem.description
                    if(selectedItem.price.isNotEmpty() && selectedItem.amount.isNotEmpty()){
                        binding.totalTextView.text = (selectedItem.price.toDouble() * selectedItem.amount.toDouble()).toString()
                    }else{
                        binding.totalTextView.text = "0.0"
                    }
                }
            }
            binding.editActionButton.setOnClickListener{
                (activity as? Navigable)?.navigate(Navigable.Destination.Edit, selectedItemId)
            }
            binding.shareButton.setOnClickListener{
                var summary = 0.0
                if(selectedItem.amount.isNotEmpty() && selectedItem.price.isNotEmpty()){
                    summary = selectedItem.amount.toDouble() * selectedItem.price.toDouble()
                }
                val content = selectedItem.name+" - amount: "+selectedItem.amount+", price: "+selectedItem.price+", description: "+selectedItem.description+", total: "+summary
                val intent : Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, content)
                    type = "text/plain"
                }
                startActivity(intent)
            }
            ItemDatabase.open(requireContext()).close()
        }
    }
}

