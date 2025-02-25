package com.example.bucs501_4_1

import android.content.Context
import android.os.Bundle
import android.util.Xml
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bucs501_4_1.ui.theme.BUCS501_4_1Theme
import org.xmlpull.v1.XmlPullParser

data class StoreItem(
    val name : String,
    val photo : String,
    val price : Int,
    var quantity : Int,
    val info : String
)
data class SizeScreen(
    val width : Int,
    val height : Int
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
                    VerticalPane(
                        name = "Android",
                        modifier = Modifier
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.systemBars)
                    )
                }
            }
        }
    }
}

@Composable
fun VerticalPane(name: String, modifier: Modifier = Modifier) {
//    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
//    val contextConfig = LocalConfiguration.current


//    var isProductSelected = rememberSaveable{ mutableStateOf(false)}
//
//    var currProduct  = rememberSaveable { mutableStateOf(StoreItem("","",0,0,"")) }
    val size = screenSize()
//    val width = size.width
//    val height = size.height
    val contextContext = LocalContext.current
    val itemInStore = parse(contextContext)
    Column(
        modifier = modifier.fillMaxWidth().background(color = Color(255,255,255)),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Text(
            text = "Shopping List",
            color = Color(100,10,200),
            fontSize = 40.sp
        )


        LazyColumn(
            modifier = modifier.padding(bottom = 10.dp).fillMaxWidth().background(color = Color(255,255,255))
        ) {
            items(itemInStore){ item ->
                Row (
                    modifier = Modifier.fillMaxSize().clickable {  }.background(color = Color(10,100,100))
                ){
                    Text(
                        text = item.name,
                        fontSize = 25.sp
                    )
                }
            }
        }
    }


}

@Composable
fun screenSize() : SizeScreen{
    val configContext = LocalConfiguration.current
    val width = remember {configContext.screenWidthDp}
    val height = remember { configContext.screenHeightDp}
    return SizeScreen(width,height)
}
fun parse(context : Context) : List<StoreItem>{
    val parser = context.resources.getXml(R.xml.store_item)
    var eventType = parser.eventType
    var nameItem = ""
    var photo = ""
    var price = 0
    var quantity = 0
    var info = ""
    val itemInStore = mutableListOf<StoreItem>()
    while(eventType != XmlPullParser.END_DOCUMENT){
        when(eventType){
            XmlPullParser.START_TAG -> {
                when(parser.name){
                    "name" -> nameItem = parser.nextText()
                    "photo" -> photo = parser.nextText()
                    "price" -> price = parser.nextText().toInt()
                    "quantity" -> quantity = parser.nextText().toInt()
                    "information" -> info = parser.nextText()
                }
            }
            XmlPullParser.END_TAG -> {
                when(parser.name) {
                    "item" ->  itemInStore.add(StoreItem(nameItem,photo,price,quantity,info))
                }
            }
        }
        eventType = parser.next()
    }
    return  itemInStore
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BUCS501_4_1Theme {
        VerticalPane("Android")
    }
}