package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.MockData
import com.example.ui.theme.*

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.AppViewModel

@Composable
fun OrdersScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val orders by viewModel.orders.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(Gray100)) {
        SimpleHeader(title = "My Orders", onBack = onBack)
        
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(120.dp).background(White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = null, modifier = Modifier.size(64.dp), tint = Gray300)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("No orders yet", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Looks like you haven't placed an order.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Order ID: ${order.id}", fontSize = 12.sp, color = Gray500)
                                Text(order.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(top = 4.dp))
                                Text("₹${order.price}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Pink600, modifier = Modifier.padding(top = 4.dp))
                            }
                            Image(
                                painter = rememberAsyncImagePainter(order.image),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 12.dp).width(64.dp).height(80.dp).clip(RoundedCornerShape(8.dp)).background(Gray100),
                                contentScale = ContentScale.Crop
                            )
                        }
                        HorizontalDivider(color = Gray100, modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            order.statusText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == "delivered") Green600 else Orange600,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            order.trackingSteps.forEachIndexed { index, step ->
                                Row(verticalAlignment = Alignment.Top) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(if (step.completed) Green500 else White, CircleShape)
                                                .border(2.dp, if (step.completed) Green500 else Gray300, CircleShape)
                                        )
                                        if (index < order.trackingSteps.lastIndex) {
                                            Box(modifier = Modifier.width(2.dp).height(32.dp).background(if (step.completed) Green500 else Gray200))
                                        }
                                    }
                                    Column(modifier = Modifier.padding(start = 16.dp).offset(y = (-2).dp)) {
                                        Text(step.label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (step.completed) Gray900 else Gray400)
                                        Text(step.date, fontSize = 12.sp, color = Gray500)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

