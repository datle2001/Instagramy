package depauw.datle.instagramy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.parse.ParseUser
import depauw.datle.instagramy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val bt_login_clickListener = View.OnClickListener {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        loginUser(username, password)
    }
    private val bt_signUp_clickListener = View.OnClickListener {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        signUpUser(username, password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //check if the user is logged in
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }
        binding.btLogin.setOnClickListener(bt_login_clickListener)
        binding.btnSignUp.setOnClickListener(bt_signUp_clickListener)
    }
    private fun signUpUser(username: String, password: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
                Toast.makeText(this, "Sign up successfully", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({user, e  ->
            if (user != null) {
                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }))
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    companion object {
        val TAG = "LoginActivity"
    }
}