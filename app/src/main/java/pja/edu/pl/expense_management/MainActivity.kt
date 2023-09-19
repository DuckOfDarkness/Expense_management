package pja.edu.pl.expense_management

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import pja.edu.pl.expense_management.fragments.DetailsFragment
import pja.edu.pl.expense_management.fragments.EditFragment
import pja.edu.pl.expense_management.fragments.ListFragment

class MainActivity : AppCompatActivity(), Navigable {

    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayUseLogoEnabled(true);
        supportActionBar?.title = " "+resources.getString(R.string.app_name)
        supportActionBar?.setIcon(R.drawable.ic_logo);


        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, id: Int?) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.container,
                    listFragment,
                    listFragment.javaClass.name
                )
                Navigable.Destination.Add -> {
                    replace(R.id.container, EditFragment(null), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Details -> {
                    replace(R.id.container, DetailsFragment(id), DetailsFragment::class.java.name)
                    addToBackStack(DetailsFragment::class.java.name)
                }
                Navigable.Destination.Edit -> {
                    replace(R.id.container, EditFragment(id), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
            }
        }.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        val currentFocus = currentFocus
        if (currentFocus != null) {
            val currentView = findViewById<View>(currentFocus.id)
            val myConstraintLayout = findViewById<ConstraintLayout>(R.id.ListFragment)

            if (currentView == myConstraintLayout) {
                finish()
                return true
            }
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is ListFragment) {
            finish()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}