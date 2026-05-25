package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.AppViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CheckoutScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onPlaceOrder: (String, Int, Int) -> Unit,
    onEditCart: () -> Unit,
    onAddNewAddress: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val cartTotal = cart.sumOf { it.product.price * it.quantity }
    var checkoutStep by remember { mutableStateOf("shipping") }
    var selectedAddressId by remember { mutableStateOf(1) }
    var deliveryOption by remember { mutableStateOf("standard") }
    var paymentMethod by remember { mutableStateOf("cod") }
    var showStripeDialog by remember { mutableStateOf(false) }
    val addresses by viewModel.addresses.collectAsState()
    
    val deliveryFee = if (deliveryOption == "express") 99 else 0
    val finalTotal = cartTotal + deliveryFee

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Checkout", onBack = { if (checkoutStep == "payment") checkoutStep = "shipping" else onBack() })
        
        // Progress Stepper
        Row(modifier = Modifier.fillMaxWidth().background(White).padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(24.dp).background(Pink600, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Check, null, tint = White, modifier = Modifier.size(14.dp)) }
            HorizontalDivider(modifier = Modifier.weight(1f), color = Pink600, thickness = 2.dp)
            Box(modifier = Modifier.size(24.dp).background(if (checkoutStep == "payment") Pink600 else White, CircleShape).border(if (checkoutStep == "payment") 0.dp else 4.dp, Pink100, CircleShape), contentAlignment = Alignment.Center) { 
                if (checkoutStep == "payment") Icon(Icons.Default.Check, null, tint = White, modifier = Modifier.size(14.dp)) else Text("2", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Pink600)
            }
            HorizontalDivider(modifier = Modifier.weight(1f), color = if (checkoutStep == "payment") Pink600 else Gray200, thickness = 2.dp)
            Box(modifier = Modifier.size(24.dp).background(if (checkoutStep == "payment") White else Gray200, CircleShape).border(if (checkoutStep == "payment") 4.dp else 0.dp, Pink100, CircleShape), contentAlignment = Alignment.Center) {
                Text("3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (checkoutStep == "payment") Gray900 else Gray500)
            }
        }
        
        if (checkoutStep == "shipping") {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                
                // Addresses
                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Shipping Address", fontWeight = FontWeight.Bold, color = Gray800)
                        Text("+ Add New", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Pink600, modifier = Modifier.clickable {
                            onAddNewAddress()
                        })
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    addresses.forEach { address ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { selectedAddressId = address.id }.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                            RadioButton(selected = selectedAddressId == address.id, onClick = { selectedAddressId = address.id }, colors = RadioButtonDefaults.colors(selectedColor = Pink600))
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(address.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                                    Box(modifier = Modifier.padding(start = 8.dp).background(Gray200, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                        Text(address.type, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gray700)
                                    }
                                }
                                Text("${address.flat}, ${address.area}, ${address.city}, ${address.state} - ${address.pincode}", fontSize = 12.sp, color = Gray600, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
                
                // Delivery
                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                        Icon(Icons.Outlined.LocalShipping, null, tint = Blue500, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Shipping Method", fontWeight = FontWeight.Bold, color = Gray800)
                    }
                    
                    Row(modifier = Modifier.fillMaxWidth().clickable { deliveryOption = "standard" }.border(1.dp, if(deliveryOption == "standard") Pink500 else Gray200, RoundedCornerShape(8.dp)).background(if(deliveryOption == "standard") Pink50 else White).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = deliveryOption == "standard", onClick = { deliveryOption = "standard" }, colors = RadioButtonDefaults.colors(selectedColor = Pink600))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Standard Delivery", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                                Text("Est. Delivery: Expected in 4 days", fontSize = 12.sp, color = Gray500)
                            }
                        }
                        Text("FREE", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Green600)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth().clickable { deliveryOption = "express" }.border(1.dp, if(deliveryOption == "express") Pink500 else Gray200, RoundedCornerShape(8.dp)).background(if(deliveryOption == "express") Pink50 else White).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = deliveryOption == "express", onClick = { deliveryOption = "express" }, colors = RadioButtonDefaults.colors(selectedColor = Pink600))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Express Delivery", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                                    Box(modifier = Modifier.padding(start = 4.dp).background(Orange100, RoundedCornerShape(4.dp)).padding(horizontal = 4.dp)) {
                                        Text("FAST", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Orange600)
                                    }
                                }
                                Text("Est. Delivery: Expected Tomorrow", fontSize = 12.sp, color = Gray500)
                            }
                        }
                        Text("+₹99", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray900)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            Box(modifier = Modifier.fillMaxWidth().background(White).padding(16.dp)) {
                Button(
                    onClick = { checkoutStep = "payment" },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Continue to Payment", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.Default.ChevronRight, null, modifier = Modifier.padding(start = 8.dp))
                }
            }
        } else {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                
                // Payment Methods
                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Text("Payment Options", fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(bottom = 12.dp))
                    listOf(
                        Triple("stripe", "Credit Card (Stripe)", Icons.Outlined.CreditCard),
                        Triple("cod", "Cash on Delivery", Icons.Outlined.Wallet)
                    ).forEach { (id, label, icon) ->
                        Row(modifier = Modifier.fillMaxWidth().clickable { paymentMethod = id.toString() }.border(1.dp, if(paymentMethod == id.toString()) Pink500 else Gray200, RoundedCornerShape(8.dp)).background(if(paymentMethod == id.toString()) Pink50 else White).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(32.dp).background(White, CircleShape).border(1.dp, Gray100, CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(icon as androidx.compose.ui.graphics.vector.ImageVector, null, tint = Pink600, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(label.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                            }
                            RadioButton(selected = paymentMethod == id.toString(), onClick = { paymentMethod = id.toString() }, colors = RadioButtonDefaults.colors(selectedColor = Pink600))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                
                // Price Details
                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Text("Price Details", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                    HorizontalDivider(color = Gray100, modifier = Modifier.padding(vertical = 12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", fontSize = 14.sp, color = Gray600)
                        Text("₹$cartTotal", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray800)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Shipping Fee", fontSize = 14.sp, color = Gray600)
                        Text(if (deliveryFee == 0) "Free" else "+₹$deliveryFee", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if(deliveryFee == 0) Green600 else Gray800)
                    }
                    HorizontalDivider(color = Gray100, modifier = Modifier.padding(vertical = 12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                        Text("₹$finalTotal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().background(White).padding(16.dp)) {
                Button(
                    onClick = { 
                        if (paymentMethod == "stripe") {
                            showStripeDialog = true
                        } else {
                            onPlaceOrder(paymentMethod, selectedAddressId, finalTotal)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Pay ₹$finalTotal", modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Place Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.ChevronRight, null, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
        }
    }

    if (showStripeDialog) {
        StripeSimulationDialog(
            amount = finalTotal,
            onSuccess = {
                showStripeDialog = false
                onPlaceOrder(paymentMethod, selectedAddressId, finalTotal)
            },
            onCancel = { showStripeDialog = false }
        )
    }
}

@Composable
fun StripeSimulationDialog(amount: Int, onSuccess: () -> Unit, onCancel: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    androidx.compose.ui.window.Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(16.dp)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CreditCard, null, tint = Color(0xFF6772E5), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stripe", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color(0xFF6772E5))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Amount to pay: ₹$amount", fontSize = 16.sp, color = Gray700)
            Spacer(modifier = Modifier.height(24.dp))
            
            androidx.compose.material3.OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                androidx.compose.material3.OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    label = { Text("MM/YY") },
                    modifier = Modifier.weight(1f)
                )
                androidx.compose.material3.OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    isLoading = true
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(2000)
                        isLoading = false
                        onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6772E5)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Pay Securely", fontWeight = FontWeight.Bold, color = White)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            androidx.compose.material3.TextButton(onClick = onCancel) {
                Text("Cancel", color = Gray600)
            }
        }
    }
}
