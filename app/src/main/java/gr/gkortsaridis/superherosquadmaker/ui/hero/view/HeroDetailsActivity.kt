package gr.gkortsaridis.superherosquadmaker.ui.hero.view

import android.graphics.Color
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityHeroDetailsBinding
import gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel.HeroDetailsViewModel

@AndroidEntryPoint
class HeroDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeroDetailsBinding

    private val viewModel: HeroDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hero_details)
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.statusBarColor = Color.TRANSPARENT

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val hero = intent.getParcelableExtra("HERO", Hero::class.java)!!
        viewModel.setHeroToDisplay(hero)

        binding.heroActionBtn.setOnClickListener {
            viewModel.handleHeroAction()
        }

        binding.closeBtn.setOnClickListener { finishAfterTransition() }
    }
}