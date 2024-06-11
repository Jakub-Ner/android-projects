package com.example.exercise03

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StaticFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class StaticFragment : Fragment(), RadioGroup.OnCheckedChangeListener {
    interface OnSelectListener {
        fun onSelect(option: Int)
    }

    lateinit var actA: AppCompatActivity
    lateinit var listF1: OnSelectListener

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_static, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById<RadioGroup>(R.id.radioGroup)).setOnCheckedChangeListener(this)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StaticFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StaticFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            actA = context as AppCompatActivity
            listF1 = context as OnSelectListener
        } catch (e: ClassCastException) {
            throw ClassCastException("OnMyEventListener must be implemented")
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        Log.d("myTag", "onCheckedChanged: checkedId = $checkedId")
        when (checkedId) {
            R.id.option1 -> listF1.onSelect(1)
            R.id.option2 -> listF1.onSelect(2)
        }
    }
}