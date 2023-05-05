package eu.mobile.application.collector.fragment.positionEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryListBinding
import eu.mobile.application.collector.databinding.FragmentPositionEntryBinding
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import eu.mobile.application.collector.fragment.categoryList.CategoryListFragment
import eu.mobile.application.collector.fragment.categoryList.CategoryListViewModel
import eu.mobile.application.collector.fragment.positionDetails.PositionDetailsViewModel
import java.util.logging.Logger

@AndroidEntryPoint
class PositionEntryFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionEntryFragment()
        val logger = Logger.getLogger(PositionEntryFragment::class.simpleName)

    }
    private lateinit var viewBinding: FragmentPositionEntryBinding
    private val viewModel by viewModels<PositionEntryViewModel>()

    private val args: PositionEntryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_entry, container, false)
        viewBinding = FragmentPositionEntryBinding.bind(root).apply {
            viewModel = this@PositionEntryFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = args.category.name })
    }
}