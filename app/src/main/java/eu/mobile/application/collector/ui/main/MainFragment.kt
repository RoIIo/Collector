package eu.mobile.application.collector.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import eu.mobile.application.collector.R
import eu.mobile.application.collector.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {


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
        //setupObservers

    }

    companion object {
        fun newInstance() = MainFragment()
    }
}