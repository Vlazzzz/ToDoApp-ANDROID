package com.example.todoapp2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp2.R
import com.example.todoapp2.databinding.FragmentHomeBinding
import com.example.todoapp2.utils.ToDoAdapter
import com.example.todoapp2.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), AddToDoPopupFragment.DialogNextBtnClickListener, ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var databaseRef: DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddToDoPopupFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList:MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.addBtnHome.setOnClickListener {
            if(popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

            popUpFragment = AddToDoPopupFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager, AddToDoPopupFragment.TAG)
        }

        // Logout button click listener
        binding.logoutBtnHome.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            logout()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        // Get the reference of the Firebase database(Change with Room)
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase()
    {
        databaseRef.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(takeSnapshot in snapshot.children) {
                    val todoTask = takeSnapshot.key?.let {
                        ToDoData(it, takeSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun logout() {

        auth.signOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.signInFragment, true)
            .build()
        navControl.navigate(R.id.action_homeFragment_to_signInFragment, null, navOptions)
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if(it.isSuccessful) {
                todoEt.text = null
                Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
        popUpFragment?.dismiss()
    }

    override fun onUpdateTask(todoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[todoData.taskId] = todoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful) {
                todoEt.text = null
                Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
        }

        popUpFragment?.dismiss()
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful)
                Toast.makeText(context, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        if(popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment = AddToDoPopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager, AddToDoPopupFragment.TAG)
    }
}
