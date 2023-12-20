package gr.gkortsaridis.superherosquadmaker.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.api.RetrofitBuilder
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityMainBinding
import gr.gkortsaridis.superherosquadmaker.ui.hero.view.HeroDetailsActivity
import gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel.MainViewModel
import gr.gkortsaridis.superherosquadmaker.utils.HeroListCreator
import gr.gkortsaridis.superherosquadmaker.utils.HeroView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = HeroesAdapter()

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(
            myRepository = MainRepository(
                MarvelApiHelper(RetrofitBuilder.apiService),
                Room.databaseBuilder(applicationContext, HeroesDatabase::class.java, "heroes-db").build()
            ),
            owner = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        adapter.setClickListener(object : HeroesAdapter.ClickListener {
            override fun onHeroClicked(hero: Hero) {
                startActivity(
                    Intent(this@MainActivity, HeroDetailsActivity::class.java).apply {
                        putExtra("HERO", hero)
                    }
                )
            }

            override fun onLoadMoreClicked() {
                MarvelApiHelper.characterOffset += MarvelApiHelper.characterLimit
                viewModel.getHeroes()
            }
        })
        binding.marvelSquadList.adapter = adapter
        binding.marvelSquadList.layoutManager = LinearLayoutManager(this)

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
                MainViewModel.HerosUiStates.Loading -> {
                    Log.i("TEST", "Loading")
                }
                is MainViewModel.HerosUiStates.Success -> {
                    adapter.setHeroesToDisplay(HeroListCreator.createHeroList(heroUiState.heroes, heroUiState.hasMore))
                }
                is MainViewModel.HerosUiStates.Error -> {
                    Log.i("TEST", heroUiState.error)
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
                }
                binding.mySquadList.addView(heroView)
            }
        }
    }
}