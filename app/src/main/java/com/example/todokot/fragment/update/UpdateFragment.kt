package com.example.todokot.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todokot.R
import com.example.todokot.SharedViewModel
import com.example.todokot.data.models.Priority
import com.example.todokot.data.models.ToDoData
import com.example.todokot.data.viewmodel.ToDoViewModel
import com.example.todokot.databinding.FragmentUpdateBinding


class Update : Fragment() {
    lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding.currentTitleEt.setText(args.currentItem.title)
        binding.currentDescriptionEt.setText(args.currentItem.description)
        binding.currentSpinnerPriorites.setSelection(
            mSharedViewModel.parsePriorityToInt(args.currentItem.priority)
        )
        binding.currentSpinnerPriorites.onItemSelectedListener = mSharedViewModel.listener
// Return the root view of the binding
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    private fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemove()
        }
        return super.onOptionsItemSelected(item)
    }


    fun updateItem() {
        //Update current item
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val getPriority = binding.currentSpinnerPriorites.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataUser(title, description)
        if (validation) {
            val updateItem = ToDoData(
                args.currentItem.id, title, mSharedViewModel.parsePriority(getPriority),
                description
            )

            mToDoViewModel.updateData(updateItem)
            Toast.makeText(requireContext(), "Successfully update!!", Toast.LENGTH_SHORT).show()
            //Navigate back
            findNavController().navigate(R.id.action_update_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Show AlertDialog to confirm item Remove
    private fun confirmItemRemove() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully remove!! ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_update_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure you want to remove ${args.currentItem.title}?")
        builder.create().show()
    }
}