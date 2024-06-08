package android.goes.pokemon.ui.loading

import android.goes.pokemon.databinding.ItemPagingLoadFooterBinding
import android.goes.pokemon.utils.inflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class FooterLoadViewHolder(
	private val binding: ItemPagingLoadFooterBinding,
	private val retryAction: () -> Unit = {}
) : RecyclerView.ViewHolder(binding.root) {

	init {
		binding.tvFooter.setOnClickListener { retryAction.invoke() }
	}

	fun bind(loadState: LoadState) {
		binding.cpiFooter.isVisible = loadState is LoadState.Loading
		binding.tvFooter.isVisible = loadState !is LoadState.Loading

		if (loadState is LoadState.Error) {
			binding.tvFooter.text = loadState.error.localizedMessage.orEmpty()

			Timber.e(loadState.error)
		}
	}

	companion object {
		fun create(
			parent: ViewGroup,
			retryAction: () -> Unit = {}
		): FooterLoadViewHolder {
			val itemBinding = ItemPagingLoadFooterBinding
				.inflate(parent.context.inflater, parent, false)

			return FooterLoadViewHolder(itemBinding, retryAction)
		}
	}
}