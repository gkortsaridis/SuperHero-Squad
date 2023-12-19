package gr.gkortsaridis.superherosquadmaker.utils

import gr.gkortsaridis.superherosquadmaker.data.model.CharacterDataContainer
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.ui.main.view.HeroesAdapter

object HeroListCreator {

    fun createHeroList(data: CharacterDataContainer): List<HeroesAdapter.AdapterItem> {
        val heroList: MutableList<HeroesAdapter.AdapterItem> = data.results.map { HeroesAdapter.AdapterItem.HeroItem(it) }.toMutableList()
        if(data.offset + data.count < data.total) {
            heroList.add(HeroesAdapter.AdapterItem.LoadMoreItem)
        }
        return heroList
    }
}