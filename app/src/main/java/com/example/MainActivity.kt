package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.AppViewModel
import com.example.screens.*
import com.example.ui.theme.MyApplicationTheme

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyBmvsp44RGcI5anWpOx1bSdiq8OTk3Wcf4")
                    .setApplicationId("1:458513139418:web:0bf5df2278eb79798c07e3")
                    .setDatabaseUrl("https://achal-mobile-shop-default-rtdb.firebaseio.com")
                    .setProjectId("achal-mobile-shop")
                    .build()
                FirebaseApp.initializeApp(this, options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val context = androidx.compose.ui.platform.LocalContext.current
                val viewModel: AppViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            val db = com.example.data.AppDatabase.getDatabase(context)
                            val repo = com.example.data.AppRepository(db.appDao())
                            return AppViewModel(repo) as T
                        }
                    }
                )
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Determine if bottom nav should be shown
                val showBottomNav = currentRoute in listOf("home", "categories", "cart", "profile")
                val cart by viewModel.cart.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            BottomNav(
                                activeTab = currentRoute ?: "home",
                                cartItemCount = cart.sumOf { it.quantity },
                                onTabSelected = { route ->
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        NavHost(navController = navController, startDestination = "splash") {
                            composable("splash") {
                                SplashScreen(onSplashFinished = {
                                    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                                    val isLoggedIn = prefs.getBoolean("is_logged_in", false)
                                    if (isLoggedIn) {
                                        navController.navigate("home") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("auth") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                })
                            }
                            composable("auth") {
                                AuthScreen(onAuthSuccess = {
                                    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                                    prefs.edit().putBoolean("is_logged_in", true).apply()
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                })
                            }
                            composable("home") {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onProductClick = { productId -> navController.navigate("product_detail/$productId") },
                                    onWishlistClick = { navController.navigate("wishlist") },
                                    onCartClick = { navController.navigate("cart") },
                                    onNavigateToCategories = {
                                        navController.navigate("categories") {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                            composable("categories") {
                                CategoriesScreen(
                                    viewModel = viewModel,
                                    onSearchClick = { navController.navigate("home") {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    } },
                                    onCartClick = { navController.navigate("cart") }
                                )
                            }
                            composable("cart") {
                                CartScreen(
                                    viewModel = viewModel,
                                    onCheckoutClick = { navController.navigate("checkout") },
                                    onContinueShopping = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                            composable("profile") {
                                val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                                var language by remember { mutableStateOf(prefs.getString("selected_language", "English") ?: "English") }
                                
                                androidx.compose.runtime.DisposableEffect(context) {
                                    val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                                        if (key == "selected_language") {
                                            language = sharedPreferences.getString("selected_language", "English") ?: "English"
                                        }
                                    }
                                    prefs.registerOnSharedPreferenceChangeListener(listener)
                                    onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
                                }
                                ProfileScreen(
                                    language = language,
                                    onEditProfileClick = { navController.navigate("edit_profile") },
                                    onOrdersClick = { navController.navigate("orders") },
                                    onWishlistClick = { navController.navigate("wishlist") },
                                    onHelpClick = { navController.navigate("help") },
                                    onAddressesClick = { navController.navigate("addresses") },
                                    onNotificationsClick = { navController.navigate("notifications") },
                                    onLanguageClick = { navController.navigate("language") },
                                    onSettingsClick = { navController.navigate("settings") },
                                    onLogoutClick = {
                                        val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                                        prefs.edit().putBoolean("is_logged_in", false).apply()
                                        navController.navigate("auth") {
                                            popUpTo(navController.graph.id) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("product_detail/{productId}") { backStackEntry ->
                                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                                if (productId != null) {
                                    ProductDetailScreen(
                                        productId = productId,
                                        viewModel = viewModel,
                                        onBack = { navController.popBackStack() },
                                        onCartClick = { navController.navigate("cart") }
                                    )
                                }
                            }
                            composable("checkout") {
                                CheckoutScreen(
                                    viewModel = viewModel,
                                    onBack = { navController.popBackStack() },
                                    onPlaceOrder = { method, addressId, total ->
                                        viewModel.placeOrder(method, addressId, total)
                                        navController.navigate("order_success") {
                                            popUpTo("home")
                                        }
                                    },
                                    onEditCart = { navController.navigate("cart") },
                                    onAddNewAddress = { navController.navigate("addresses") }
                                )
                            }
                            composable("order_success") {
                                OrderSuccessScreen(
                                    onTrackOrder = {
                                        navController.navigate("orders") {
                                            popUpTo("home")
                                        }
                                    },
                                    onContinueShopping = {
                                        navController.navigate("home") {
                                            popUpTo(navController.graph.id) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("orders") {
                                OrdersScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                            }
                            composable("wishlist") {
                                WishlistScreen(
                                    viewModel = viewModel,
                                    onProductClick = { productId -> navController.navigate("product_detail/$productId") },
                                    onContinueShopping = {
                                        navController.navigate("home") {
                                            popUpTo("home")
                                        }
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("help") {
                                HelpScreen(
                                    onChatClick = { navController.navigate("chat_support") },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("chat_support") {
                                ChatScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                            }
                            composable("notifications") {
                                NotificationsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                            }
                            composable("addresses") {
                                AddressesScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                            }
                            composable("settings") {
                                SettingsScreen(onBack = { navController.popBackStack() })
                            }
                            composable("edit_profile") {
                                EditProfileScreen(onBack = { navController.popBackStack() })
                            }
                            composable("language") {
                                val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                                val currentLang = prefs.getString("selected_language", "English") ?: "English"
                                LanguageScreen(
                                    currentLanguage = currentLang,
                                    onSelect = { 
                                        prefs.edit().putString("selected_language", it).apply()
                                        navController.popBackStack() 
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
