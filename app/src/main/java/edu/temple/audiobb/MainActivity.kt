package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface  {

    private var isTwoPane : Boolean = false
    lateinit var bookViewModel: BookViewModel
    private var booksList = BookList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTwoPane = findViewById<View>(R.id.fragmentContainerView2) != null
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        booksList.add(Book(0, "", "", ""))

        val bookListFragment = BookListFragment.newInstance(booksList)

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment
            && isTwoPane) {
            supportFragmentManager.popBackStack()
        }

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) is BookDetailsFragment
            && !isTwoPane) {
            if (bookViewModel.getBook().value?.id != -1
                && !bookViewModel.isEmpty()) {
                selectionMade()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, bookListFragment)
                .commit()
        }

        if(isTwoPane && supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        } else if(isTwoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()
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
        bookViewModel.setBook(Book(-1, "", "", ""))
    }
}