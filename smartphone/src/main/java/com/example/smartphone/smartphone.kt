package com.example.smartphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartphone.ui.theme.RelogioTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class smartphone : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelogioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = currentDate
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RelogioTheme {
        Greeting()
    }
}