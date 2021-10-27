package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface  {

    private var isTwoPane : Boolean = false
    lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTwoPane = findViewById<View>(R.id.fragmentContainerView2) != null
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        val booksList = BookList()
        populateBooks(10, booksList)

        val bookListFragment = BookListFragment.newInstance(booksList)

        // Check if BookDetailsFragment is open in portrait mode. If so, pop of the stack before
        // landscape orientation change.
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment
            && isTwoPane) {
            supportFragmentManager.popBackStack()
        }

        // Check if BookDetailsFragment is occupied with data in landscape and
        // open in portrait orientation change.
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) is BookDetailsFragment
            && !isTwoPane) {
            // Only open details over top of portrait recycler view if they are not empty.
            if (ViewModelProvider(this).get(BookViewModel::class.java).getBook().value?.title != ""
                && !bookViewModel.isEmpty()) {
                selectionMade()
            }

        }

        // Attach book list fragment only when app first starts.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, bookListFragment)
                .commit()
        }

        // If it is two panes attach second fragment, book details.
        if(isTwoPane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()
        }

    }

    /**
     * The populateBooks function will fill the BookList with the same number of books
     * provided from the parameters of the function.
     *
     * @param numOfBooks The number of books to populate the BookList with.
     * @param booksList The BookList to populate.
     */
    private fun populateBooks(numOfBooks: Int, booksList: BookList) {

        for (i in 0..numOfBooks) {
            val book = Book("Title: $i", "Author: $i")
            booksList.add(book)
        }
    }

    override fun selectionMade() {
        // If selection is made and it is not two pane open over top of book details fragment.
        if (!isTwoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
        // If the device is in two pane mode and back is pressed to remove details reattach
        // fragment if needed.
        else{
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewModelProvider(this).get(BookViewModel::class.java).setBook(Book("", ""))

    }

}
