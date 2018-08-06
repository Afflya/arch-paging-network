package com.afflyas.fwcwallpapers.ui.image

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afflyas.fwcwallpapers.R
import com.afflyas.fwcwallpapers.core.MainActivity
import com.afflyas.fwcwallpapers.databinding.FragmentImageBinding
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import android.app.WallpaperManager
import com.bumptech.glide.request.target.SimpleTarget
import android.R.attr.path
import android.os.Build
import android.widget.Toast
import com.afflyas.fwcwallpapers.utils.WallpaperHelper
import com.bumptech.glide.request.transition.Transition

/**
 *
 * Fragment for display [PixabayImage] in fullscreen and set the wallpaper
 *
 */
class ImageFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentImageBinding

    @Inject
    lateinit var mainActivity: MainActivity

    /**
     * Enable injections
     */
    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(arguments == null) throw IllegalArgumentException("arguments must contain PixabayImage object")

        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false)
        fragmentBinding.image = ImageFragmentArgs.fromBundle(arguments).image

        //Show toolbar above other elements for android 4.*
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            fragmentBinding.appBar.bringToFront()
        }


        mainActivity.setSupportActionBar(fragmentBinding.toolbar)

        val actionBar = mainActivity.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fragmentBinding.posterImageView.setOnClickListener { toggle() }

        return fragmentBinding.root
    }

    /**
     * Toogle fullscreen
     */
    override fun onResume() {
        super.onResume()
        toggle()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.apply_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemApply -> {
                setWallpaper()
            }
            android.R.id.home -> mainActivity.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Set wallpaper and show toast message after its done
     */
    fun setWallpaper(){
        Glide.with(this)
                .asBitmap()
                .load(fragmentBinding.image!!.largeImageURL)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val wm = WallpaperManager.getInstance(mainActivity)
                        wm.setBitmap(WallpaperHelper.cropBitmapFromCenterAndScreenSize(mainActivity, resource))
                    }
                })

        Toast.makeText(mainActivity, R.string.done, Toast.LENGTH_SHORT).show()
    }

    private var mVisible: Boolean = false

    /**
     * Toogle UI and system bars visibility
     */
    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    /**
     * Hide UI and system bars
     */
    private fun hide() {
        // Hide UI first
        fragmentBinding.uiIsVisible = false

        mVisible = false

        // remove the status and navigation bar
        fragmentBinding.coordinator.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    /**
     * Show UI and system bars
     */
    private fun show() {
        // Show the system bar
        fragmentBinding.coordinator.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        fragmentBinding.uiIsVisible = true
    }
}
