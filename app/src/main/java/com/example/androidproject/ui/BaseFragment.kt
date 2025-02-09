package com.example.androidproject.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias InflateMethod<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<T : ViewBinding>(
    private val inflateMethod: InflateMethod<T>
) : Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun createTextWatcher(onTextChanged: () -> Unit): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                // no-op
            }
        }
}