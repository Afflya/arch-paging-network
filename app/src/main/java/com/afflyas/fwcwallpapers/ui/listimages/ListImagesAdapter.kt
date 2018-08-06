package com.afflyas.fwcwallpapers.ui.listimages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afflyas.fwcwallpapers.R
import com.afflyas.fwcwallpapers.databinding.ItemImageBinding
import com.afflyas.fwcwallpapers.repository.PixabayImage
import com.afflyas.fwcwallpapers.ui.common.ItemClickCallback

/**
 *
 * Adapter for [RecyclerView] in [ListImagesFragment]
 *
 */
class ListImagesAdapter(private val onItemClickCallback: ItemClickCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var images: List<PixabayImage>? = null

    /**
     *
     * Set item count based on the size of a list
     *
     * set 0 if it is null
     *
     * @return size of a albums list or 0
     */
    override fun getItemCount(): Int {
        val images = this.images
        return images?.size ?: 0
    }

    /**
     *
     * Set items data set or destroy all items if new list is empty/null
     *
     * @param newImages - new list of [PixabayImage] to display in the view
     */
    fun setImagesList(newImages: List<PixabayImage>?) {
        if (newImages == null || newImages.isEmpty()) {
            images = null
            notifyItemRangeInserted(0, 0)
        } else {
            if(images == null || images!!.isEmpty()){
                images = newImages
                notifyDataSetChanged()
            }else{
                val diffResult = DiffUtil.calculateDiff(ListImagesDiffUtilsCallback(images!!,newImages))
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    /**
     * set corresponding [PixabayImage] object to item
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image = images?.get(position)
        if (image != null) {
            val itemViewHolder = holder as ItemViewHolder
            itemViewHolder.binding.image = image
            itemViewHolder.binding.executePendingBindings()
        }
    }

    /**
     * create ViewHolder with data binding
     * and set callback
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemImageBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.context), R.layout.item_image, parent, false)
        binding.callback = onItemClickCallback
        return ItemViewHolder(binding)
    }

    companion object {
        /**
         * ViewHolder that contains binding with `item_image.xml` layout
         */
        class ItemViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
    }

}