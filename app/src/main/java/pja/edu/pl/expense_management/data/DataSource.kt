package pja.edu.pl.expense_management.data

import pja.edu.pl.expense_management.R
import pja.edu.pl.expense_management.data.model.ItemEntity
import pja.edu.pl.expense_management.model.Item

object DataSource {

    val exampleItems = mutableListOf<Item>(
        Item("Qra", "2", "16.75", "Qra doskonala, lepsza niz w kfc. Prawie jak u babci. Chrupiąca skórka.", R.drawable.chicken),
        Item("Ważny lek", "1", "3.2", "Lek ważny dla zdrowia i życia", R.drawable.drugs),
        Item("Zastawa", "3", "11.98", "Dobra zastawa z porcelany", R.drawable.food),
        Item("Leczenie kanałowe", "1", "1200.0", "Bolesne leczenie zeba za olbrzymie pieniadze - nie refundowane.", R.drawable.health),
        Item("Java. Techniki zaawansowane", "1", "80.78", "Wydanie XI", R.drawable.java)
    )


}