package com.example.todokot.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todokot.data.viewmodel.ToDoViewModel
import com.example.todokot.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {
    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView =  view.findViewById<RecyclerView>(R.id.rcView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {data->
            adapter.setData(data)

        })


        view.findViewById<FloatingActionButton>(R.id.floatingActionButton4).setOnClickListener {
            findNavController().navigate(R.id.action_add_to_listFragment)

        }

        //Set menu
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.list_fragment_menu,menu)
    }
}