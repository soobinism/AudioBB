package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import android.net.Uri
import com.squareup.picasso.Picasso
import java.net.URL
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with

class BookDetailsFragment : Fragment() {

    lateinit var bookTitleTextView: TextView
    lateinit var bookDetailsTextView: TextView
    private lateinit var coverImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookTitleTextView = view.findViewById(R.id.bookTitleTextView)
        bookDetailsTextView =  view.findViewById(R.id.bookDetailsTextView)
        coverImageView = view.findViewById(R.id.coverImageView)

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBook()
            .observe(requireActivity()) {
                updateBookDetails(it)
            }
    }

    private fun updateBookDetails(book: Book) {
        bookTitleTextView.text = book.title
        bookDetailsTextView.text = book.author
        if (book.coverURL.isNotEmpty())
            Picasso.get().load(book.coverURL).into(coverImageView)

    }


}