package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.AppViewModel
import com.example.ui.theme.*

@Composable
fun CartScreen(
    viewModel: AppViewModel,
    onCheckoutClick: () -> Unit,
    onContinueShopping: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val cartTotal = cart.sumOf { it.product.price * it.quantity }

    Column(modifier = Modifier.fillMaxSize().background(Gray100)) {
        Box(modifier = Modifier.fillMaxWidth().background(White).padding(16.dp)) {
            Text("My Cart", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
        }

        if (cart.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(96.dp).background(Pink50, shape = RoundedCornerShape(percent = 50)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.ShoppingCart, null, tint = Pink300, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your cart is empty", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Looks like you haven't added anything to your cart yet.", color = Gray500, fontSize = 14.sp)
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
            LazyColumn(
                modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cart) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(8.dp)).border(1.dp, Gray100, RoundedCornerShape(8.dp)).padding(12.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.product.image),
                            contentDescription = null,
                            modifier = Modifier.width(80.dp).height(96.dp).clip(RoundedCornerShape(4.dp)).background(Gray100),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.product.title, fontSize = 14.sp, color = Gray800, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Size: Free Size", fontSize = 12.sp, color = Gray500)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹${item.product.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Gray900)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.border(1.dp, Gray300, RoundedCornerShape(4.dp))
                                ) {
                                    Icon(Icons.Default.Remove, null, tint = Gray600, modifier = Modifier.size(24.dp).clickable { viewModel.updateQuantity(item.product.id, item.quantity - 1) }.padding(4.dp))
                                    Text(item.quantity.toString(), fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp))
                                    Icon(Icons.Default.Add, null, tint = Gray600, modifier = Modifier.size(24.dp).clickable { viewModel.updateQuantity(item.product.id, item.quantity + 1) }.padding(4.dp))
                                }
                                Icon(Icons.Default.Delete, null, tint = Gray400, modifier = Modifier.clickable { viewModel.removeFromCart(item.product.id) })
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(8.dp)).border(1.dp, Gray100, RoundedCornerShape(8.dp)).padding(16.dp)) {
                        Text("Price Details (${cart.sumOf { it.quantity }} Items)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                        Divider(color = Gray100, modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Product Price", fontSize = 14.sp, color = Gray600)
                            Text("₹$cartTotal", fontSize = 14.sp, color = Gray600)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Discounts", fontSize = 14.sp, color = Green600)
                            Text("-₹0", fontSize = 14.sp, color = Green600)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Additional Fees", fontSize = 14.sp, color = Gray600)
                            Text("+₹0", fontSize = 14.sp, color = Gray600)
                        }
                        Divider(color = Gray100, modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                            Text("₹$cartTotal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(White).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("₹$cartTotal", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Gray900)
                    Text("View Details", fontSize = 12.sp, color = Pink600, fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = onCheckoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                ) {
                    Text("Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
