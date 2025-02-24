package com.example.bucs501_4_1

import android.os.Bundle
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bucs501_4_1.ui.theme.BUCS501_4_1Theme
import org.xmlpull.v1.XmlPullParser

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BUCS501_4_1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
//    val contextConfig = LocalConfiguration.current
    val contextContext = LocalContext.current
    val parser = contextContext.resources.getXml(R.xml.store_item)

//    val height = contextConfig.screenHeightDp.dp
//    val width = contextConfig.screenWidthDp.dp
    Text(
        text = "Hello $name! ${parser.eventType}",
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars),
        color = Color(100,10,200)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BUCS501_4_1Theme {
        Greeting("Android")
    }
}