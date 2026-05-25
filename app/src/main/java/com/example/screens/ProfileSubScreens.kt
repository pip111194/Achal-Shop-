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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Edit
import com.example.ui.theme.*

@Composable
fun SimpleHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(White).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.ChevronLeft, null, tint = Gray800, modifier = Modifier.size(28.dp).clickable { onBack() })
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray800)
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)

    var pushNotifications by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getBoolean("settings_push", true)) }
    var emailUpdates by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getBoolean("settings_email", true)) }
    var locationServices by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getBoolean("settings_location", false)) }
    
    var showChangePassword by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showDeleteAccount by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showChangePassword) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showChangePassword = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("Current Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("New Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(onClick = {
                    showChangePassword = false
                    android.widget.Toast.makeText(context, "Password updated successfully", android.widget.Toast.LENGTH_SHORT).show()
                }, colors = ButtonDefaults.buttonColors(containerColor = Pink600)) {
                    Text("Update", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePassword = false }) {
                    Text("Cancel", color = Gray600)
                }
            }
        )
    }

    if (showDeleteAccount) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteAccount = false },
            title = { Text("Delete Account", fontWeight = FontWeight.Bold, color = Red500) },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently removed.") },
            confirmButton = {
                Button(onClick = {
                    showDeleteAccount = false
                    android.widget.Toast.makeText(context, "Account deleted", android.widget.Toast.LENGTH_SHORT).show()
                    // Typically you would log out here and navigate to Auth
                }, colors = ButtonDefaults.buttonColors(containerColor = Red500)) {
                    Text("Delete", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccount = false }) {
                    Text("Cancel", color = Gray600)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Settings", onBack = onBack)
        Column(modifier = Modifier.padding(16.dp)) {
            Text("NOTIFICATIONS & PERMISSIONS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray400, modifier = Modifier.padding(bottom = 12.dp, start = 4.dp))
            Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp))) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Push Notifications", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800)
                    Switch(
                        checked = pushNotifications,
                        onCheckedChange = { 
                            pushNotifications = it
                            prefs.edit().putBoolean("settings_push", it).apply()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Pink600)
                    )
                }
                HorizontalDivider(color = Gray50)
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Email Promos", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800)
                    Switch(
                        checked = emailUpdates,
                        onCheckedChange = { 
                            emailUpdates = it
                            prefs.edit().putBoolean("settings_email", it).apply()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Pink600)
                    )
                }
                HorizontalDivider(color = Gray50)
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Location Services", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800)
                    Switch(
                        checked = locationServices,
                        onCheckedChange = { 
                            locationServices = it
                            prefs.edit().putBoolean("settings_location", it).apply()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Pink600)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("PRIVACY & SECURITY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray400, modifier = Modifier.padding(bottom = 12.dp, start = 4.dp))
            Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp))) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { showChangePassword = true }.padding(16.dp), 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Change Password", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800)
                }
                HorizontalDivider(color = Gray50)
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { showDeleteAccount = true }.padding(16.dp), 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Delete Account", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Red500)
                }
            }
        }
    }
}

@Composable
fun LanguageScreen(currentLanguage: String, onSelect: (String) -> Unit, onBack: () -> Unit) {
    val langs = listOf("English", "Hindi (हिंदी)", "Marathi (मराठी)", "Tamil (தமிழ்)", "Telugu (తెలుగు)", "Bengali (বাংলা)", "Gujarati (ગુજરાતી)")
    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Choose Language", onBack = onBack)
        Column(modifier = Modifier.padding(16.dp).background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp))) {
            langs.forEachIndexed { index, lang ->
                val isSelected = lang == currentLanguage
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSelect(lang) }.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(lang, fontSize = 14.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Medium, color = if(isSelected) Pink600 else Gray700)
                    if (isSelected) {
                        Icon(Icons.Default.Check, null, tint = Pink600, modifier = Modifier.size(18.dp))
                    }
                }
                if (index < langs.lastIndex) Divider(color = Gray100)
            }
        }
    }
}

@Composable
fun AddressesScreen(viewModel: com.example.data.AppViewModel, onBack: () -> Unit) {
    val addresses by viewModel.addresses.collectAsState()
    var isAdding by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Saved Addresses", onBack = onBack)
        Column(modifier = Modifier.weight(1f).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            addresses.forEach { address ->
                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray200, RoundedCornerShape(12.dp)).padding(16.dp).fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.background(if (address.type == "HOME") Pink50 else Gray100, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(address.type, color = if (address.type == "HOME") Pink700 else Gray700, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Outlined.Delete, null, tint = Gray400, modifier = Modifier.size(20.dp).clickable { viewModel.removeAddress(address.id) })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(address.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Gray800)
                    Text("${address.flat}, ${address.area}\n${address.city}, ${address.state} - ${address.pincode}", fontSize = 12.sp, color = Gray600, modifier = Modifier.padding(vertical = 4.dp))
                    Text(address.phone, fontSize = 12.sp, color = Gray600, fontWeight = FontWeight.Medium)
                }
            }
            
            if (isAdding) {
                var name by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                var phone by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                var flat by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                var area by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                var city by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
                var pincode by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

                Column(modifier = Modifier.background(White, RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Text("Add New Address", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Gray800)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = flat, onValueChange = { flat = it }, label = { Text("House/Flat No.") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Area/Locality") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { isAdding = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel", color = Gray600)
                        }
                        Button(onClick = { 
                            if (name.isNotBlank() && phone.isNotBlank() && flat.isNotBlank() && area.isNotBlank() && city.isNotBlank() && pincode.isNotBlank()) {
                                viewModel.addAddress(com.example.data.Address(id = 0, name = name, phone = phone, flat = flat, area = area, city = city, pincode = pincode, state = "State", type = "HOME"))
                                isAdding = false
                            }
                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Pink600)) {
                            Text("Save", color = White)
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { isAdding = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Pink50, contentColor = Pink600),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Pink500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+ Add New Address", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
    
    var name by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getString("user_name", "Achal Kumar") ?: "Achal Kumar") }
    var phone by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getString("user_phone", "+91 9876543210") ?: "+91 9876543210") }
    var email by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(prefs.getString("saved_email", "guest@google.com") ?: "") }
    var profilePhotoUri by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<android.net.Uri?>(
        prefs.getString("profile_photo", null)?.let { android.net.Uri.parse(it) }
    ) }
    
    var isSaving by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val photoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                // Keep the permission across restarts
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    android.util.Log.e("EditProfile", "Failed to take persistable URI permission", e)
                }
                profilePhotoUri = uri
            }
        }
    )

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        SimpleHeader(title = "Edit Profile", onBack = onBack)
        
        Column(modifier = Modifier.padding(16.dp).background(White, RoundedCornerShape(12.dp)).border(1.dp, Gray100, RoundedCornerShape(12.dp)).padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Pink50, CircleShape)
                    .border(2.dp, Pink200, CircleShape)
                    .align(Alignment.CenterHorizontally)
                    .clickable { 
                        photoPickerLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        ) 
                    },
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
                    Icon(Icons.Outlined.Person, null, tint = Pink600, modifier = Modifier.size(40.dp))
                }
                
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .background(Pink600, CircleShape)
                        .border(2.dp, White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Change Photo", tint = White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { 
                    isSaving = true
                    prefs.edit()
                        .putString("user_name", name)
                        .putString("user_phone", phone)
                        .putString("saved_email", email)
                        .putString("profile_photo", profilePhotoUri?.toString())
                        .apply()
                        
                    android.widget.Toast.makeText(context, "Profile Updated Successfully", android.widget.Toast.LENGTH_SHORT).show()
                    isSaving = false
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Pink600),
                shape = RoundedCornerShape(8.dp),
                enabled = !isSaving
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
            }
        }
    }
}
