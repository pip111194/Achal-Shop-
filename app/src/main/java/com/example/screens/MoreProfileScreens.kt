package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.data.AppViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onChatClick: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Help Center", onBack = onBack)
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Column(modifier = Modifier.fillMaxWidth().background(Pink600, RoundedCornerShape(12.dp)).padding(20.dp)) {
                    Text("How can we help you?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
                    Text("Search our knowledge base or reach out to our team.", fontSize = 14.sp, color = Pink100, modifier = Modifier.padding(bottom = 16.dp, top = 4.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search for answers...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Outlined.Search, null, tint = Gray400) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = White,
                            unfocusedContainerColor = White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }
            item {
                val context = androidx.compose.ui.platform.LocalContext.current
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f).background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray200, RoundedCornerShape(12.dp)).clickable { 
                        onChatClick()
                    }.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.AutoMirrored.Outlined.Chat, null, tint = Blue500, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Chat with us", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
                    }
                    Column(modifier = Modifier.weight(1f).background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray200, RoundedCornerShape(12.dp)).clickable { 
                        android.widget.Toast.makeText(context, "Calling support...", android.widget.Toast.LENGTH_SHORT).show()
                    }.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Phone, null, tint = Green500, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Call Support", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
                    }
                }
            }
            item {
                Text("Frequently Asked Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
                Column(modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray200, RoundedCornerShape(12.dp))) {
                    val faqs = listOf(
                        "How to track my order?" to "You can track your order by going to the Orders section and selecting 'Track Order'.",
                         "Return & Exchange Policy" to "We allow returns within 7 days of delivery. Make sure the item is in its original condition.", 
                         "Refund Status" to "Refunds take 3-5 business days to reflect in your account after processing.", 
                         "Payment Issues" to "If your payment failed but the amount was deducted, it will be refunded within 48 hours."
                    )
                    var expandedFaqIndex by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(-1) }
                    faqs.forEachIndexed { index, faq ->
                        Column(modifier = Modifier.fillMaxWidth().clickable { 
                            expandedFaqIndex = if (expandedFaqIndex == index) -1 else index
                        }) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(faq.first, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray700)
                                Icon(
                                    if (expandedFaqIndex == index) Icons.Default.ChevronLeft else Icons.Default.ChevronRight, 
                                    null, 
                                    tint = Gray400, 
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (expandedFaqIndex == index) {
                                Text(
                                    faq.second,
                                    fontSize = 12.sp,
                                    color = Gray600,
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                )
                            }
                            if (index < faqs.lastIndex) HorizontalDivider(color = Gray100)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val notifications by viewModel.notifications.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ChevronLeft, null, tint = Gray800, modifier = Modifier.size(28.dp).clickable { onBack() })
                Spacer(modifier = Modifier.width(16.dp))
                Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
            }
            Text("Mark all read", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Pink600, modifier = Modifier.clickable { viewModel.markAllNotificationsRead() })
        }
        
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(notifications.size) { index ->
                val notif = notifications[index]
                val icon = if (notif.type == "shipping") Icons.Outlined.LocalShipping else if (notif.type == "offer") Icons.Outlined.LocalOffer else Icons.Outlined.CheckCircle
                val color = if (notif.type == "shipping") Blue600 else if (notif.type == "offer") Pink600 else Green600
                val isUnread = notif.isUnread
                Row(
                    modifier = Modifier.fillMaxWidth().background(if (isUnread) White else Gray50, RoundedCornerShape(12.dp)).border(1.dp, if (isUnread) Pink100 else Gray200, RoundedCornerShape(12.dp)).padding(16.dp)
                ) {
                    Box(modifier = Modifier.size(40.dp).background(color.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Text(notif.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isUnread) Gray900 else Gray700)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (isUnread) {
                                    Box(modifier = Modifier.size(8.dp).background(Pink500, CircleShape))
                                }
                                Icon(Icons.Outlined.Delete, null, tint = Gray400, modifier = Modifier.size(16.dp).clickable { viewModel.removeNotification(notif.id) })
                            }
                        }
                        Text(notif.desc, fontSize = 12.sp, color = Gray600, modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                        Text(notif.time, fontSize = 10.sp, color = Gray400, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val messages by viewModel.chatMessages.collectAsState()
    var currentMessage by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    
    // Automatically scroll to bottom when new messages arrive.
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    androidx.compose.runtime.LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ChevronLeft, null, tint = Gray800, modifier = Modifier.size(28.dp).clickable { onBack() })
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Chat Support", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
                    Text("Online \u2022 Typically replies in minutes", fontSize = 12.sp, color = Green600)
                }
            }
            Text("Clear Chat", fontSize = 12.sp, color = Gray500, modifier = Modifier.clickable { viewModel.clearChatMessages() })
        }
        
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("No messages yet. Start chatting below!", fontSize = 12.sp, color = Gray400, modifier = Modifier.padding(top = 32.dp))
                    }
                }
            } else {
                items(messages.size) { index ->
                    val msg = messages[index]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 280.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                        bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                    )
                                )
                                .background(if (msg.isUser) Pink600 else White)
                                .border(1.dp, if (msg.isUser) Color.Transparent else Gray200, RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                        bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                ))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.text,
                                fontSize = 14.sp,
                                color = if (msg.isUser) White else Gray800
                            )
                        }
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                placeholder = { Text("Type a message...", fontSize = 14.sp, color = Gray400) },
                modifier = Modifier.weight(1f).background(Gray50, RoundedCornerShape(24.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Pink600, CircleShape)
                    .clickable { 
                        if (currentMessage.isNotBlank()) {
                            viewModel.sendChatMessage(currentMessage)
                            currentMessage = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("→", color = White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}