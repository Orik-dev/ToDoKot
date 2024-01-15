package com.example.todokot.fragment.list

import android.app.AlertDialog
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todokot.data.viewmodel.ToDoViewModel
import com.example.todokot.R
import com.example.todokot.SharedViewModel
import com.example.todokot.data.models.ToDoData
import com.example.todokot.databinding.FragmentListBinding
import com.example.todokot.fragment.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = this._binding!!
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        this._binding = FragmentListBinding.inflate(inflater, container, false)

        //Setup RecyclerView
        setupRecyclerView()

        // Observe LiveData and update the adapter
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })
        // Set up FAB click listener to navigate to the add fragment
        binding.floatingActionButton4.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Set menu
        setHasOptionsMenu(true)

        return this._binding?.root
    }

    private fun setupRecyclerView() {
        val recyclerView = this._binding?.rcView
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView?.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        if (recyclerView != null) {
            swipeToDelete(recyclerView)
        }

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                //Delete Item
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //Restore delete item
                restoreDeleteData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteData(view: View, deletedItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted ${deletedItem.title}",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }


    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            this.binding.noDataImg.visibility = View.VISIBLE
            this.binding.noDataTv.visibility = View.VISIBLE
        } else {
            this.binding.noDataImg.visibility = View.INVISIBLE
            this.binding.noDataTv.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemove()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show Alert Dialog Removal all
    private fun confirmRemove() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything!!")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }
}