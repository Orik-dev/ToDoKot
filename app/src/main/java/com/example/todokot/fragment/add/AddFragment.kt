package com.example.todokot.fragment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todokot.data.models.ToDoData
import com.example.todokot.data.viewmodel.ToDoViewModel
import com.example.todokot.R
import com.example.todokot.databinding.FragmentAddBinding
import com.example.todokot.SharedViewModel


class Add : Fragment() {
    private var binding: FragmentAddBinding? = null
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding!!.spinnerPriorites.onItemSelectedListener = mSharedViewModel.listener
        return binding?.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding?.titleEt?.text.toString()
        val mPriority = binding?.spinnerPriorites?.selectedItem.toString()
        val mDescription = binding?.descriptionEt?.text.toString()

        val validation = mSharedViewModel.verifyDataUser(mTitle, mDescription)
        if (validation) {
            val newData =
                ToDoData(0, mTitle, mSharedViewModel.parsePriority(mPriority), mDescription)
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added!!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_add_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }


}