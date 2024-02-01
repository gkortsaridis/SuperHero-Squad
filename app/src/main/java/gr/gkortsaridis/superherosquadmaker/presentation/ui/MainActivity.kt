package gr.gkortsaridis.superherosquadmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityMainBinding
import gr.gkortsaridis.superherosquadmaker.presentation.ui.utils.BaseActivity
import gr.gkortsaridis.superherosquadmaker.presentation.ui.utils.HeroView
import gr.gkortsaridis.superherosquadmaker.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity(), HeroesAdapter.ClickListener {

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
        viewModel.heroes.collect { heroUiState ->
            when(heroUiState) {
                is MainViewModel.HeroesUiStates.Loading -> {
                    if(heroUiState.isLoading) dialog.show()
                    else dialog.hide()
                }
                is MainViewModel.HeroesUiStates.Success -> {
                    adapter.setHeroesToDisplay(HeroListCreator.createHeroList(heroUiState.heroes, heroUiState.hasMore))
                }
                is MainViewModel.HeroesUiStates.Error -> {
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
                putExtra(Companion.hero, hero)
            }
        )
    }

    override fun onLoadMoreClicked() {
        viewModel.getHeroes()
    }

    companion object {
        const val hero = "HERO"
    }
}