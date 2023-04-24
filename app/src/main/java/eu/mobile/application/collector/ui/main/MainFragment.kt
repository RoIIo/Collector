package eu.mobile.application.collector.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentMainBinding
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    companion object {
        fun newInstance() = MainFragment()
        val logger = Logger.getLogger(MainFragment::class.simpleName)

    }
    private lateinit var viewBinding: FragmentMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        viewBinding = FragmentMainBinding.bind(root).apply {
            viewModel = this@MainFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize()
        setupList()
        setupObservers()
    }

    private fun setupList() {
        val list = viewBinding.categoryList
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, viewModel.categoryArray.value!!.map { it.name }.toTypedArray())
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupObservers(){

        //binding.categoryList.setOnItemClickListener{ _: AdapterView<*>, _: View, i: Int, l: Long ->
        //    goToCategoryDetails(i)
        //}
        viewModel.categoryArray.observe(viewLifecycleOwner){
           val list = viewBinding.categoryList
         val adapter = list.adapter as ArrayAdapter<*>
        adapter.notifyDataSetChanged()
        }
        viewModel.addCategoryPressed.observe(viewLifecycleOwner) {
            logger.log(Level.INFO, "Add category pressed")
            goToAddCategory()
        }
        viewModel.isLoaded.observe(viewLifecycleOwner) {
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

    private fun goToAddCategory(){
    }
    private fun goToCategoryDetails(positionId: Int){
    }



}