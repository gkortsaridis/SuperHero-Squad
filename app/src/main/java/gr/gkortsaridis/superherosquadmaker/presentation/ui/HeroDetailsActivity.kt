package gr.gkortsaridis.superherosquadmaker.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityHeroDetailsBinding
import gr.gkortsaridis.superherosquadmaker.presentation.ui.utils.BaseActivity
import gr.gkortsaridis.superherosquadmaker.presentation.viewModel.HeroDetailsViewModel


@AndroidEntryPoint
class HeroDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityHeroDetailsBinding

    private val viewModel: HeroDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hero_details)
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.statusBarColor = Color.TRANSPARENT

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val hero = intent.getParcelableExtra(MainActivity.hero, Hero::class.java)!!
        viewModel.setHeroToDisplay(hero)

        binding.heroActionBtn.setOnClickListener {
            viewModel.handleHeroAction()
        }

        binding.closeBtn.setOnClickListener { finishAfterTransition() }
    }
}