package eu.mobile.application.collector.fragment.positionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.adapter.PositionListAdapter
import eu.mobile.application.collector.databinding.FragmentPositionListBinding
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.event.SubtitleMessage
import java.util.logging.Level
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
    private lateinit var arrayAdapter: PositionListAdapter

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
        val category = args.category
        viewModel.initialize(category)
        setupList()
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = category.name})
    }

    private fun setupList() {
        if(viewModel.positionArrayNotifier.value == null) viewModel.positionArrayNotifier.value = arrayListOf()

        val arrayAdapter = PositionListAdapter(
            requireContext(),
            ArrayList(viewModel.positionArrayNotifier.value!!.map { it.name!! }),
            arrayListOf(),
            arrayListOf()
        )
        logger.log(Level.INFO, "Size ${viewModel.positionArrayNotifier.value?.size}")
        val list = viewBinding.listPosition
        list.adapter = arrayAdapter
        this.arrayAdapter = arrayAdapter
    }

    private fun setupObservers(){
        viewBinding.listPosition.setOnItemClickListener{ _: AdapterView<*>, _: View, position: Int, id: Long ->
            val position = viewModel.positionArrayNotifier.value?.get(position)
            if(position!= null)
                goToPositionDetails(position)
            else
                EventBusHandler.postMessage(Message().apply { message = "Wystąpił błąd podczas klikniecia kategorii" })

        }
        viewBinding.listPosition.setOnItemLongClickListener(){ _: AdapterView<*>, _: View, position: Int, id: Long->
            logger.log(Level.INFO, "Delete position:  $position")
            var selectedPosition = viewModel.positionArrayNotifier.value?.get(position)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.fragment_position_list_delete_title))
                .setMessage(resources.getString(R.string.fragment_position_list_delete_message) +
                        selectedPosition?.name)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    viewModel.deletePosition(position)
                }
                .setNegativeButton(android.R.string.no){ _, _ ->
                }.show()
            true
        }

        viewModel.positionArrayNotifier.observe(viewLifecycleOwner){
            logger.log(Level.INFO, "Category list changed size: ${viewModel.positionArrayNotifier.value?.size}")
            val arrayAdapter = PositionListAdapter(
                requireContext(),
                ArrayList(viewModel.positionArrayNotifier.value!!.map { it.name!! }),
                arrayListOf(),
                arrayListOf()
            )
            val list = viewBinding.listPosition
            list.adapter = arrayAdapter
            arrayAdapter.notifyDataSetChanged()
        }
        viewModel.positionPressed.observe(viewLifecycleOwner) {
            logger.log(Level.INFO, "Add position pressed")
            if(it)
                goToPositionEntry()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            logger.log(Level.INFO, "IsLoaded: $it")
            if (it)
                showLoading()
            else
                hideLoading()
        }
    }



    private fun hideLoading() {

    }

    private fun showLoading() {
    }

    private fun goToPositionDetails(position: Position) {
        val action = PositionListFragmentDirections.actionPositionListFragmentToPositionDetailsFragment(position)
        findNavController().navigate(action)
    }
    private fun goToPositionEntry(){
        val action = PositionListFragmentDirections.actionPositionListFragmentToPositionEntryFragment(args.category)
        findNavController().navigate(action)
    }
}