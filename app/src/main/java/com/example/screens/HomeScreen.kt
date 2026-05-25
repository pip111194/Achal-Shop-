package com.example.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.AppViewModel
import com.example.data.MockData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onProductClick: (Int) -> Unit,
    onWishlistClick: () -> Unit,
    onCartClick: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val products by viewModel.products.collectAsState()
    val filteredProducts = products.filter { it.title.contains(searchQuery, ignoreCase = true) }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Gray100)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // The Header
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                AppHeader(
                    wishlistCount = wishlist.size,
                    cartItemCount = cart.sumOf { it.quantity },
                    onWishlistClick = onWishlistClick,
                    onCartClick = onCartClick
                )
            }
            
            // Search bar
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search by Product Code or Name", fontSize = 14.sp, color = Gray400) },
                        leadingIcon = { Icon(Icons.Outlined.Search, null, tint = Gray400) },
                        trailingIcon = { 
                            val coroutineScope = rememberCoroutineScope()
                            var isListening by remember { mutableStateOf(false) }
                            val audioPermissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
                            
                            val speechRecognizer = remember {
                                try {
                                    SpeechRecognizer.createSpeechRecognizer(context).apply {
                                        setRecognitionListener(object : RecognitionListener {
                                            override fun onReadyForSpeech(params: Bundle?) {}
                                            override fun onBeginningOfSpeech() {}
                                            override fun onRmsChanged(rmsdB: Float) {}
                                            override fun onBufferReceived(buffer: ByteArray?) {}
                                            override fun onEndOfSpeech() {
                                                isListening = false
                                            }
                                            override fun onError(error: Int) {
                                                isListening = false
                                            }
                                            override fun onResults(results: Bundle?) {
                                                isListening = false
                                                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                                if (!matches.isNullOrEmpty()) {
                                                    viewModel.setSearchQuery(matches[0])
                                                }
                                            }
                                            override fun onPartialResults(partialResults: Bundle?) {
                                                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                                if (!matches.isNullOrEmpty()) {
                                                    viewModel.setSearchQuery(matches[0])
                                                }
                                            }
                                            override fun onEvent(eventType: Int, params: Bundle?) {}
                                        })
                                    }
                                } catch(e: Exception) {
                                    null
                                }
                            }

                            DisposableEffect(Unit) {
                                onDispose {
                                    speechRecognizer?.destroy()
                                }
                            }

                            Icon(
                                Icons.Outlined.Mic, 
                                contentDescription = "Voice Search", 
                                tint = if (isListening) Color.Red else Pink600,
                                modifier = Modifier.clickable {
                                    if (!audioPermissionState.status.isGranted) {
                                        audioPermissionState.launchPermissionRequest()
                                        return@clickable
                                    }

                                    if (isListening) {
                                        speechRecognizer?.stopListening()
                                        isListening = false
                                        return@clickable
                                    }
                                    
                                    if (speechRecognizer != null) {
                                        isListening = true
                                        viewModel.setSearchQuery("")
                                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                                        }
                                        try {
                                            speechRecognizer.startListening(intent)
                                        } catch (e: Exception) {
                                            isListening = false
                                        }
                                    } else {
                                        android.widget.Toast.makeText(context, "Voice search not available on this device.", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) 
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Gray50,
                            unfocusedContainerColor = Gray50,
                            focusedBorderColor = Gray300,
                            unfocusedBorderColor = Gray300
                        )
                    )
                }
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                // Banners
                val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { MockData.banners.size })
                
                LaunchedEffect(Unit) {
                    while (true) {
                        kotlinx.coroutines.delay(3000)
                        val nextPage = (pagerState.currentPage + 1) % MockData.banners.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }

                Column(modifier = Modifier.fillMaxWidth().background(White).padding(vertical = 12.dp)) {
                    androidx.compose.foundation.pager.HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        pageSpacing = 16.dp
                    ) { page ->
                        val banner = MockData.banners[page]
                        Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))) {
                            Image(
                                painter = rememberAsyncImagePainter(banner.image),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
                            Column(modifier = Modifier.align(Alignment.CenterStart).padding(24.dp)) {
                                Text(banner.title, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(banner.subtitle, color = White.copy(alpha = 0.9f), fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { onNavigateToCategories() }, colors = ButtonDefaults.buttonColors(containerColor = Pink600)) {
                                    Text("Shop Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Pager Indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(MockData.banners.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Pink600 else Gray300
                            val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .height(8.dp)
                                    .width(width)
                                    .animateContentSize()
                            )
                        }
                    }
                }
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                // Categories Map
                LazyRow(
                    modifier = Modifier.fillMaxWidth().background(White).padding(vertical = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(MockData.categories) { cat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, 
                            modifier = Modifier.clickable { 
                                viewModel.setSearchQuery(cat.name)
                                onNavigateToCategories()
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .shadow(4.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(White)
                                    .border(2.dp, Pink50, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(cat.image),
                                    contentDescription = cat.name,
                                    modifier = Modifier.size(68.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(cat.name, fontSize = 12.sp, color = Gray800, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                // Products
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Products For You",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Gray800
                    )
                    Text(
                        "View All >",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Pink600,
                        modifier = Modifier.clickable { onNavigateToCategories() }
                    )
                }
            }

            if (filteredProducts.isEmpty()) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No products found for \"$searchQuery\"", color = Gray500)
                    }
                }
            } else {
                items(filteredProducts) { product ->
                    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                        ProductCard(
                            product = product,
                            isWished = wishlist.any { it.id == product.id },
                            onProductClick = { onProductClick(product.id) },
                            onWishlistClick = { viewModel.toggleWishlist(product) }
                        )
                    }
                }
            }
        }
    }
}
