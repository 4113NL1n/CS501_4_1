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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bucs501_4_1.ui.theme.BUCS501_4_1Theme
import org.xmlpull.v1.XmlPullParser

data class StoreItem(
    val name : String,
    val photo : String,
    val price : Int,
    val quantity : Int,
    val info : String
)
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
    var eventType = parser.eventType
    var nameitem = ""
    var photo = ""
    var price = 0
    var quantity = 0
    var info = ""
    val itemInStore = remember { mutableListOf<StoreItem>() }
    while(eventType != XmlPullParser.END_DOCUMENT){
        when(eventType){
            XmlPullParser.START_TAG -> {
                when(parser.name){
                    "name" -> nameitem = parser.nextText()
                    "photo" -> photo = parser.nextText()
                    "price" -> price = parser.nextText().toInt()
                    "quantity" -> quantity = parser.nextText().toInt()
                    "information" -> info = parser.nextText()
                }

            }
            XmlPullParser.END_TAG -> {
                when(parser.name) {
                    "item" ->  itemInStore.add(StoreItem(nameitem,photo,price,quantity,info))

                }
            }
        }
        eventType = parser.next()

    }
    Text(
        text = "Hello $itemInStore",
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