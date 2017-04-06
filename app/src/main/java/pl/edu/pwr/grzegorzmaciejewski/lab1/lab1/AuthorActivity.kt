package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1

/**
 * Created by PanG on 04.04.2017.
 */

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class AuthorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}