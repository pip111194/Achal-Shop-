package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
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

@Composable
fun CategoriesScreen(
    viewModel: AppViewModel,
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit
) {
    var activeCategoryId by remember { mutableStateOf(MockData.categories.first().id) }
    val cart by viewModel.cart.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        // App Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Categories", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(Icons.Outlined.Search, null, tint = Gray600, modifier = Modifier.clickable { onSearchClick() })
                BadgedIcon(icon = Icons.Outlined.ShoppingCart, badgeCount = cart.sumOf { it.quantity }, onClick = onCartClick)
            }
        }
        HorizontalDivider(color = Gray100)

        Row(modifier = Modifier.weight(1f).fillMaxWidth().background(Gray50)) {
            // Sidebar
            LazyColumn(
                modifier = Modifier.width(85.dp).fillMaxHeight().background(White).padding(end = 1.dp)
            ) {
                items(MockData.categories) { cat ->
                    val isActive = cat.id == activeCategoryId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) Pink50 else Color.Transparent)
                            .clickable { activeCategoryId = cat.id }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isActive) {
                            Box(modifier = Modifier.align(Alignment.CenterStart).width(4.dp).height(40.dp).background(Pink600, RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(if (isActive) Pink100 else Gray100), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = rememberAsyncImagePainter(cat.image),
                                    contentDescription = cat.name,
                                    modifier = Modifier.size(52.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(cat.name, fontSize = 10.sp, color = if (isActive) Pink600 else Gray600, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium, textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            // Subcategories
            val activeCategory = MockData.categories.find { it.id == activeCategoryId } ?: MockData.categories.first()
            val subcategories = MockData.subcategoriesData[activeCategoryId] ?: emptyList()

            Column(modifier = Modifier.weight(1f).background(White).padding(16.dp)) {
                // Banner
                Box(modifier = Modifier.fillMaxWidth().height(96.dp).clip(RoundedCornerShape(12.dp))) {
                    Image(
                        painter = rememberAsyncImagePainter(activeCategory.image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
                        Text(activeCategory.name.uppercase(), color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp, letterSpacing = 2.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Shop by Category", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    modifier = Modifier.weight(1f),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(subcategories) { sub ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { 
                                viewModel.setSearchQuery(activeCategory.name)
                                onSearchClick()
                            }
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(CircleShape).background(Gray50)) {
                                Image(
                                    painter = rememberAsyncImagePainter(sub.image),
                                    contentDescription = sub.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(sub.name, fontSize = 10.sp, color = Gray700, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
