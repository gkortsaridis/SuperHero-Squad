package gr.gkortsaridis.superherosquadmaker.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
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
import gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel.MainViewModel
import gr.gkortsaridis.superherosquadmaker.utils.HeroListCreator
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
        binding.mySquadContainer.visibility = View.GONE
        lifecycleScope.launch {
            viewModel.content.collect { heroUiState ->
                when(heroUiState) {
                    MainViewModel.HerosUiStates.Loading -> {
                        Log.i("TEST", "Loading")
                    }
                    is MainViewModel.HerosUiStates.Success -> {
                        Log.i("TEST", heroUiState.heroes.toString())
                        adapter.setHeroesToDisplay(HeroListCreator.createHeroList(heroUiState.heroes.data))
                    }
                    is MainViewModel.HerosUiStates.Error -> {
                        Log.i("TEST", heroUiState.error)
                    }
                }
            }
        }
        adapter.setClickListener(object : HeroesAdapter.ClickListener {
            override fun onHeroClicked(hero: Hero) {
            }

            override fun onLoadMoreClicked() {
                MarvelApiHelper.characterOffset += MarvelApiHelper.characterLimit
                viewModel.getHeroes()
            }
        })
        binding.marvelSquadList.adapter = adapter
        binding.marvelSquadList.layoutManager = LinearLayoutManager(this)



    }
}