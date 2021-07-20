package tw.edu.ncku.im.yusin.arpractice

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import android.widget.Adapter
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
//import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityMenuListBinding

class MenuListActivity : AppCompatActivity() {
    private lateinit var myrecyclerView: RecyclerView
//    private lateinit var mylist: MutableList<menu>
    lateinit var image: MutableList<Int>
    lateinit var text: MutableList<String>
    lateinit var Fade:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set set the transition to be shown when the user enters this activity
            enterTransition = android.transition.Fade()
            // set the transition to be shown when the user leaves this activity
            exitTransition = android.transition.Fade()
        }
        setContentView(R.layout.activity_menu_list)

        myrecyclerView = findViewById(R.id.MenuRecycleview)
        myrecyclerView.layoutManager = LinearLayoutManager(this)


        var adapter = MainAdapter(mylist)
        myrecyclerView.adapter = adapter
        adapter.setOnMenuClicklistener(
                object : MainAdapter.onMenuClicklistener {
                    override fun onClick(position: Int) {
                        Toast.makeText(this@MenuListActivity, "Clicked", Toast.LENGTH_LONG).show()
                        when (position) {
                            0 -> {
                                val intent = Intent(this@MenuListActivity, FurnitureActivity::class.java)
                                    startActivity(intent)
                            }
                            1 -> {
                                val intent = Intent(this@MenuListActivity, AnimalActivity::class.java)
                                    startActivity(intent)
                            }
                            2->{
                                val intent = Intent(this@MenuListActivity, TransportationActivity::class.java)
                                startActivity(intent)
                            }
                            3->{
                                val intent = Intent(this@MenuListActivity, WallpaperActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                })
    }
}
