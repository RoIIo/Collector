package eu.mobile.application.collector.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    open fun setListeners() {
    }

    open fun setObservers() {
    }
}