package com.minhkhue.note.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.minhkhue.note.R
import com.minhkhue.note.databinding.FragmentSettingBinding
import com.minhkhue.note.utils.DataEncryptorModern
import java.util.regex.Pattern

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataEncryptorModern: DataEncryptorModern
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataEncryptorModern = DataEncryptorModern()
        binding.btnDone.setOnClickListener {
            val password = binding.edtPass.text.toString().trim()
            val confirmPass = binding.edtConfirmPass.text.toString().trim()
            Log.d("TAG", "$password $confirmPass")
            if (isValidPassword(password)) {
                if (confirmPass == password) {
                    val sharedPreferences: SharedPreferences =
                        activity?.getSharedPreferences("password", Context.MODE_PRIVATE)
                            ?: return@setOnClickListener
                    with(sharedPreferences.edit()) {
                        putString("pass", dataEncryptorModern.encryptString(password))
                        apply()
                    }
                    findNavController().navigate(R.id.action_settingFragment_to_homeFragment)
                } else {
                    Snackbar.make(binding.root, "Password not matched", Snackbar.LENGTH_SHORT).show()
                }

            } else {
                Snackbar.make(binding.root, "Invalid Password", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }
}