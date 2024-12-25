package com.kashifshahazad.smartagriassistant

import CropIssueAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.kashifshahazad.smartagriassistant.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class homeFragment : Fragment() {
    lateinit var adapter: CropIssueAdapter
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeFragmentViewModel
    lateinit var authViewModel: AuthViewModel
    lateinit var CurrentUser:FirebaseUser
    val items=ArrayList<CropIssue>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel=AuthViewModel()
            viewModel=HomeFragmentViewModel()
        lifecycleScope.launch {
            authViewModel.currentUser.collect {
                it?.let {
                    CurrentUser = it
                        viewModel.readAllOrders()
                }
            }
        }
        viewModel.readAllOrders()


        adapter= CropIssueAdapter(items)
        binding.recyclerview.adapter=adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(context)
        lifecycleScope.launch {
            viewModel.dataa.collect {
                it?.let {
                    Log.i("ABC", "Test")
                    items.clear()
                    items.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }




        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}