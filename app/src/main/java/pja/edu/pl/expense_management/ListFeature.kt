package pja.edu.pl.expense_management

import android.content.Context
import android.content.Intent

class ListFeature : Feature {
    override fun start(context: Context) {
        context.startActivity(Intent(context, ListScreen::class.java))
    }

}