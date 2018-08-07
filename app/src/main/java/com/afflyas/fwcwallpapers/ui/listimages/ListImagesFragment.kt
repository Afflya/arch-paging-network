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
import androidx.paging.PagedList

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
     * Expand appBar when fragment resumes
     */
    override fun onResume() {
        super.onResume()
        fragmentBinding.appBar.setExpanded(true)
    }

    /**
     * Navigate to ImageFragment
     * after clicking one of the RecyclerView's items
     *
     * PixabayImage object that represents clicked item is passed as an argument
     */
    override fun onItemClick(pixabayImage: PixabayImage) {
        val action = ListImagesFragmentDirections.actionListImagesFragmentToImageFragment(pixabayImage)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun subscribeUI() {
        subscripeAdapter()
        subscribeSwipeRefresh()
        subscribeSearch()
    }

    private fun subscripeAdapter(){
        val adapter = ImagesPagedAdapter(this, this)

        if(mViewModel.currentQuery() == null){
            mViewModel.loadImages("Russia World Cup")
        }

        fragmentBinding.recyclerView.layoutManager = GridLayoutManager(mainActivity, 2)
        fragmentBinding.recyclerView.adapter = adapter

        mViewModel.images.observe(this, Observer<PagedList<PixabayImage>> {
            adapter.submitList(it)
            fragmentBinding.hasItems = it.isNotEmpty()
        })

        mViewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

    }

    private fun subscribeSwipeRefresh(){
        mViewModel.refreshState.observe(this, Observer {
            fragmentBinding.refreshState = it
        })

        fragmentBinding.swipeRefresh.setOnRefreshListener {
            mViewModel.refresh()
        }
    }

    private fun subscribeSearch(){
        fragmentBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty()){
                    if(mViewModel.loadImages(query)){
                        fragmentBinding.recyclerView.scrollToPosition(0)
                        (fragmentBinding.recyclerView.adapter as? ImagesPagedAdapter)?.submitList(null)
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean { return false }
        })
    }

    /**
     * call to retry search request
     *
     * Retry button displayed only when api request was failed
     */
    override fun retry() {
        mViewModel.retry()
    }
}
