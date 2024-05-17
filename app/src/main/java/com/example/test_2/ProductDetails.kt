package com.example.test_2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.test_2.ui.theme.Test_2Theme
import com.example.test_2.ui.theme.red
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetails : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test_2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color(0xFF1A4D2E),
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "All Product",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                                }
                            },
                            actions = {
                                var expanded by remember { mutableStateOf(false) }
                                val productType = remember { mutableStateOf("") }
                                val context = LocalContext.current

                                IconButton(onClick = { expanded = true }) {
                                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    val categories = arrayOf("Pizza", "Burger", "Fries", "Drink", "Chicken fried")
                                    categories.forEach { item ->
                                        DropdownMenuItem(onClick = {
                                            productType.value = item
                                            expanded = false
                                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                        }) {
                                            Text(text = item)
                                        }
                                    }
                                }
                            }
                        )
                    }

                    var productList = mutableStateListOf<ProductData>()
                    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbProducts: CollectionReference = db.collection("Products")

                    dbProducts.get().addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty){
                            val list = queryDocumentSnapshot.documents
                            for (d in list){
                                val c: ProductData? = d.toObject(ProductData::class.java)
                                c?.productID = d.id
                                Log.e("TAG", "Course id is : " + c!!.productID)
                                productList.add(c)
                            }
                        }else{
                            Toast.makeText(
                               this@ProductDetails,
                                "No data found in Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@ProductDetails,
                            "Fail to get the data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    productDetailsUI(LocalContext.current, productList)

                }
            }
        }
    }
}
private fun deleteDataFromFirebase(productID: String?, context: Context) {

    val db = FirebaseFirestore.getInstance();
    db.collection("Products").document(productID.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Product Deleted successfully..", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, ProductDetails::class.java))
    }.addOnFailureListener {
        Toast.makeText(context, "Fail to delete item..", Toast.LENGTH_SHORT).show()
    }

}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun productDetailsUI(context: Context, productList: SnapshotStateList<ProductData>){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 70.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }

        LazyColumn {
            itemsIndexed(productList) {index, item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF6E9B2),
                    modifier = Modifier
                        .height(210.dp)
                        .padding(8.dp),
                    shadowElevation = 10.dp,
                    onClick = {
                    val i = Intent(context, UpdateProducts::class.java)
                        i.putExtra("productName", item?.productName)
                        i.putExtra("productType", item?.productType)
                        i.putExtra("productPrice", item?.productPrice)
                        i.putExtra("productDescription", item?.productDescription)
                        i.putExtra("productImage", item?.productImage)
                        i.putExtra("productID", item?.productID)

                        context.startActivity(i)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(width = 160.dp, height = 160.dp)
                        ) {
                            productList[index]?.productImage?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                                .padding(horizontal = 15.dp, vertical = 0.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.wrapContentSize(),
                                color = Color(0xFFF8D082)
                            ) {
                                productList[index]?.productType?.let {
                                    Text(
                                        text = it,
                                        fontSize =  10.sp,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                        color = Color.White
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            productList[index]?.productName?.let {
                                Text(
                                    text = it,
                                    fontSize =  22.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            productList[index]?.productPrice?.let {
                                Text(text = "$ "+ it)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "4.0",
                                    fontSize =  14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Color(0xFFFFBF00),
                                    contentDescription = null
                                )
                            }
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedButton(
//                                shape = RoundedCornerShape(8.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    contentColor = Color.Black,
//                                    containerColor = Color.White
//                                ),
//                                border = BorderStroke(0.5.dp, Color(0xC3B91C00)),
//                                onClick = {
//                                    val i = Intent(context, UpdateProducts::class.java)
//                                    i.putExtra("productName", item?.productName)
//                                    i.putExtra("productType", item?.productType)
//                                    i.putExtra("productPrice", item?.productPrice)
//                                    i.putExtra("productDescription", item?.productDescription)
//                                    i.putExtra("productImage", item?.productImage)
//                                    i.putExtra("productID", item?.productID)
//
//                                    context.startActivity(i)
//                                }
//                            ) {
//                                Text(
//                                    text = "Edit",
//                                    fontSize =  12.sp,
//                                    fontWeight = FontWeight.SemiBold,
//                                    style = MaterialTheme.typography.titleLarge
//                                )
//                            }
                        }

                    }
                    Row(

                    ) {
                        Surface(
                            onClick = {
                                      deleteDataFromFirebase(productList[index]?.productID,context)
                            },
                            shape = CircleShape,
                            color = Color(0xFFEA906C)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = Color(0xFFFFFFFF),
                                modifier = Modifier
                                    .size(27.dp)
                                    .padding(2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
