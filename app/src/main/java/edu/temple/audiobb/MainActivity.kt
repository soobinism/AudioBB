package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val booksList = BookList()

        populateBooks(10, booksList)

        for (i in 0 until booksList.size()) {
            Log.d("title list", booksList.get(i).title)
            Log.d("author list", booksList.get(i).author)
        }
    }

    private fun populateBooks(numberOfBooks : Int, booksList : BookList){
        for (i in 0..numberOfBooks){
            val book = Book("Title: $i", "Author: $i")
            booksList.add(book)
        }
    }
}