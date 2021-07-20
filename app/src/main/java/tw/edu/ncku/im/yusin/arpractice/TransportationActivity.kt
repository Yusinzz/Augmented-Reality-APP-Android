package tw.edu.ncku.im.yusin.arpractice

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.transition.Explode
import androidx.transition.Fade
import androidx.transition.Slide
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityFurnitureBinding
import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityTransportationBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class TransportationActivity : AppCompatActivity() {
    //sceneform部分參考sceneform google developer教學
    lateinit var binding: ActivityTransportationBinding
    lateinit var arFragment: ArFragment
    private lateinit var model: Uri
    private var renderable: ModelRenderable? = null
    private var animator: ModelAnimator? = null
    private lateinit var take: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransportationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Toast.makeText(this,"請將手機對往不同角度以偵測平面",Toast.LENGTH_SHORT).show()


        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment2) as ArFragment
        model = Uri.parse("tank.sfb")
        choose()
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                return@setOnTapArPlaneListener
            }
            var anchor = hitResult.createAnchor()
            placeObject(arFragment, anchor, model)
        }
        take = binding.record2
        take.setOnClickListener {
            val bitmap = takeScreenShot()
            Toast.makeText(this, "已儲存", Toast.LENGTH_SHORT).show()

        }


    }

    private fun takeScreenShot() {
        //Pixel Copy 參考https://stackoverflow.com/questions/58665513/how-can-i-take-screenshots-using-arcore
        val screenshot = arFragment.arSceneView

        // Create a bitmap the size of the scene view.
        val bitmap = Bitmap.createBitmap(screenshot.width, screenshot.height,
            Bitmap.Config.ARGB_8888)

        // Create a handler thread to offload the processing of the image.
        val handler = HandlerThread("PixelCopier")
        handler.start()
        // Make the request to copy.
        PixelCopy.request(screenshot, bitmap, { copyResult ->
            if (copyResult === PixelCopy.SUCCESS) {

                try {
                    saveMediaToStorage(bitmap)
                } catch (e: IOException) {
                    val toast: Toast = Toast.makeText(this, e.toString(),
                        Toast.LENGTH_LONG)
                    toast.show()
                    return@request
                }

            } else {
                Toast.makeText(this, "拍照失敗", Toast.LENGTH_SHORT).show()
            }
            handler.quitSafely()
        }, Handler(handler.looper))
    }


    private fun saveMediaToStorage(bitmap: Bitmap) {
        //部分參考https://stackoverflow.com/questions/66594081/saving-media-files-in-android
        val filename = "${System.currentTimeMillis()}.jpg"

        var fos: OutputStream? = null
        this.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }


        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

        }
    }

    private fun choose() {

        binding.tank.setOnClickListener {
            model = Uri.parse("tank.sfb")
            Toast.makeText(this, "已選擇坦克", Toast.LENGTH_SHORT).show()
        }


        binding.futrueTank.setOnClickListener {
            model = Uri.parse("future_tank.sfb")
            Toast.makeText(this, "已選擇未來坦克", Toast.LENGTH_SHORT).show()
        }
        binding.boat.setOnClickListener{
            model = Uri.parse("boat.sfb")
            Toast.makeText(this, "已選擇船船", Toast.LENGTH_SHORT).show()
        }

        binding.space.setOnClickListener {
            model = Uri.parse("space.sfb")
            Toast.makeText(this, "已選擇太空梭", Toast.LENGTH_SHORT).show()
        }
        binding.xPlane.setOnClickListener {
            model = Uri.parse("x_plane.sfb")
            Toast.makeText(this, "已選擇Ｘ戰機", Toast.LENGTH_SHORT).show()
        }
        binding.xPlane1.setOnClickListener {
            model = Uri.parse("x_plane1.sfb")
            Toast.makeText(this, "已選擇Ｘ戰機", Toast.LENGTH_SHORT).show()
        }
        binding.oldCar.setOnClickListener {
            model = Uri.parse("old_car.sfb")
            Toast.makeText(this, "已選擇車車", Toast.LENGTH_SHORT).show()
        }
        binding.raceCar.setOnClickListener {
            model = Uri.parse("race_car.sfb")
            Toast.makeText(this, "已選擇跑車", Toast.LENGTH_SHORT).show()
        }
        binding.raceCar1.setOnClickListener {
            model = Uri.parse("race_car1.sfb")
            Toast.makeText(this, "已選擇跑車", Toast.LENGTH_SHORT).show()
        }
        binding.futureCar.setOnClickListener {
            model = Uri.parse("future_car.sfb")
            Toast.makeText(this, "已選擇未來車", Toast.LENGTH_SHORT).show()
        }
    }


    private fun placeObject(arFragment: ArFragment, anchor: Anchor?, model: Uri?) {
        ModelRenderable.builder()
            .setSource(arFragment.context, model)
            .build()
            .thenAccept {
                renderable = it
                addtoScene(arFragment, anchor, it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message).setTitle("Error")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }

    }

    private fun addtoScene(arFragment: ArFragment, anchor: Anchor?, renderable: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)
        val skeletonNode = SkeletonNode()
        skeletonNode.renderable = renderable
        val node = TransformableNode(arFragment.transformationSystem)
        node.getScaleController().setMaxScale(1f);
        //調整AR object呈現最大size
        node.getScaleController().setMinScale(0.01f);
        //調整AR object呈現最小size
        node.addChild(skeletonNode)
        node.setParent(anchorNode)
        arFragment.arSceneView.scene.addChild(anchorNode)
        node.select()
    }
}