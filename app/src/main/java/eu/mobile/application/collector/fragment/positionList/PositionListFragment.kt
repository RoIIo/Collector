package eu.mobile.application.collector.fragment.positionList

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
import eu.mobile.application.collector.databinding.FragmentPositionListBinding
import java.util.logging.Logger
@AndroidEntryPoint
class PositionListFragment : Fragment()  {

    companion object {
        fun newInstance() = PositionListFragment()
        val logger = Logger.getLogger(PositionListFragment::class.simpleName)

    }
    private val args: PositionListFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPositionListBinding
    private val viewModel by viewModels<PositionListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_position_list, container, false)
        viewBinding = FragmentPositionListBinding.bind(root).apply {
            viewModel = this@PositionListFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var categoryId = args.categoryId
        viewModel.initialize(categoryId)
        setupObservers()
    }

    private fun setupObservers(){

        viewModel.positionListNotifier.observe(viewLifecycleOwner){

        }
        viewModel.positionPressed.observe(viewLifecycleOwner){
            if(it)
                goToPositionEntry()
        }
    }

    private fun goToPositionEntry(){
        findNavController().navigate(R.id.positionEntryFragmentDestination)
    }
}