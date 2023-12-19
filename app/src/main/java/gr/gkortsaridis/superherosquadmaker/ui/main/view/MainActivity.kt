package gr.gkortsaridis.superherosquadmaker.data.ui.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import gr.gkortsaridis.superherosquadmaker.R
import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.api.RetrofitBuilder
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(
            myRepository = MainRepository(MarvelApiHelper(RetrofitBuilder.apiService)),
            owner = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}