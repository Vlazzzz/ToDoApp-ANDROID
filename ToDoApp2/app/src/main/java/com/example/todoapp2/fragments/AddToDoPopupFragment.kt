package com.example.todoapp2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoapp2.R
import com.example.todoapp2.databinding.FragmentAddToDoPopupBinding
import com.example.todoapp2.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddToDoPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddToDoPopupBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var toDoData : ToDoData? = null

    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }


    //pentru a putea apela metoda newInstance fara a crea un obiect
    // de tip AddToDoPopupFragment folosim companion object
    companion object {
        const val TAG = "AddToDoPopupFragment"

        @JvmStatic //Anotarea indica faptul ca metoda nu este legata de un obiect specific
        fun newInstance(taskId:String, task:String) = AddToDoPopupFragment().apply {
            arguments = Bundle().apply {//Bundle este un container pentru date
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddToDoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //functie care se apeleaza dupa ce view-ul a fost creat
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null) {
            toDoData = ToDoData(arguments?.getString("taskId")!!, arguments?.getString("task")!!)
            binding.todoEt.setText(toDoData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            // Add the ToDo item to the database
            val todoTask = binding.todoEt.text.toString()
            if(todoTask.isNotEmpty()) {
                if(toDoData == null) {
                    listener.onSaveTask(todoTask, binding.todoEt)
                }
                else {
                    toDoData?.task = todoTask
                    listener.onUpdateTask(toDoData!!, binding.todoEt)
                }

            }else{
                Toast.makeText(context, "Please fill the task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }
    //todoTask retine task-ul introdus de utilizator in campul de text
    //toDoData retine task-ul si id-ul task-ului selectat

    interface DialogNextBtnClickListener {
        fun onSaveTask(todo : String, todoEt : TextInputEditText)
        fun onUpdateTask(todoData : ToDoData, todoEt : TextInputEditText)
    }
}