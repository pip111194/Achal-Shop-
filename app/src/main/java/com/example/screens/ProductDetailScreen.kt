package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.AppViewModel
import com.example.data.MockData
import com.example.ui.theme.*

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val product = products.find { it.id == productId } ?: return
    val wishlist by viewModel.wishlist.collectAsState()
    val isWished = wishlist.any { it.id == product.id }
    val cart by viewModel.cart.collectAsState()
    
    var currentImgIdx by remember(product.id) { mutableStateOf(0) }
    val images = if (product.images.isNotEmpty()) product.images else listOf(product.image)
    val safeImgIdx = currentImgIdx.coerceIn(0, (images.size - 1).coerceAtLeast(0))
    val discount = (((product.originalPrice - product.price).toFloat() / product.originalPrice) * 100).toInt()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Gray100)) {
        // Sticky Header
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.ChevronLeft, null, tint = Gray800, modifier = Modifier.size(28.dp).clickable { onBack() })
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(
                    imageVector = if (isWished) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = if(isWished) Pink500 else Gray600,
                    modifier = Modifier.size(24.dp).clickable { viewModel.toggleWishlist(product) }
                )
                BadgedIcon(icon = Icons.Outlined.ShoppingCart, badgeCount = cart.sumOf { it.quantity }, onClick = onCartClick)
            }
        }
        
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            // Main Image
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.8f).background(Gray50)) {
                Image(
                    painter = rememberAsyncImagePainter(images[safeImgIdx]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                if (images.size > 1) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        images.indices.forEach { idx ->
                            Box(
                                modifier = Modifier
                                    .width(if (safeImgIdx == idx) 20.dp else 6.dp)
                                    .height(6.dp)
                                    .background(if (safeImgIdx == idx) Pink600 else Gray300, CircleShape)
                            )
                        }
                    }
                }
            }
            
            // Thumbnails
            if (images.size > 1) {
                LazyRow(
                    modifier = Modifier.background(White).fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(images.withIndex().toList()) { (idx, img) ->
                        Box(
                            modifier = Modifier
                                .size(64.dp, 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 2.dp,
                                    color = if (safeImgIdx == idx) Pink500 else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { currentImgIdx = idx }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(img),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Info
            Column(modifier = Modifier.background(White).padding(16.dp).fillMaxWidth()) {
                Text(product.title, fontSize = 18.sp, color = Gray700, lineHeight = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("₹${product.price}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Gray900)
                    Text("₹${product.originalPrice}", fontSize = 14.sp, color = Gray400, textDecoration = TextDecoration.LineThrough)
                    Text("$discount% off", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Green600)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.background(Green600, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(product.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Filled.Star, contentDescription = null, tint = White, modifier = Modifier.size(12.dp))
                    }
                    Text("${product.reviews} Ratings", fontSize = 12.sp, color = Gray500)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.background(Green50, RoundedCornerShape(4.dp)).border(1.dp, Green200, RoundedCornerShape(4.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocalOffer, null, tint = Green600, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(product.delivery, fontSize = 14.sp, color = Green700)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Variants (Size/Color)
            var selectedSize by remember { mutableStateOf("M") }
            var selectedColor by remember { mutableStateOf(Color(0xFF1E1E1E)) }
            val sizes = listOf("S", "M", "L", "XL")
            val colors = listOf(Color(0xFF1E1E1E), Color(0xFFF44336), Color(0xFF2196F3), Color(0xFF4CAF50))

            Column(modifier = Modifier.background(White).padding(16.dp).fillMaxWidth()) {
                Text("Select Size", fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(bottom = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    sizes.forEach { size ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .border(1.dp, if (selectedSize == size) Pink600 else Gray300, RoundedCornerShape(8.dp))
                                .background(if (selectedSize == size) Pink50 else White, RoundedCornerShape(8.dp))
                                .clickable { selectedSize = size },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(size, fontWeight = if (selectedSize == size) FontWeight.Bold else FontWeight.Medium, color = if (selectedSize == size) Pink600 else Gray600)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select Color", fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(bottom = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(2.dp, if (selectedColor == color) Pink500 else Color.Transparent, CircleShape)
                                .padding(2.dp)
                                .background(color, CircleShape)
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Column(modifier = Modifier.background(White).padding(16.dp).fillMaxWidth()) {
                Text("Product Details", fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(bottom = 8.dp))
                val details = listOf("Brand:" to "Tech Pro", "Warranty:" to "1 Year Manufacturer", "Condition:" to "New", "Return Policy:" to "7 Days Replacement")
                details.forEach { (k, v) ->
                    Row(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text(k, fontWeight = FontWeight.Medium, color = Gray600, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Text(v, color = Gray800, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reviews
            var showReviewInput by remember { mutableStateOf(false) }
            Column(modifier = Modifier.background(White).padding(16.dp).fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Customer Reviews", fontWeight = FontWeight.Bold, color = Gray800)
                    Text("Write Review", color = Pink600, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { showReviewInput = !showReviewInput })
                }
                
                if (showReviewInput) {
                    var newReview by remember { mutableStateOf("") }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newReview,
                            onValueChange = { newReview = it },
                            placeholder = { Text("What do you think about this?", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Gray300, focusedBorderColor = Pink600)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newReview.isNotBlank()) {
                                    android.widget.Toast.makeText(context, "Review Submitted!", android.widget.Toast.LENGTH_SHORT).show()
                                    newReview = ""
                                    showReviewInput = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(Icons.Filled.ChevronRight, contentDescription = "Submit", tint = White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val mockReviews = listOf(
                    Triple("Alex Johnson", "Absolutely love this! The quality is amazing and it arrived perfectly on time.", 5),
                    Triple("Sam Smith", "Good product, does exactly what it says. Slightly overpriced though.", 4),
                    Triple("Jordan Lee", "Decent build quality but the packaging was a bit damaged.", 3)
                )

                mockReviews.forEachIndexed { index, review ->
                    Column(modifier = Modifier.padding(bottom = if (index == mockReviews.lastIndex) 0.dp else 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(36.dp).background(Pink100, CircleShape), contentAlignment = Alignment.Center) {
                                Text(review.first.first().toString(), color = Pink600, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(review.first, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(top = 2.dp)) {
                                    (1..5).forEach { star ->
                                        Icon(
                                            Icons.Filled.Star, 
                                            contentDescription = null, 
                                            tint = if (star <= review.third) Color(0xFFFFC107) else Gray300,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Text(review.second, fontSize = 14.sp, color = Gray600, modifier = Modifier.padding(top = 8.dp), lineHeight = 20.sp)
                    }
                    if (index != mockReviews.lastIndex) {
                        HorizontalDivider(color = Gray100, modifier = Modifier.padding(top = 16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // padding for bottom bar
        }
        
        // Bottom Bar
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { 
                    viewModel.addToCart(product)
                    android.widget.Toast.makeText(context, "Added to Cart", android.widget.Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Pink600),
                border = androidx.compose.foundation.BorderStroke(1.dp, Pink600),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add to Cart", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { 
                    viewModel.addToCart(product)
                    android.widget.Toast.makeText(context, "Added to Cart", android.widget.Toast.LENGTH_SHORT).show()
                    onCartClick()
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Buy Now", fontWeight = FontWeight.Bold)
            }
        }
    }
}
