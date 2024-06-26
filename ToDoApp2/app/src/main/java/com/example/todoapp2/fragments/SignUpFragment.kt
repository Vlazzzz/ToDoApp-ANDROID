package com.example.todoapp2.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp2.R
import com.example.todoapp2.databinding.FragmentSignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.btnSignInRedirect.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val verifyPass = binding.verifyPassEt.text.toString().trim()

            if (!email.isEmpty() && !pass.isEmpty() && !verifyPass.isEmpty()) {
                if (pass.equals(verifyPass)) {
                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Save the user's email and password in SharedPreferences
                            val sharedPreferences: SharedPreferences =
                                requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("user_email", email)
                            editor.putString("user_password", pass)
                            editor.apply()

                            Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT)
                                .show()
                            navControl.navigate(R.id.action_signUpFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                context,
                                "Error: ${it.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

}