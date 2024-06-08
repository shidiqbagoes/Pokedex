package android.goes.pokemon.ui.home.adapter

import android.goes.pokemon.R
import android.goes.pokemon.data.remote.api.ApiInterface
import android.goes.pokemon.data.remote.model.Pokemon
import android.goes.pokemon.databinding.ItemPokemonBinding
import android.goes.pokemon.utils.PokemonType
import android.goes.pokemon.utils.addChip
import android.goes.pokemon.utils.capitalize
import android.goes.pokemon.utils.colorStateList
import android.goes.pokemon.utils.dimen
import android.goes.pokemon.utils.drawable
import android.goes.pokemon.utils.homeSprite
import android.goes.pokemon.utils.lightenColor
import android.goes.pokemon.utils.loadImage
import android.goes.pokemon.utils.pokemonId
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PokemonAdapter() : PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        if (pokemon != null) {
            holder.bind(pokemon)
        }
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(pokemon: Pokemon) {
            binding.tvName.text = pokemon.name.capitalize()
            binding.tvName.transitionName = "shared_pokemon_text_$bindingAdapterPosition"

            binding.tvDesc.text = "Pokemon Ability #${pokemon.id}"
            binding.ivSprite.loadImage(
                source = pokemon.displaySprite,
                placeHolder = context.drawable(R.drawable.pikachu_placeholder)
            )

            binding.ivSprite.transitionName = "shared_pokemon_image_$bindingAdapterPosition"

            val types = pokemon.types.distinctBy { it }
            bindChip(types)

            val primaryType = types.firstOrNull()?.let { PokemonType.fromString(it) } ?: PokemonType.Normal
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(primaryType.color, lightenColor(primaryType.color))
            )
            binding.ivBgColor.background = gradientDrawable

            // Bind Click Callback
            binding.root.setOnClickListener {
                onItemClickCallback.invoke(
                    pokemon,
                    bindingAdapterPosition,
                    binding.ivSprite,
                    binding.tvName
                )
            }
        }

        private fun bindChip(types: List<String>) {
            binding.cgElements.removeAllViews()

            types.forEachIndexed { index, type ->
                binding.cgElements.addChip(
                    chipId = index.hashCode(),
                    chipText = type.capitalize(),
                    chipAttributes = {
                        val chipDrawable = ChipDrawable
                            .createFromAttributes(context, null, 0, R.style.TranslucentChipStyle)
                        setChipDrawable(chipDrawable)

                        chipBackgroundColor = context.colorStateList(R.color.white_25)

                        setTextColor(context.getColorStateList(R.color.colorOnPrimary))
                        setEnsureMinTouchTargetSize(false)

                        typeface = ResourcesCompat.getFont(context, R.font.montserrat_semibold)
                        chipStrokeColor = context.colorStateList(android.R.color.transparent)
                        shapeAppearanceModel = ShapeAppearanceModel()
                            .withCornerSize(context.dimen(R.dimen._32dp))
                            .toBuilder()
                            .build()
                    }
                )
            }
        }
    }

    private var onItemClickCallback: (
        item: Pokemon,
        position: Int,
        imageView: ShapeableImageView,
        textView: MaterialTextView
    ) -> Unit = { _, _, _, _ -> }

    fun setOnItemClickListener(action: (item: Pokemon, position: Int, imageView: ShapeableImageView, textView: MaterialTextView) -> Unit) {
        onItemClickCallback = action
    }
}
