package com.afflyas.fwcwallpapers.ui.listimages

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment

import com.afflyas.fwcwallpapers.R
import com.afflyas.fwcwallpapers.core.MainActivity
import com.afflyas.fwcwallpapers.databinding.FragmentListImagesBinding
import com.afflyas.fwcwallpapers.repository.PixabayImage
import com.afflyas.fwcwallpapers.ui.common.ItemClickCallback
import com.afflyas.fwcwallpapers.ui.common.RetryCallback
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import androidx.recyclerview.widget.GridLayoutManager



class ListImagesFragment : Fragment(), RetryCallback, ItemClickCallback {

    private lateinit var fragmentBinding: FragmentListImagesBinding

    @Inject
    lateinit var mainActivity: MainActivity

    /**
     * Custom factory to enable injecting into ViewModel
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: ListImagesViewModel

    private var listAdapter: ListImagesAdapter? = null

    /**
     * Enable injections
     */
    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_images, container, false)
        fragmentBinding.callback = this
        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListImagesViewModel::class.java)
        subscribeUI()
    }

    /**
     * set adapter to recyclerView
     *
     * subscribe searchView to text submit. Call new search request after it is happen
     *
     * subscribe observer for search result
     * to change data in the view's binding and searchAdapter
     *
     */
    private fun subscribeUI() {

        fragmentBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String): Boolean {
                mViewModel.loadImages(p0)
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean { return false }
        })

        listAdapter = ListImagesAdapter(this)
        fragmentBinding.recyclerView.layoutManager = GridLayoutManager(mainActivity, 2)
        fragmentBinding.recyclerView.adapter = listAdapter


        mViewModel.searchResult.observe(this, Observer {
            fragmentBinding.response = it
            listAdapter?.setImagesList(it.getData())
        })

        fragmentBinding.swipeRefresh.setOnRefreshListener {
            mViewModel.refresh()
        }

        if(mViewModel.term.value == null){
            mViewModel.term.value = "Russia World Cup"
        }

        if (mViewModel.searchResult.value == null) {
            mViewModel.loadImages(mViewModel.term.value)
        }
    }

    /**
     * Expand appBar when fragment resumes
     */
    override fun onResume() {
        super.onResume()
        fragmentBinding.appBar.setExpanded(true)
    }

    /**
     * call to repeat search request
     *
     * Retry button displayed only when api request was failed or empty
     */
    override fun retry() {
        mViewModel.refresh()
    }

    /**
     * Navigate to [ImageFragment]
     * after clicking one of the RecyclerView's items
     *
     * [PixabayImage] object that represents clicked item is passed as an argument
     */
    override fun onItemClick(pixabayImage: PixabayImage) {
        val action = ListImagesFragmentDirections.actionListImagesFragmentToImageFragment(pixabayImage)
        NavHostFragment.findNavController(this).navigate(action)
    }

}
