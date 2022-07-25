package com.alons.marvel_universe.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alons.marvel_universe.R
import com.alons.marvel_universe.R.id.favorite
import com.alons.marvel_universe.databinding.ActivityMainBinding
import com.alons.marvel_universe.domain.model.CharacterModel
import com.alons.marvel_universe.ui.CharacterList.CharactersViewModel
import com.alons.marvel_universe.util.CharacterListAdapter
import com.alons.marvel_universe.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    val charactersViewModel: CharactersViewModel by viewModels()
    private lateinit var searchTerm: String
    var valueRepeat = 3
    var paginatedValue = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(binding.root)
        recyclerView = binding.charactersRecyclerView
        layoutManager = GridLayoutManager(this, 2)
        bottomNav = binding.bottomNavigation
        appBarConfiguration = AppBarConfiguration(bottomNav.menu)
        recyclerViewCharacters()
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

            }
            false
        }

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
        Log.d("tag", Constants.timeStamp)

    }

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
                            Toast.makeText(this@MainActivity, value.error, Toast.LENGTH_LONG).show()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.menuSearch)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerViewCharacters() {
        recyclerView = binding.charactersRecyclerView
        adapter = CharacterListAdapter(this, ArrayList())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchTerm = query
        }
        if (searchTerm.isNotEmpty()) {
            search()
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchTerm = query
        }
        if (searchTerm.isNotEmpty()) {
            search()
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun search() {
        charactersViewModel.getSearchedCharacters(searchTerm)
        CoroutineScope(Dispatchers.Main).launch {
            charactersViewModel._marvelValue.collect {
                when {
                    it.isLoading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    it.error.isNotBlank() -> {
                        binding.progressCircular.visibility = View.GONE
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                    it.characterList.isNotEmpty() -> {
                        binding.progressCircular.visibility = View.GONE
                        adapter.setData(it.characterList as ArrayList<CharacterModel>)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        charactersViewModel.getAllCharactersData(paginatedValue)
        callAPI()
    }
}
