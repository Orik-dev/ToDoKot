package com.example.todokot.fragment.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.todokot.R
import com.example.todokot.data.models.Priority


class Update : Fragment() {

    private val args by navArgs<UpdateArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update, container, false)
        setHasOptionsMenu(true)

        view.findViewById<EditText>(R.id.current_title_et).setText(args.currentItem.title)
        view.findViewById<EditText>(R.id.current_description_et)
            .setText(args.currentItem.description)
        view.findViewById<Spinner>(R.id.current_spinner_priorites)
            .setSelection(parsePriority(args.currentItem.priority))

        // Inflate the layout for this fragment
        return view
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
}