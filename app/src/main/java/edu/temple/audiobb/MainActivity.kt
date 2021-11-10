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

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment
            && isTwoPane) {
            supportFragmentManager.popBackStack()
        }

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) is BookDetailsFragment
            && !isTwoPane) {
            if (ViewModelProvider(this).get(BookViewModel::class.java).getBook().value?.id != -1
                && !bookViewModel.isEmpty()) {
                selectionMade()
            }

        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, bookListFragment)
                .commit()
        }

        if(isTwoPane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()
        }

    }

    private fun populateBooks(numOfBooks: Int, booksList: BookList) {
        for (i in 0..numOfBooks) {
            val book = Book(i, "Title: $i, "Author: $i", "URL: $i")
            booksList.add(book)
        }
    }

    override fun selectionMade() {
        if (!isTwoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
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
        ViewModelProvider(this).get(BookViewModel::class.java).setBook(Book(-1, "", "", ""))

    }

}
