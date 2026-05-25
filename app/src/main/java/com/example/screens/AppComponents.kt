package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.Product
import com.example.ui.theme.*

@Composable
fun AchalShopLogo() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Pink500, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("A", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
        Text("Achal Shop", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Gray900)
    }
}

@Composable
fun AppHeader(
    wishlistCount: Int,
    cartItemCount: Int,
    onWishlistClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AchalShopLogo()
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BadgedIcon(icon = Icons.Outlined.FavoriteBorder, badgeCount = wishlistCount, onClick = onWishlistClick)
            BadgedIcon(icon = Icons.Outlined.ShoppingCart, badgeCount = cartItemCount, onClick = onCartClick)
        }
    }
}

@Composable
fun BadgedIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, badgeCount: Int, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable { onClick() }) {
        Icon(imageVector = icon, contentDescription = null, tint = Gray600, modifier = Modifier.size(24.dp))
        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(16.dp)
                    .background(Pink600, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = badgeCount.toString(), color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    isWished: Boolean,
    onProductClick: (Product) -> Unit,
    onWishlistClick: (Product) -> Unit
) {
    val discount = (((product.originalPrice - product.price).toFloat() / product.originalPrice) * 100).toInt()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product) },
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
                if (product.isPopular) {
                    Box(modifier = Modifier.padding(top = 8.dp).background(Pink600, RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("POPULAR", color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(28.dp).background(White.copy(alpha = 0.9f), CircleShape).clickable { onWishlistClick(product) }, contentAlignment = Alignment.Center) {
                    Icon(imageVector = if (isWished) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null, tint = if(isWished) Pink500 else Gray400, modifier = Modifier.size(16.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.title, fontSize = 14.sp, color = Gray600, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("₹${product.price}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                    Text("₹${product.originalPrice}", fontSize = 12.sp, color = Gray400, textDecoration = TextDecoration.LineThrough)
                    Text("$discount% off", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Green600)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.background(Green100, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(product.rating.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Green700)
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Green700, modifier = Modifier.size(10.dp))
                    }
                    Text("(${product.reviews})", fontSize = 10.sp, color = Gray400)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.background(Gray100, CircleShape).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text(product.delivery, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Gray600)
                }
            }
        }
    }
}

@Composable
fun BottomNav(
    activeTab: String,
    cartItemCount: Int,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        Triple("home", Icons.Outlined.Home, "Home"),
        Triple("categories", Icons.Outlined.Search, "Categories"),
        Triple("cart", Icons.Outlined.ShoppingCart, "Cart"),
        Triple("profile", Icons.Outlined.Person, "Account")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { (id, icon, label) ->
            val isSelected = activeTab == id
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onTabSelected(id) }
            ) {
                Box {
                    Icon(imageVector = icon, contentDescription = label, tint = if (isSelected) Pink600 else Gray500)
                    if (id == "cart" && cartItemCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-4).dp)
                                .size(16.dp)
                                .background(Pink600, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = cartItemCount.toString(), color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = if (isSelected) Pink600 else Gray500)
            }
        }
    }
}
