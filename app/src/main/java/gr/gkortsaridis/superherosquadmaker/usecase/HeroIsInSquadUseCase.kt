package gr.gkortsaridis.superherosquadmaker.usecase

import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.domain.repository.room.HeroesDatabase
import javax.inject.Inject

class HeroIsInSquadUseCase @Inject constructor(
    private val roomDb: HeroesDatabase
) {
    suspend operator fun invoke(hero: Hero): Boolean {
        return roomDb.heroesDao().loadAById(hero.id) != null
    }
}