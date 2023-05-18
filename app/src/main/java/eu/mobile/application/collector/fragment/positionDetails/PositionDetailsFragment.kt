package eu.mobile.application.collector.fragment.positionDetails

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentPositionDetailsBinding
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.SubtitleMessage
import java.util.logging.Logger
@AndroidEntryPoint
class PositionDetailsFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionDetailsFragment()
        val logger = Logger.getLogger(PositionDetailsFragment::class.simpleName)

    }
    val args: PositionDetailsFragmentArgs by navArgs()
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

        var position = args.position
       // findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Position>("position")?.observe(viewLifecycleOwner){
       //     position = it
       // }
        viewModel.initialize(position)
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = position.name })
    }
    private fun setupObservers(){

        viewModel.positionImageNotifier.observe(viewLifecycleOwner){
            viewBinding.fragmentPositionDetailsImage.setImageBitmap(it)
        }
        viewModel.modifyPositionLiveData.observe(viewLifecycleOwner){
            if(it)
                goToPositionModify()
        }
    }

    private fun goToPositionModify() {
        val action = PositionDetailsFragmentDirections.actionPositionDetailsFragmentToPositionModifyFragment(args.position)
        findNavController().navigate(action)
    }
}