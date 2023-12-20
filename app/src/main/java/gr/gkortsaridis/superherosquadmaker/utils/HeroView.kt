package gr.gkortsaridis.superherosquadmaker.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.HeroViewBinding


class HeroView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: HeroViewBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.hero_view, this, true)
    }

    var hero: Hero? = null
        set(value) {
            field = value
            if(hero != null) {
                binding.hero = value
            }
        }

    var size: HeroSize = HeroSize.MediumHorizontal
        set(value) {
            field = value

            when (value) {
                HeroSize.SmallVertical -> {
                    orientation = VERTICAL
                    binding.divider.visibility = View.GONE
                }
                HeroSize.MediumHorizontal -> {
                    orientation = HORIZONTAL
                    binding.divider.visibility = View.VISIBLE
                    //binding?.parentLayout?.orientation = LinearLayout.HORIZONTAL
                }
            }
        }

    /**
     * I know i could have size and orientation in different fields,
     * I chose for this demo to have them together for simplicity
     */
    sealed class HeroSize {
        data object SmallVertical : HeroSize()
        data object MediumHorizontal : HeroSize()
    }
}