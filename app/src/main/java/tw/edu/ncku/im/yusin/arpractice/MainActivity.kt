package tw.edu.ncku.im.yusin.arpractice

import android.app.ActivityOptions
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mediaPlayer = MediaPlayer.create(this, R.raw.sunset)

        mediaPlayer.isLooping = true
        mediaPlayer?.start()

        binding.goin.setOnClickListener {
            val intent = Intent(this, MenuListActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

}