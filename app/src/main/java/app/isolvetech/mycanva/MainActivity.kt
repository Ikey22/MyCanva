package app.isolvetech.mycanva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myCanvaView = MyCanvaView(this)
        myCanvaView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        myCanvaView.contentDescription = getString(R.string.content_description)
        setContentView(myCanvaView)
    }
}