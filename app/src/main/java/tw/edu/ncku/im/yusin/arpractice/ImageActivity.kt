package tw.edu.ncku.im.yusin.arpractice

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.PixelCopy
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import tw.edu.ncku.im.yusin.arpractice.databinding.ActivityImageBinding
import java.io.IOException
import java.io.OutputStream


class ImageActivity : AppCompatActivity() {
    //sceneform部分參考sceneform google developer教學
    lateinit var binding: ActivityImageBinding
    lateinit var arFragment: ArFragment
    private lateinit var model: Uri
    private var renderable: ModelRenderable? = null
    private var animator: ModelAnimator? = null
    private lateinit var take: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this,"請將手機對往不同角度以偵測牆壁",Toast.LENGTH_SHORT).show()

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment3) as ArFragment
        val intent = intent
        var wallpaper:String = intent.getStringExtra("name")

        val resID = resources.getIdentifier(wallpaper, "drawable", packageName)


        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (plane.type != Plane.Type.VERTICAL) {
                return@setOnTapArPlaneListener
            }
            var anchor = hitResult.createAnchor()
            putwallpaper(arFragment, anchor, resID)
        }
        take = binding.record3
        take.setOnClickListener {
            val bitmap = takeScreenShot()
            Toast.makeText(this, "已儲存", Toast.LENGTH_SHORT).show()

        }


    }

    private fun takeScreenShot() {
        //Pixel Copy 部分參考https://stackoverflow.com/questions/58665513/how-can-i-take-screenshots-using-arcore
        val screenshot = arFragment.arSceneView

        // 建立bitmap的size
        val bitmap = Bitmap.createBitmap(
            screenshot.width, screenshot.height,
            Bitmap.Config.ARGB_8888
        )

        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        PixelCopy.request(screenshot, bitmap, { copyResult ->
            if (copyResult === PixelCopy.SUCCESS) {

                try {
                    saveMediaToStorage(bitmap)
                } catch (e: IOException) {
                    val toast: Toast = Toast.makeText(
                        this, e.toString(),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return@request
                }

            } else {
                Toast.makeText(this, "Fail to capture Sceenshot", Toast.LENGTH_SHORT).show()
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))
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

            val imageUri: Uri? = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

        }
    }



    private fun putwallpaper(arFragment: ArFragment, anchor: Anchor, resID: Int) {
        Texture.builder()
            .setSource(BitmapFactory.decodeResource(arFragment.resources, resID))
            .build()
            .thenAccept {
                MaterialFactory.makeOpaqueWithTexture(arFragment.context, it)
                    .thenAccept { material ->
                        val modelRenderable = ShapeFactory.makeCube(
                            Vector3(0.8f, 0.02f, 1.1f),
                            Vector3(0.0f, 0.0f, 0.0f),
                            material
                        )
                        addtoScene(arFragment, anchor, modelRenderable)
                    }
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






