package edu.temple.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri
import com.squareup.picasso.Picasso
import java.net.URL

class BookAdapter(private var bookList: BookList, private val myOnClick : (position: Int) -> Unit) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val myOnClick : (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView){
        val bookTitleTextView: TextView = itemView.findViewById(R.id.bookTitleRecyclerTextView)
        val bookDetailsTextView: TextView = itemView.findViewById(R.id.bookDetailsRecyclerTextView)
        val bookIDTextView: TextView = itemView.findViewById(R.id.bookIDTextView)

        init {
            itemView.setOnClickListener {
                myOnClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(view, myOnClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bookTitleTextView.text = bookList.get(position).title
        holder.bookDetailsTextView.text = bookList.get(position).author

        if (bookList.get(position).id == 0) {
            holder.bookIDTextView.text = ""
        } else {
            holder.bookIDTextView.text = bookList.get(position).id.toString()
        }
    }

    override fun getItemCount() = bookList.size()

    fun updateList(bookList: BookList) {
        this.bookList = bookList
        notifyDataSetChanged()
    }

}