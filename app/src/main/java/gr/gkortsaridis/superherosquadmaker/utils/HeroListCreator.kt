package gr.gkortsaridis.superherosquadmaker.utils

import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.ui.main.view.HeroesAdapter

object HeroListCreator {
    fun createHeroList(heroes: List<Hero>, hasMore: Boolean): List<HeroesAdapter.AdapterItem> {
        val heroList: MutableList<HeroesAdapter.AdapterItem> = heroes.map { HeroesAdapter.AdapterItem.HeroItem(it) }.toMutableList()
        if(hasMore) {
            heroList.add(HeroesAdapter.AdapterItem.LoadMoreItem)
        }
        return heroList
    }
}