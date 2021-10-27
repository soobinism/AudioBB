package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class BookDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBook()
            .observe(requireActivity()) {
                updateBookDetails(it)
            }
    }

    private fun updateBookDetails(book: Book) {
        view?.findViewById<TextView>(R.id.bookTitleTextView)?.text = book.title
        view?.findViewById<TextView>(R.id.bookDetailsTextView)?.text = book.author
    }

}