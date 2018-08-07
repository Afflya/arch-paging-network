package com.afflyas.fwcwallpapers.ui.listimages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afflyas.fwcwallpapers.R
import com.afflyas.fwcwallpapers.databinding.ItemImageBinding
import com.afflyas.fwcwallpapers.databinding.ItemLoadingStateBinding
import com.afflyas.fwcwallpapers.repository.NetworkState
import com.afflyas.fwcwallpapers.repository.PixabayImage
import com.afflyas.fwcwallpapers.ui.common.ItemClickCallback
import com.afflyas.fwcwallpapers.ui.common.RetryCallback

class ImagesPagedAdapter(private val onItemClickCallback: ItemClickCallback,
                         private val retryCallback: RetryCallback) : PagedListAdapter<PixabayImage, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_image -> {
                val binding: ItemImageBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.context), R.layout.item_image, parent, false)
                binding.callback = onItemClickCallback
                ItemImageViewHolder(binding)
            }
            R.layout.item_loading_state -> {
                val binding: ItemLoadingStateBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(parent.context), R.layout.item_loading_state, parent, false)
                binding.callback = retryCallback
                ItemLoadingStateViewHolder(binding)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_image -> {
                val image = getItem(position)
                val itemViewHolder = holder as ItemImageViewHolder
                itemViewHolder.binding.image = image
                itemViewHolder.binding.executePendingBindings() //todo возможно можно удалить
            }
            R.layout.item_loading_state -> {
                val itemViewHolder = holder as ItemLoadingStateViewHolder
                itemViewHolder.binding.networkState = networkState
                itemViewHolder.binding.executePendingBindings() //todo возможно можно удалить
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_loading_state
        } else {
            R.layout.item_image
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private var networkState: NetworkState? = null

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        /**
         * ViewHolder that contains binding with `item_image.xml` layout
         */
        class ItemImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
        /**
         * ViewHolder that contains binding with `item_loading_state.xml` layout
         */
        class ItemLoadingStateViewHolder(val binding: ItemLoadingStateBinding) : RecyclerView.ViewHolder(binding.root)


        val POST_COMPARATOR = object : DiffUtil.ItemCallback<PixabayImage>(){

            override fun areItemsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PixabayImage, newItem: PixabayImage): Boolean {
                return oldItem.id == newItem.id
            }

//            override fun getChangePayload(oldItem: PixabayImage, newItem: PixabayImage): Any? {
//                Log.d(App.DEV_TAG, javaClass.simpleName + " getChangePayload")
//
//                return super.getChangePayload(oldItem, newItem)
//            }
        }

    }
}