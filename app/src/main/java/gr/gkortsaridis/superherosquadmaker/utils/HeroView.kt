package gr.gkortsaridis.superherosquadmaker.utils

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
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

    var style: HeroViewStyle = HeroViewStyle.Horizontal
        set(value) {
            field = value
            when (value) {
                HeroViewStyle.Vertical -> {
                    binding.heroIcon.layoutParams.apply {
                        width = dpToPx(64)
                        height = dpToPx(64)
                    }
                    binding.heroCard.layoutParams.apply {
                        width = dpToPx(64)
                        height = dpToPx(64)
                    }
                    binding.heroCard.radius = dpToPx(32).toFloat()
                    orientation = VERTICAL
                    binding.divider.visibility = View.GONE
                    binding.heroName.maxLines = 2
                    binding.heroName.gravity = Gravity.CENTER_HORIZONTAL
                }
                HeroViewStyle.Horizontal -> {
                    orientation = HORIZONTAL
                    binding.divider.visibility = View.VISIBLE
                    binding.heroName.gravity = Gravity.CENTER_VERTICAL
                    binding.textContainer.setPadding(dpToPx(16), 0, 0, 0)
                }
            }
        }

    val heroImageView: ImageView
        get() = binding.heroIcon


    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics).toInt()
    }

    sealed class HeroViewStyle {
        data object Vertical : HeroViewStyle()
        data object Horizontal : HeroViewStyle()
    }
}