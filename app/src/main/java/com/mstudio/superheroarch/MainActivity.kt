package com.mstudio.superheroarch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mstudio.superheroarch.ApiService.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val adapter = CharacterAdapter()
    private lateinit var apiRick: ApiRick

    companion object {
        const val EXTRA_NAME = "com.mstudio.superheroarch.MainActivity.EXTRA_NAME"
        const val EXTRA_STATUS = "com.mstudio.superheroarch.MainActivity.EXTRA_STATUS"
        const val EXTRA_IMAGE = "com.mstudio.superheroarch.MainActivity.EXTRA_IMAGE"
        const val EXTRA_LOCATION = "com.mstudio.superheroarch.MainActivity.EXTRA_LOCATION"
        const val EXTRA_ORIGIN= "com.mstudio.superheroarch.MainActivity.EXTRA_ORIGIN"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        apiRick = retrofit.create(ApiRick::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.main_button)
        val characterRecyclerView = findViewById<RecyclerView>(R.id.characters_recycler)

        characterRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : CharacterAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val character = adapter.characters[position]
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                intent.putExtra(EXTRA_NAME, character.name)
                intent.putExtra(EXTRA_STATUS, character.status)
                intent.putExtra(EXTRA_IMAGE, character.image)
                intent.putExtra(EXTRA_LOCATION, character.location.name)
                intent.putExtra(EXTRA_ORIGIN, character.origin.name)
                startActivity(intent)
            }
        })

        button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiRick.getCharacter()

                if (response.isSuccessful) {
                    val characterResponse = response.body()
                    val characters = characterResponse?.results ?: emptyList()

                    withContext(Dispatchers.Main) {
                        if (characters.isNotEmpty()){
                            button.visibility = View.GONE
                        }else{
                            Snackbar.make(characterRecyclerView, R.string.failed_fetch_data, Snackbar.LENGTH_LONG).show()
                        }
                        adapter.updateCharacters(characters)
                    }
                } else {
                    withContext(Dispatchers.Main){
                        Snackbar.make(characterRecyclerView, R.string.failed_fetch_data, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
