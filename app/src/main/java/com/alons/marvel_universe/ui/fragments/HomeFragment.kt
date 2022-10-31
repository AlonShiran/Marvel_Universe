package com.alons.marvel_universe.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.databinding.FragmentHomeBinding
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.ui.characterList.CharactersViewModel
import com.alons.marvel_universe.util.CharacterListAdapter
import com.alons.marvel_universe.util.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home){
    val charactersViewModel: CharactersViewModel by viewModels()
    private lateinit var searchTerm: String
    private var valueRepeat = 3
    var paginatedValue = 0
    private var _binding: FragmentHomeBinding? = null
    private val binding by lazy { _binding!! }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = GridLayoutManager(context, 2)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.charactersRV
        recyclerViewCharacters()
        Log.d("tag", Constants.timeStamp)
        charactersViewModel.getAllCharactersData(paginatedValue)
        callAPI()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                    paginatedValue += 20
                    charactersViewModel.getAllCharactersData(paginatedValue)
                    callAPI()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //calling the marvel API and fill the view model if succeeded
    private fun callAPI() {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(valueRepeat) {
                charactersViewModel._marvelValue.collect { value ->
                    when {
                        value.isLoading -> {
                            binding.progressCircular.visibility = View.VISIBLE
                        }
                        value.error.isNotBlank() -> {
                            binding.progressCircular.visibility = View.GONE
                            valueRepeat = 0
                            Toast.makeText(context, value.error, Toast.LENGTH_LONG).show()
                        }
                        value.characterList.isNotEmpty() -> {
                            binding.progressCircular.visibility = View.GONE
                            valueRepeat = 0
                            adapter.setData(value.characterList as ArrayList<CharacterModel>)
                        }
                    }
                    delay(1000)
                }
            }
        }
    }
    //method to initialize the Recycler View
    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerViewCharacters() {
        recyclerView = binding.charactersRV
        adapter = CharacterListAdapter(requireContext(), ArrayList())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

}