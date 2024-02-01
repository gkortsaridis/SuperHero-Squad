package gr.gkortsaridis.superherosquadmaker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import gr.gkortsaridis.marvelherodownloader.MarvelHeroesDownloader

@HiltAndroidApp
class SuperheroSquadMakerApplication: Application() {

    init {
        MarvelHeroesDownloader.setupDownloader(
            privateApiKey = "622919e60ea63eb5355ac75a1ab7be2a65ea23e2",
            publicApiKey = "301ea4d2491e82711f03ee01a782124c",
            characterLimit = 100
        )
    }

}