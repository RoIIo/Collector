package eu.mobile.application.collector.fragment.positionModify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentPositionModifyBinding
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import java.util.logging.Logger

@AndroidEntryPoint
class PositionModifyFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionModifyFragment()
        val logger = Logger.getLogger(PositionModifyFragment::class.simpleName)

    }
    val args: PositionModifyFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPositionModifyBinding
    private val viewModel by viewModels<PositionModifyFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_details, container, false)
        viewBinding = FragmentPositionModifyBinding.bind(root).apply {
            viewModel = this@PositionModifyFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = args.position
        viewModel.initialize(position)
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = position.name })
    }
}