package edu.temple.audiobb

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import edu.temple.audlibplayer.PlayerService

class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface, ControlFragment.ActionsInterface {

    private lateinit var bookListFragment : BookListFragment
    var isConnected = false
    lateinit var mediaControlBinder: PlayerService.MediaControlBinder

    val progressHandler = Handler(Looper.getMainLooper()){
        if(it.obj != null) {
            val bookProgressObject = it.obj as PlayerService.BookProgress
            val progressTime = bookProgressObject.progress
            var seekBar = findViewById<SeekBar>(R.id.seekBar)
            var progressTextView = findViewById<TextView>(R.id.currentProgress)
            progressTextView.text = progressTime.toString()
            seekBar.progress = progressTime
        }
        true
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isConnected = true
            mediaControlBinder = service as PlayerService.MediaControlBinder
            mediaControlBinder.setProgressHandler(progressHandler)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }

    private val searchRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        supportFragmentManager.popBackStack()
        it.data?.run {
            bookListViewModel.copyBooks(getSerializableExtra(BookList.BOOKLIST_KEY) as BookList)
            bookListFragment.bookListUpdated()
        }
    }

    private val isSingleContainer : Boolean by lazy{
        findViewById<View>(R.id.container2) == null
    }

    private val selectedBookViewModel : SelectedBookViewModel by lazy {
        ViewModelProvider(this).get(SelectedBookViewModel::class.java)
    }

    private val bookListViewModel : BookList by lazy {
        ViewModelProvider(this).get(BookList::class.java)
    }

    companion object {
        const val BOOKLISTFRAGMENT_KEY = "BookListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.activitiesView, ControlFragment()).commit()

        bindService(Intent(this, PlayerService::class.java), serviceConnection, BIND_AUTO_CREATE)

        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment
            && selectedBookViewModel.getSelectedBook().value != null) {
            supportFragmentManager.popBackStack()
        }

        if (savedInstanceState == null) {
            bookListFragment = BookListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container1, bookListFragment, BOOKLISTFRAGMENT_KEY)
                .commit()
        } else {
            bookListFragment = supportFragmentManager.findFragmentByTag(BOOKLISTFRAGMENT_KEY) as BookListFragment
            if (isSingleContainer && selectedBookViewModel.getSelectedBook().value != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container1, BookDetailsFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }
        }

        if (!isSingleContainer && supportFragmentManager.findFragmentById(R.id.container2) !is BookDetailsFragment)
            supportFragmentManager.beginTransaction()
                .add(R.id.container2, BookDetailsFragment())
                .commit()

        findViewById<ImageButton>(R.id.searchButton).setOnClickListener {
            searchRequest.launch(Intent(this, BookSearchActivity::class.java))
        }

    }

    override fun onBackPressed() {
        selectedBookViewModel.setSelectedBook(null)
        super.onBackPressed()
    }

    override fun bookSelected(book: Book) {
        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onClickPlay(progressTime: Int) {
        Toast.makeText(this,"Clicked Play", Toast.LENGTH_SHORT).show()
        val currentBook = selectedBookViewModel.getSelectedBook().value
        if (currentBook != null) {
            if(progressTime > 0){
                mediaControlBinder.seekTo(progressTime)
            }
            else{
                mediaControlBinder.play(currentBook.id)
            }
        }
    }

    override fun onClickPause() {
        Toast.makeText(this,"Clicked Pause", Toast.LENGTH_SHORT).show()
        mediaControlBinder.pause()
    }

    override fun onClickStop() {
        Toast.makeText(this,"Clicked Stop", Toast.LENGTH_SHORT).show()
        mediaControlBinder.stop()
    }

    override fun onClickSeek() {
        Toast.makeText(this,"Clicked Change Time", Toast.LENGTH_SHORT).show()
    }
}