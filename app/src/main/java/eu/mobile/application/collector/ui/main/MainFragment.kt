package eu.mobile.application.collector.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentMainBinding
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.event.ErrorHandler
import eu.mobile.application.collector.event.Message
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
    private lateinit var arrayAdapter: ArrayAdapter<*>

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
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.row_category,
            viewModel.categoryArray.value?.map { it.name } ?: arrayListOf()
        )
        val customAdapter = arrayAdapter
        logger.log(Level.INFO, "Size ${viewModel.categoryArray.value?.size}")
        val list = viewBinding.listCategory
        this.arrayAdapter = customAdapter
        list.adapter = this.arrayAdapter
    }

    private fun setupObservers(){

        viewBinding.listCategory.setOnItemClickListener{ _: AdapterView<*>, _: View, position: Int, id: Long ->
            logger.log(Level.INFO, "Go to category details: ${id}")
            logger.log(Level.INFO, "Go to category details Time: $id")
            val category = viewModel.categoryArray.value?.get(position)
            if(category!= null)
                goToCategoryDetails(category)
            else
                ErrorHandler.postMessageEvent(Message().apply { message = "Wystąpił błąd podczas klikniecia kategorii" })

        }
        viewBinding.listCategory.setOnItemLongClickListener(){ _: AdapterView<*>, _: View, position: Int, id: Long->
            logger.log(Level.INFO, "Delete category:  $position")
            var selectedCategory = viewModel.categoryArray.value?.get(position)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.fragment_main_fragment_category_delete_title))
                .setMessage(resources.getString(R.string.fragment_main_fragment_category_delete_message) +
                        selectedCategory?.name)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    viewModel.deleteCategory(position)
                }
                .setNegativeButton(android.R.string.no){ _, _ ->
                }.show()
            true
        }

        viewModel.categoryArray.observe(viewLifecycleOwner){
            logger.log(Level.INFO, "Category list changed size: ${viewModel.categoryArray.value?.size}")
            var arrayAdapter1 = ArrayAdapter(
                requireContext(),
                R.layout.row_category,
                viewModel.categoryArray.value?.map { it.name } ?: arrayListOf()
            )
            val list = viewBinding.listCategory
            list.adapter = arrayAdapter1
            list.invalidate()
            list.refreshDrawableState()

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
    private fun goToCategoryDetails(category: Category){
    }



}