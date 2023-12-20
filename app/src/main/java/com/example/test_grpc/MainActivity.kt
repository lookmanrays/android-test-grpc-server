package com.example.test_grpc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test_grpc.client.HelloWorldClient
import com.example.test_grpc.databinding.ActivityMainBinding
import com.example.test_grpc.server.HelloWorldServer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var server: HelloWorldServer
    private val client = HelloWorldClient("localhost", 7777)
    private var textToShow = MutableStateFlow("")

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToShow = MutableStateFlow(resources.getString(R.string.waiting_for_response))

        // Launching a thread
        Thread {
            server = HelloWorldServer(7777)
            server.start()
        }.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.button)
        val textView: TextView = findViewById(R.id.textViewResponse)

        GlobalScope.launch(Dispatchers.Main) {
            textToShow.collect { newText ->
                textView.text = newText
            }
        }

//        textView.text = textToShow.value

        button.setOnClickListener {
            onButtonClickListener()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onButtonClickListener() // Replace with your actual function
        }, 500) // Delay in milliseconds
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onButtonClickListener() {
        textToShow.value = resources.getString(R.string.waiting_for_response)
        GlobalScope.launch(Dispatchers.IO) {
            val response = client.sayHello("Norris") // replace with your actual gRPC method
            withContext(Dispatchers.Main) {
                textToShow.value = response
            }
        }
    }
}