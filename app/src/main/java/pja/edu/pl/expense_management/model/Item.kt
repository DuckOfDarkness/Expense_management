package pja.edu.pl.expense_management.model

import androidx.annotation.DrawableRes

data class Item(

    val name: String,
    val amount: String,
    val price: String,
    val description: String,

    @DrawableRes
    val imageId: Int

)
