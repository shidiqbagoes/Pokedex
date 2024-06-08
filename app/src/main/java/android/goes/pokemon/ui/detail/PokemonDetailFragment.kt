package android.goes.pokemon.ui.detail

import android.goes.pokemon.R
import android.goes.pokemon.databinding.FragmentPokemonDetailBinding
import android.goes.pokemon.utils.loadImage
import android.goes.pokemon.utils.pokemonId
import android.goes.pokemon.utils.viewBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment(R.layout.fragment_pokemon_detail) {

    private val binding by viewBinding<FragmentPokemonDetailBinding>()
    private val navArgs by navArgs<PokemonDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTransition()
        bindArgs()
    }

    private fun bindTransition() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        binding.ivPokemon.transitionName = "shared_pokemon_image_${navArgs.adapterPosition}"
        binding.tvPokemonName.transitionName = "shared_pokemon_name_${navArgs.adapterPosition}"
    }

    private fun bindArgs() {
        binding.ivPokemon.loadImage(
            source = navArgs.pokemon?.displaySprite
        )

        binding.tvPokemonName.text = navArgs.pokemon?.name
    }
}