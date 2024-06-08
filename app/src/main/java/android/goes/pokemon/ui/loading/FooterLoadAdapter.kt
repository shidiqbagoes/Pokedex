package android.goes.pokemon.ui.loading

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class FooterLoadAdapter(private val retry: () -> Unit = {}) :
	LoadStateAdapter<FooterLoadViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
		FooterLoadViewHolder.create(parent, retry).apply {
			if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
				(itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
			}
		}

	override fun onBindViewHolder(holder: FooterLoadViewHolder, loadState: LoadState) {
		holder.bind(loadState)
	}
}