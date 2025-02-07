package com.example.trashmobie.Presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.trashmobie.R
import com.example.trashmobie.databinding.FragmentStatisticalBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding: FragmentStatisticalBinding
class FragmentStatistical : Fragment(R.layout.fragment_statistical) {

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding = FragmentStatisticalBinding.inflate(layoutInflater)

        }



}
