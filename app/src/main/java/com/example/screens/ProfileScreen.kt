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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.ui.theme.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProfileScreen(
    language: String,
    onEditProfileClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
    
    // We can use a derived state or remember to reload on composition
    var userName by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getString("user_name", "Achal Kumar") ?: "Achal Kumar") }
    var userPhone by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getString("user_phone", "+91 9876543210") ?: "+91 9876543210") }
    var profilePhotoUri by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<android.net.Uri?>(
        prefs.getString("profile_photo", null)?.let { android.net.Uri.parse(it) }
    ) }
    
    androidx.compose.runtime.DisposableEffect(context) {
        val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                "user_name" -> userName = sharedPreferences.getString("user_name", "Achal Kumar") ?: "Achal Kumar"
                "user_phone" -> userPhone = sharedPreferences.getString("user_phone", "+91 9876543210") ?: "+91 9876543210"
                "profile_photo" -> profilePhotoUri = sharedPreferences.getString("profile_photo", null)?.let { android.net.Uri.parse(it) }
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Gray100).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().background(White).padding(16.dp)) {
            Text("Account", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
        }

        // Profile Header
        Row(
            modifier = Modifier.fillMaxWidth().background(White).clickable { onEditProfileClick() }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp).background(Pink50, CircleShape).border(1.dp, Pink200, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (profilePhotoUri != null) {
                    coil.compose.AsyncImage(
                        model = profilePhotoUri,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Outlined.Person, null, tint = Pink600, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(userName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
                Text(userPhone, fontSize = 14.sp, color = Gray500)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Gray400)
        }

        Divider(color = Gray200)

        // Quick Links
        Row(
            modifier = Modifier.fillMaxWidth().background(White).padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickLinkItem(Icons.Outlined.LocalShipping, "My Orders", onOrdersClick)
            QuickLinkItem(Icons.Outlined.FavoriteBorder, "Wishlist", onWishlistClick)
            QuickLinkItem(Icons.Outlined.SupportAgent, "Help Center", onHelpClick)
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        // Menu Items
        Column(modifier = Modifier.fillMaxWidth().background(White)) {
            MenuItem(Icons.Outlined.LocationOn, "Saved Addresses", onClick = onAddressesClick)
            Divider(color = Gray100)
            MenuItem(Icons.Outlined.Notifications, "Notifications", onClick = onNotificationsClick)
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth().background(White)) {
            MenuItem(Icons.Outlined.Language, "Choose Language", subtitle = language, onClick = onLanguageClick)
            Divider(color = Gray100)
            MenuItem(Icons.Outlined.Settings, "Settings", onClick = onSettingsClick)
            Divider(color = Gray100)
            MenuItem(Icons.AutoMirrored.Outlined.Logout, "Log Out", iconColor = Red500, titleColor = Red500, showArrow = false, onClick = onLogoutClick)
        }
    }
}

@Composable
fun QuickLinkItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(8.dp).clip(RoundedCornerShape(8.dp))
    ) {
        Icon(icon, null, tint = Gray700, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Gray700)
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    iconColor: Color = Gray500,
    titleColor: Color = Gray700,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = iconColor, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = titleColor)
            if (subtitle != null) {
                Text(subtitle, fontSize = 11.sp, color = Gray400)
            }
        }
        if (showArrow) {
            Icon(Icons.Default.ChevronRight, null, tint = Gray400, modifier = Modifier.size(20.dp))
        }
    }
}
