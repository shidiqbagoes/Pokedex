package android.goes.pokemon.ui.home

import android.goes.pokemon.R
import android.goes.pokemon.data.remote.api.ApiConst
import android.goes.pokemon.data.remote.api.ApiInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.goes.pokemon.databinding.FragmentHomeBinding
import android.goes.pokemon.ui.home.adapter.PokemonAdapter
import android.goes.pokemon.utils.PokemonType
import android.goes.pokemon.utils.addChip
import android.goes.pokemon.utils.capitalize
import android.goes.pokemon.utils.dimen
import android.goes.pokemon.utils.font
import android.goes.pokemon.utils.viewBinding
import android.goes.pokemon.viewmodel.GeneralViewModel
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModels<GeneralViewModel>()

    private val pokemonAdapter by lazy { PokemonAdapter() }

    // Pokemon Type list used to store user input
    private val selectedPokemonType = mutableListOf<PokemonType>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindElementsFilter()
        bindAdapter()
        onPokemonRequestObserved()
    }

    private fun bindAdapter() {
        binding.rvPokemonList.adapter = pokemonAdapter
    }

    private fun onPokemonRequestObserved() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.requestPokemonList().collectLatest { pagingData ->
                    pokemonAdapter.submitData(lifecycle, pagingData)
                }
            }
        }
    }

    private fun bindElementsFilter() {
        val elementNames = elements
            .map { it.javaClass.simpleName.capitalize() }
            .toMutableList()
            .apply {
                add(ApiConst.STARTING_OFFSET, getString(R.string.all_elements))
            }


        elementNames.forEachIndexed { index, element ->
            binding.cgElementsFilter.addChip(
                chipId = index.hashCode(),
                chipText = element,
                chipAttributes = {
                    val chipDrawable = ChipDrawable
                        .createFromAttributes(context, null, 0, com.google.android.material.R.style.Widget_Material3_Chip_Filter)
                    setChipDrawable(chipDrawable)

                    setEnsureMinTouchTargetSize(false)

                    if (index == 0) isChecked = true

                    val verticalPadding = context.dimen(R.dimen._12dp).roundToInt()

                    setPadding(0, verticalPadding, 0, verticalPadding)

                    isCheckedIconVisible = false

                    typeface = context.font(R.font.montserrat_medium)
                    setTextColor(context.getColorStateList(R.color.chip_text_color_selector))
                    chipBackgroundColor = context.getColorStateList(R.color.chip_color_selector)
                    chipStrokeColor = context.getColorStateList(android.R.color.transparent)
                    shapeAppearanceModel = ShapeAppearanceModel()
                        .withCornerSize(context.resources.getDimension(R.dimen._32dp))
                        .toBuilder()
                        .build()
                },
                checkedAction = { view, isChecked ->

                    if (isChecked && index == 0) {
                        for (chipIndex in 1 until binding.cgElementsFilter.childCount) {
                            (binding.cgElementsFilter.getChildAt(chipIndex) as Chip).isChecked = false
                        }
                    } else {
                        (binding.cgElementsFilter.getChildAt(0) as Chip).isChecked = false
                    }

                    val pokemonType = PokemonType.fromString(element)

                    if (isChecked) {
                        selectedPokemonType.add(pokemonType)
                    } else {
                        selectedPokemonType.remove(pokemonType)
                    }

                    viewModel
                        .requestPokemonList("", selectedPokemonType)
                        .onEach { pokemonAdapter.submitData(lifecycle, it) }
                        .launchIn(lifecycleScope)

                    Toast.makeText(context, "$selectedPokemonType", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private val elements = mutableListOf(
        PokemonType.Electric,
        PokemonType.Fire,
        PokemonType.Water,
        PokemonType.Ice,
        PokemonType.Grass,
    )

}