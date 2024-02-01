package gr.gkortsaridis.superherosquadmaker.utils

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(): AppCompatActivity() {

    protected val dialog by lazy {
        val builder = AlertDialog.Builder(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        builder.create()
    }

}