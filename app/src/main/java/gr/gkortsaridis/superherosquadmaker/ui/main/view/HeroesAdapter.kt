package gr.gkortsaridis.superherosquadmaker.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.databinding.HeroListLoadMoreBinding
import gr.gkortsaridis.superherosquadmaker.databinding.HeroListViewBinding
import gr.gkortsaridis.superherosquadmaker.utils.HeroView

class HeroesAdapter : RecyclerView.Adapter<HeroesAdapter.HeroesItemVH>() {

    companion object {
        const val VIEW_TYPE_HERO = 0
        const val VIEW_TYPE_LOAD_MORE = 1
    }

    private var heroesToDisplay: List<AdapterItem> = listOf()
    private var clickListener: ClickListener? = null
    override fun getItemCount() = heroesToDisplay.size

    override fun getItemViewType(position: Int) =
        when(heroesToDisplay[position]) {
            is AdapterItem.HeroItem -> VIEW_TYPE_HERO
            is AdapterItem.LoadMoreItem -> VIEW_TYPE_LOAD_MORE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesItemVH {
        if(viewType == VIEW_TYPE_LOAD_MORE) {
            val binding = HeroListLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeroesItemVH.LoadMoreViewHolder(binding)
        } else {
            val binding = HeroListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeroesItemVH.HeroViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: HeroesItemVH, position: Int) {
        when(holder) {
            is HeroesItemVH.HeroViewHolder -> {
                val heroItem = heroesToDisplay[position] as AdapterItem.HeroItem
                holder.bind(heroItem.hero, clickListener)
            }
            is HeroesItemVH.LoadMoreViewHolder -> {
                holder.bind(clickListener)
            }
        }
    }

    fun setHeroesToDisplay(heroes: List<AdapterItem>) {
        heroesToDisplay = heroes
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    sealed class HeroesItemVH(view: View) : RecyclerView.ViewHolder(view) {
        class HeroViewHolder(private val binding: HeroListViewBinding) : HeroesItemVH(binding.root) {
            fun bind(hero: Hero, clickListener: ClickListener?) {
                binding.heroView.hero = hero
                binding.heroView.style = HeroView.HeroViewStyle.Horizontal
                binding.root.setOnClickListener { clickListener?.onHeroClicked(hero, binding.heroView.heroImageView) }
            }
        }

        class LoadMoreViewHolder(private val binding: HeroListLoadMoreBinding) : HeroesItemVH(binding.root) {
            fun bind(clickListener: ClickListener?) {
                binding.root.setOnClickListener { clickListener?.onLoadMoreClicked() }
            }
        }
    }


    sealed class AdapterItem {
        data class HeroItem(val hero: Hero) : AdapterItem()
        data object LoadMoreItem : AdapterItem()
    }

    interface ClickListener {
        fun onHeroClicked(hero: Hero, heroImageView: ImageView)
        fun onLoadMoreClicked()
    }
}