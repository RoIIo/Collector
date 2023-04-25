package eu.mobile.application.collector.fragment.positionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryDetailsBinding
import eu.mobile.application.collector.databinding.FragmentPositionDetailsBinding
import eu.mobile.application.collector.fragment.BaseFragment
import eu.mobile.application.collector.fragment.categoryDetails.CategoryDetailsFragment
import eu.mobile.application.collector.fragment.categoryDetails.CategoryDetailsViewModel
import eu.mobile.application.collector.fragment.mainMenu.MainFragment
import java.util.logging.Logger
@AndroidEntryPoint
class PositionDetailsFragment : BaseFragment<FragmentPositionDetailsBinding, PositionDetailsViewModel>() {

    companion object {
        fun newInstance() = MainFragment()
        val logger = Logger.getLogger(PositionDetailsFragment::class.simpleName)

    }

    private lateinit var viewBinding: FragmentPositionDetailsBinding
    private val viewModel by viewModels<PositionDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_details, container, false)
        viewBinding = FragmentPositionDetailsBinding.bind(root).apply {
            viewModel = this@PositionDetailsFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}