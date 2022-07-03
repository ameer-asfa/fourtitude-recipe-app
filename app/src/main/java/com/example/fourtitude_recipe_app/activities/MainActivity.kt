package com.example.fourtitude_recipe_app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.fourtitude_recipe_app.R
import com.example.fourtitude_recipe_app.database.DatabaseHandler
import com.example.fourtitude_recipe_app.model.RecipeModelClass
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Login as Admin
        val btnLogin = btnLogin
        btnLogin.setOnClickListener{
            login()
        }


        // Login as Guest
        val buttonClick = login_guest
        buttonClick.setOnClickListener{
            val intent = Intent(this, RecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        if (!username.isEmpty() && !password.isEmpty()) {
            val status = databaseHandler.login(username, password)

            if (status) {
                // Initializing Shared Preferences
                val sharedPreferences : SharedPreferences = this.getSharedPreferences("ADMIN_PREFS", Context.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putString("username", "admin")
                editor.apply()
                editor.commit()

                // Changing Page
                val intent = Intent(this, RecipeActivity::class.java)
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Invalid Login")
                builder.setMessage("Wrong Username or Password")

                builder.setPositiveButton("Okay") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }

                builder.setNegativeButton("Cancel") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }

                // Creating the alert dialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
                alertDialog.show()
            }
        }
    }
}