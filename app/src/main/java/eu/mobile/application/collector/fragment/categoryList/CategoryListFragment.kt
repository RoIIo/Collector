package eu.mobile.application.collector.fragment.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentCategoryListBinding
import eu.mobile.application.collector.entity.Category
import eu.mobile.application.collector.entity.Position
import eu.mobile.application.collector.event.EventBusHandler
import eu.mobile.application.collector.event.Message
import eu.mobile.application.collector.event.SubtitleMessage
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryListFragment()
        val logger = Logger.getLogger(CategoryListFragment::class.simpleName)

    }
    private lateinit var viewBinding: FragmentCategoryListBinding
    private val viewModel by viewModels<CategoryListViewModel>()
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_category_list, container, false)
        viewBinding = FragmentCategoryListBinding.bind(root).apply {
            viewModel = this@CategoryListFragment.viewModel
        }
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize()
        setupList()
        setupSearch()
        setupObservers()
        EventBusHandler.postSubtitle(SubtitleMessage().apply { name = "" })
    }

    private fun setupSearch(){
        var searchView = viewBinding.fragmentCategoryListSearch
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query == null)
                    return false
                if (viewModel.categoryArrayNotifier.value!!.any { category->
                        category.name?.contains(query) == true}) {
                    arrayAdapter.filter.filter(query)
                } else {
                    EventBusHandler.postMessage(Message().apply { message= "Nie znaleziono żadnej kategorii"})
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupList() {
        val arrayAdapter: ArrayAdapter<*>
        if(viewModel.categoryArrayNotifier.value == null) viewModel.categoryArrayNotifier.value = arrayListOf()

        viewModel.categoryArrayNotifier.value!!.sortWith { obj1, obj2 ->
            obj1.name!!.compareTo(obj2.name!!)
        }
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.row_category,
            viewModel.categoryArrayNotifier.value!!.map { it.name }
        )

        logger.log(Level.INFO, "Size ${viewModel.categoryArrayNotifier.value?.size}")
        val list = viewBinding.fragmentCategoryListListCategory
        list.adapter = arrayAdapter
        this.arrayAdapter = arrayAdapter
    }

    private fun setupObservers(){

        viewBinding.fragmentCategoryListListCategory.setOnItemClickListener{ _: AdapterView<*>, _: View, position: Int, id: Long ->
            val category = viewModel.categoryArrayNotifier.value?.get(position)
            if(category!= null)
                goToPositionList(category)
            else
                EventBusHandler.postMessage(Message().apply { message = "Wystąpił błąd podczas klikniecia kategorii" })

        }
        viewBinding.fragmentCategoryListListCategory.setOnItemLongClickListener(){ _: AdapterView<*>, _: View, position: Int, id: Long->
            logger.log(Level.INFO, "Delete category:  $position")
            var selectedCategory = viewModel.categoryArrayNotifier.value?.get(position)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.fragment_category_list_delete_title))
                .setMessage(resources.getString(R.string.fragment_category_list_delete_message) +
                        selectedCategory?.name)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    viewModel.deleteCategory(position)
                }
                .setNegativeButton(android.R.string.no){ _, _ ->
                }.show()
            true
        }

        viewModel.categoryArrayNotifier.observe(viewLifecycleOwner){
            logger.log(Level.INFO, "Category list changed size: ${viewModel.categoryArrayNotifier.value?.size}")
            setupList()
        }
        viewModel.categoryEntryPressed.observe(viewLifecycleOwner) {
            logger.log(Level.INFO, "Add category pressed")
            if(it)
                goToCategoryEntry()
        }
    }

    private fun goToCategoryEntry(){
        findNavController().navigate(R.id.categoryEntryFragmentDestination)
    }
    private fun goToPositionList(category: Category){
        if(category.Id == null){
            EventBusHandler.postMessage(Message().apply { message="There is no ID in category: ${category.name}" })
            return
        }
        val action = CategoryListFragmentDirections.actionCategoryListFragmentToPositionListFragment(category)
        findNavController().navigate(action)
    }



}