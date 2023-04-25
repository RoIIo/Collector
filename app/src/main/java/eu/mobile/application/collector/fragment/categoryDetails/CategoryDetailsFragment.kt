package eu.mobile.application.collector.fragment.categoryDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryDetailsBinding
import eu.mobile.application.collector.databinding.FragmentMainBinding
import eu.mobile.application.collector.fragment.BaseFragment
import eu.mobile.application.collector.fragment.mainMenu.MainFragment
import eu.mobile.application.collector.fragment.mainMenu.MainViewModel
import java.util.logging.Logger
@AndroidEntryPoint
class CategoryDetailsFragment : BaseFragment<FragmentCategoryDetailsBinding, CategoryDetailsViewModel>() {

    companion object {
        fun newInstance() = MainFragment()
        val logger = Logger.getLogger(CategoryDetailsFragment::class.simpleName)

    }
    private lateinit var viewBinding: FragmentCategoryDetailsBinding
    private val viewModel by viewModels<CategoryDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_category_details, container, false)
        viewBinding = FragmentCategoryDetailsBinding.bind(root).apply {
            viewModel = this@CategoryDetailsFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}