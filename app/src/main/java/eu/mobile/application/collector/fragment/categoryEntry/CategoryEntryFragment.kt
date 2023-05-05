package eu.mobile.application.collector.fragment.categoryEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryEntryBinding
import eu.mobile.application.collector.databinding.FragmentCategoryListBinding
import eu.mobile.application.collector.fragment.categoryList.CategoryListFragment
import eu.mobile.application.collector.fragment.categoryList.CategoryListViewModel
import java.util.logging.Logger

@AndroidEntryPoint
class CategoryEntryFragment : Fragment()  {
    companion object {
        fun newInstance() = CategoryEntryFragment()
        val logger = Logger.getLogger(CategoryEntryFragment::class.simpleName)
    }

    private lateinit var viewBinding: FragmentCategoryEntryBinding
    private val viewModel by viewModels<CategoryEntryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_category_entry, container, false)
        viewBinding = FragmentCategoryEntryBinding.bind(root).apply {
            viewModel = this@CategoryEntryFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.addedCategoryLiveData.observe(viewLifecycleOwner){
            if(it){
                goToListCategory()
            }
        }
    }

    private fun goToListCategory(){
        if(!findNavController().popBackStack(R.id.categoryListFragmentDestination,false))
            findNavController().navigate(R.id.categoryListFragmentDestination)
    }
}