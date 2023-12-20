package gr.gkortsaridis.superherosquadmaker.ui.main.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityMainBinding
import gr.gkortsaridis.superherosquadmaker.ui.hero.view.HeroDetailsActivity
import gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel.MainViewModel
import gr.gkortsaridis.superherosquadmaker.utils.HeroListCreator
import gr.gkortsaridis.superherosquadmaker.utils.HeroView
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HeroesAdapter.ClickListener {

    private lateinit var binding: ActivityMainBinding
    private val adapter = HeroesAdapter()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        adapter.setClickListener(this)
        binding.marvelSquadList.adapter = adapter
        binding.marvelSquadList.layoutManager = LinearLayoutManager(this)
        binding.toolbar.title = getString(R.string.app_name)

        collectHeroes()
        collectSquad()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSquad()
    }

    private fun collectHeroes() = lifecycleScope.launch {
        val dialog = ProgressDialog(this@MainActivity)
        dialog.setMessage("Loading...")

        viewModel.heroes.collect { heroUiState ->
            when(heroUiState) {
                MainViewModel.HerosUiStates.Loading -> {
                    dialog.show()
                }
                is MainViewModel.HerosUiStates.Success -> {
                    dialog.hide()
                    adapter.setHeroesToDisplay(HeroListCreator.createHeroList(heroUiState.heroes, heroUiState.hasMore))
                }
                is MainViewModel.HerosUiStates.Error -> {
                    dialog.hide()
                    Toast.makeText(this@MainActivity, "We encountered an error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun collectSquad() = lifecycleScope.launch {
        viewModel.squad.collect { squad ->
            binding.mySquadList.removeAllViews()
            binding.mySquadContainer.visibility = if(squad.isEmpty()) View.GONE else View.VISIBLE
            squad.sortedBy { it.name }.forEach { squadHero ->
                val heroView = HeroView(this@MainActivity).apply {
                    hero = squadHero
                    style = HeroView.HeroViewStyle.Vertical
                    setOnClickListener { onHeroClicked(squadHero, heroImageView) }
                }
                binding.mySquadList.addView(heroView)
            }
        }
    }

    override fun onHeroClicked(hero: Hero, heroImageView: ImageView) {
        startActivity(
            Intent(this@MainActivity, HeroDetailsActivity::class.java).apply {
                putExtra("HERO", hero)
            },
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, heroImageView, "hero_image"
            ).toBundle()
        )
    }

    override fun onLoadMoreClicked() {
        MarvelApiHelper.characterOffset += MarvelApiHelper.characterLimit
        viewModel.getHeroes()
    }
}