package gr.gkortsaridis.superherosquadmaker.usecase

import android.util.Log
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.domain.repository.room.HeroesDatabase
import javax.inject.Inject

class ToggleHeroInSquadUseCase @Inject constructor(
    private val heroInSquadUseCase: HeroIsInSquadUseCase,
    private val roomDb: HeroesDatabase
) {
    suspend operator fun invoke(hero: Hero) {
        if(heroInSquadUseCase(hero)) {
            roomDb.heroesDao().deleteHero(hero)
        } else {
            roomDb.heroesDao().insertHero(hero)
        }
    }
}