package com.example.test_2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.test_2.ui.theme.Test_2Theme
import com.example.test_2.ui.theme.grayFont
import com.example.test_2.ui.theme.pinkitemshadow
import com.example.test_2.ui.theme.yellow1
import com.example.test_2.ui.theme.yellow2
import com.example.test_2.ui.theme.yellow3
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                                    text = "Add Product Item",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
                            actions = {
                                IconButton(onClick = {/* Do Something*/ }) {
                                    Icon(imageVector = Icons.Filled.Settings, null)
                                }
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 70.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){

                    addProductUI(LocalContext.current)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addProductUI(context: Context){
    val categories = arrayOf("Pizza", "Burger", "Fries", "Drink", "Chicken fried")
    var expanded by remember { mutableStateOf(false) }

    val productName = remember {
        mutableStateOf("")
    }
    val productType = remember {
        mutableStateOf("")
    }
    val productPrice = remember {
        mutableStateOf("")
    }
    val productDescription = remember {
        mutableStateOf("")
    }
    val productImage = remember {
        mutableStateOf("")
    }

    TextButton(onClick = {
        context.startActivity(Intent(context, ProductDetails::class.java))
    },
    ) {
        Text(
            text = "View All ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A5D1A),
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "",
            tint = Color(0xFF1A5D1A)
        )
    }
//    Spacer(modifier = Modifier.size(10.dp))
    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        SelectItemImageSection{
                imageUrl -> productImage.value = imageUrl // Assign the URL to the mutableState variable

        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productName.value,
            onValueChange = { productName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = yellow2,
                unfocusedBorderColor = pinkitemshadow
            ),
            label = { Text(text = "Name", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                OutlinedTextField(
                    value = productType.value.toString(),
                    onValueChange = {productType.value = it},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                        .fillMaxWidth()
                        .height(59.dp),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = yellow2,
                        unfocusedBorderColor = pinkitemshadow
                    ),
                    label = { Text(text = "Choose type", color = grayFont)}
                )

                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item) },
                            onClick = {
                                productType.value = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productPrice.value,
            onValueChange = { productPrice.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = yellow2,
                unfocusedBorderColor = pinkitemshadow
            ),
            label = { Text(text = "Price", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productDescription.value,
            onValueChange = { productDescription.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = yellow2,
                unfocusedBorderColor = pinkitemshadow
            ),
            label = { Text(text = "Description", color = grayFont)}
        )

        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                addProductData(productName.value, productType.value, productPrice.value, productDescription.value, productImage.value, context)
                context.startActivity(Intent(context, ProductDetails::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFF337357)
            ),
//                border = BorderStroke(0.5.dp, Color.Red)
        ) {
            Text(text = "ADD", fontWeight = FontWeight.Bold,fontSize = 18.sp)
        }

    }
}


fun addProductData(productName: String,
                   productType: String,
                   productPrice: String,
                   productDescription: String,
                   productImage: String,
                   context: Context){

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbProducts: CollectionReference = db.collection("Products")
    val productId = UUID.randomUUID().toString()
    val product = ProductData(productId, productName, productType, productPrice, productDescription, productImage)

    dbProducts.add(product).addOnSuccessListener {
        Toast.makeText(
            context,
            "Added Sucsessful",
            Toast.LENGTH_SHORT
        ).show()
    }.addOnFailureListener {e ->
        Toast.makeText(context, "Added failed \n$e", Toast.LENGTH_SHORT).show()
    }
}

fun uploadToStorage(uri: Uri, context: Context, type: String, onImageUploaded: (String) -> Unit) {
    val storage = Firebase.storage
    // Create a storage reference from our app
    val storageRef = storage.reference

    val uniqueImageName = UUID.randomUUID().toString()
    val spaceRef: StorageReference = storageRef.child("$uniqueImageName.jpg")

    val byteArray: ByteArray? = context.contentResolver
        .openInputStream(uri)
        ?.use { it.readBytes() }

    byteArray?.let {
        val uploadTask = spaceRef.putBytes(byteArray)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                context,
                "Upload failed",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { _ ->
            spaceRef.downloadUrl.addOnSuccessListener { uri ->
                onImageUploaded(uri.toString()) // Pass the URL to the callback function
            }.addOnFailureListener {
                // Handle failures
                Toast.makeText(
                    context,
                    "Failed to retrieve download URL",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun SelectItemImageSection(onImageSelected: (String) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val getImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            // Call uploadToStorage function with appropriate type
            uploadToStorage(uri, context, "image") { imageUrl ->
                onImageSelected(imageUrl) // Pass the URL to the callback function
            }
        }
    }
    Surface(
            modifier = Modifier
                .size(150.dp),
            shape = CircleShape,
            shadowElevation = 3.dp,
        border = BorderStroke(0.5.dp, color = yellow3)
        ) {

            IconButton(onClick = {
                getImage.launch("image/*")
            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "",
                    tint = yellow1)
            }
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected image",
//                    modifier = Modifier
//                        .size(150.dp),
                    contentScale = ContentScale.Crop,
                )
        }

// Hiển thị trước ảnh đã chọn

    }
}

@Preview
@Composable
fun previewUI(){
    addProductUI(LocalContext.current)
}