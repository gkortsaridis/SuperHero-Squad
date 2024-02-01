package gr.gkortsaridis.superherosquadmaker.presentation.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import gr.gkortsaridis.marvelherodownloader.model.Thumbnail

object ImageBindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, thumbnail: Thumbnail?) {
        //Default images are received as http.
        //Website seems to redirect anything to https anyway, so it seems
        //a bit better to manually do the same myself instead of allowing cleartext http
        if(thumbnail != null) {
            val url = (thumbnail.path+"."+thumbnail.extension).replace("http","https")
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        }

    }
}