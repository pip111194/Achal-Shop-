package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.AppViewModel
import com.example.ui.theme.*

@Composable
fun WishlistScreen(
    viewModel: AppViewModel,
    onProductClick: (Int) -> Unit,
    onContinueShopping: () -> Unit,
    onBack: () -> Unit
) {
    val wishlist by viewModel.wishlist.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Gray100)) {
        SimpleHeader(title = "My Wishlist (${wishlist.size})", onBack = onBack)

        if (wishlist.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(96.dp).background(Pink50, shape = RoundedCornerShape(percent = 50)).border(1.dp, Pink100, shape = RoundedCornerShape(percent = 50)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.FavoriteBorder, null, tint = Pink400, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your wishlist is empty", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Save items that you like in your wishlist to buy them later.", color = Gray500, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = onContinueShopping,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Pink600),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Pink600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Continue Shopping", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wishlist) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onProductClick(product.id) },
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Gray200),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.75f).background(Gray100)) {
                                Image(
                                    painter = rememberAsyncImagePainter(product.image),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(28.dp).background(White.copy(alpha = 0.9f), CircleShape).clickable { viewModel.toggleWishlist(product) }, contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = Red500, modifier = Modifier.size(16.dp))
                                }
                            }
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(product.title, fontSize = 14.sp, color = Gray600, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹${product.price}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { 
                                        viewModel.addToCart(product)
                                        viewModel.toggleWishlist(product)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Pink50, contentColor = Pink700),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Transparent),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Move to Cart", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
