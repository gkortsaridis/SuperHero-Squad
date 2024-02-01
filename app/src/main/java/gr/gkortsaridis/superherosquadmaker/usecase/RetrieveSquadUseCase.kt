package gr.gkortsaridis.superherosquadmaker.usecase

import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import javax.inject.Inject

class RetrieveSquadUseCase @Inject constructor(
    private val roomDb: HeroesDatabase
) {
    suspend operator fun invoke(): List<Hero> {
        return roomDb.heroesDao().getSquad()
    }
}