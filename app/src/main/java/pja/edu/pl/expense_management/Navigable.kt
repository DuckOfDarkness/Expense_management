package pja.edu.pl.expense_management

interface Navigable {

    enum class Destination {
        List, Add, Details, Edit
    }

    fun navigate(to: Destination, id: Int?)
}