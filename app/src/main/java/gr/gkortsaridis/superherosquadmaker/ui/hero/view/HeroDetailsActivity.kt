package gr.gkortsaridis.superherosquadmaker.ui.hero.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.bumptech.glide.Glide
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.api.RetrofitBuilder
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.databinding.ActivityHeroDetailsBinding
import gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel.HeroDetailsViewModel

class HeroDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeroDetailsBinding

    private val viewModel: HeroDetailsViewModel by viewModels {
        HeroDetailsViewModel.provideFactory(
            myRepository = MainRepository(
                MarvelApiHelper(RetrofitBuilder.apiService),
                Room.databaseBuilder(applicationContext, HeroesDatabase::class.java, "heroes-db").build()
            ),
            owner = this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hero_details)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val hero = intent.getParcelableExtra("HERO", Hero::class.java)!!
        viewModel.setHeroToDisplay(hero)

        binding.heroActionBtn.setOnClickListener {
            viewModel.handleHeroAction()
        }
    }
}