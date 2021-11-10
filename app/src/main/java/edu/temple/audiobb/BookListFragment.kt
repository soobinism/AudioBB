package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

private const val ARG_PARAM1 = "param1"

class BookListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookList: BookList
    private lateinit var bookViewModel: BookViewModel
    private lateinit var adapter : BookAdapter

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == AppCompatActivity.RESULT_OK) {
            bookList = it.data?.getSerializableExtra(SEARCH_RESULTS) as BookList
            adapter.updateList(bookList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookList = it.getSerializable(ARG_PARAM1) as BookList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.book_list, container, false)

        val launchSearchButton = layout.findViewById<Button>(R.id.launchSearchButton)
        launchSearchButton.setOnClickListener {
            val intent = Intent(requireContext(), BookSearchActivity::class.java)

            launcher.launch(intent)
        }

        bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)

        recyclerView = layout.findViewById(R.id.bookListRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter = BookAdapter(bookList) { position ->
            myOnClick(position)
        }
        recyclerView.adapter = adapter

        return layout
    }

    private fun myOnClick(position: Int) {
        (activity as EventInterface).selectionMade()
        bookViewModel.setBook(bookList.get(position))
    }

    companion object {
        @JvmStatic
        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, bookList)
                }
            }

    }

    interface EventInterface {
        fun selectionMade()
    }
}