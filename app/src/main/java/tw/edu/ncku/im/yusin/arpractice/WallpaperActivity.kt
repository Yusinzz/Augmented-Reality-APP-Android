package tw.edu.ncku.im.yusin.arpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityWallpaperBinding

class WallpaperActivity : AppCompatActivity() {

    lateinit var binding:ActivityWallpaperBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dragoncat.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","dragoncat")
            startActivity(intent)
        }
        binding.titanic.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","titanic")
            startActivity(intent)
        }
        binding.johnwick.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","johnwick")
            startActivity(intent)
        }
        binding.johnwick3.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","johnwick3")
            startActivity(intent)
        }
        binding.thiefFamily.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","thief_family")
            startActivity(intent)
        }
        binding.gamenight.setOnClickListener{
            intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("name","gamenight")
            startActivity(intent)
        }
    }
}