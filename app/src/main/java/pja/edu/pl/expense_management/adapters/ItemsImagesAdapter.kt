package pja.edu.pl.expense_management.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pja.edu.pl.expense_management.R
import pja.edu.pl.expense_management.databinding.ItemsImagesBinding

class ItemImageViewHolder(val binding: ItemsImagesBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(imageId: Int, isSelected: Boolean) {
        binding.image.setImageResource(imageId)
        binding.selectedFrame.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class ItemsImagesAdapter : RecyclerView.Adapter<ItemImageViewHolder>() {

    private val images = listOf(
        R.drawable.chicken,
        R.drawable.drugs,
        R.drawable.food,
        R.drawable.health,
        R.drawable.java
    )
    private var selectedPositionImages: Int = 0
    val selectedImagesId: Int
        get() = images[selectedPositionImages]

    fun setSelectedPositionImages(position: Int) {
        selectedPositionImages = images.indexOf(position)
        notifyDataSetChanged()
    }

    fun getImages(): List<Int> {
        return images
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val binding = ItemsImagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemImageViewHolder(binding)
            .also { vh ->
                binding.root.setOnClickListener {
                    notifyItemChanged(selectedPositionImages)
                    selectedPositionImages = vh.layoutPosition
                    notifyItemChanged(selectedPositionImages)
                }
            }
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPositionImages)
    }

    override fun getItemCount(): Int = images.size
}