package com.example.bucs501_4_1

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bucs501_4_1.ui.theme.BUCS501_4_1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.xmlpull.v1.XmlPullParser

@Parcelize
data class StoreItem(
    val name : String,
    val photo : String,
    val price : Int,
    var quantity : Int,
    val info : String
) : Parcelable

data class SizeScreen(
    val width: Int,
    val height: Int
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BUCS501_4_1Theme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    modifier = Modifier.fillMaxSize()

                ) { innerPadding ->
                    Pane(
                        modifier = Modifier
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.systemBars),
                        scope,
                        snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun Pane( modifier: Modifier = Modifier, scope : CoroutineScope, snackbarHostState: SnackbarHostState ) {
//
    val currProduct  = rememberSaveable { mutableStateOf<StoreItem?>(null) }
    val size = screenSize()
//
    val contextContext = LocalContext.current

    if(size.width > 600){
        Row(
            modifier = modifier.fillMaxSize()
        ){
            ShowItemList(
                modifier
                    .fillMaxSize()
                    .weight(1f),contextContext, selectingItem = {currProduct.value = it})
            ShowDetails(currProduct, modifier.weight(1f),scope, snackbarHostState,size)
        }

    }else{
        if(currProduct.value == null){
            ShowItemList(modifier.fillMaxSize(),contextContext, selectingItem = {currProduct.value = it})
        }else{
            ShowDetails(currProduct,modifier.fillMaxSize(),scope, snackbarHostState,size)
        }
    }




}
@Composable
fun ShowItemList(modifier : Modifier = Modifier, contextContext : Context, selectingItem : (StoreItem) -> Unit){
    val itemInStore = parse(contextContext)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(255, 255, 255)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Shopping List",
            color = Color(100,10,200),
            fontSize = 40.sp
        )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(color = Color(255, 255, 255))
        ) {
            items(itemInStore){ item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { selectingItem(item) }
                        .background(color = Color(255, 255, 255)),
                    horizontalAlignment = Alignment.CenterHorizontally
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
fun ShowDetails(item : MutableState<StoreItem?>, modifier: Modifier = Modifier,scope : CoroutineScope, snackbarHostState: SnackbarHostState, sizeScreen: SizeScreen){
    Box(
        modifier = modifier
    ){
        item.value?.let {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Name : ${it.name}"
                )
                Text(
                    text = "Description : ${it.info}"
                )
                Text(
                    text = " Price : ${it.price}"
                )
                Text(
                    text = "Quantity : ${it.quantity}"
                )
                Button(onClick =
                { scope.launch {
                    snackbarHostState.showSnackbar("ADD TO WIShLIST")
                } }

                ){
                    Text(
                        text = "ADD TO LIST"
                    )
                }
                if(sizeScreen.width <= 600){
                    Button(onClick = {item.value = null}){
                        Text(
                            text = "GO BACK"
                        )
                    }
                }
            }
        } ?: run{

            Text(text = "No items Selected")

        }
    }

}



@Composable
fun screenSize() : SizeScreen{
    val configContext = LocalConfiguration.current
    val width =  remember{configContext.screenWidthDp}
    val height =  remember{configContext.screenHeightDp}
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