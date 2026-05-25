package com.example.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    val cart: StateFlow<List<CartItem>> = repository.cart.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val wishlist: StateFlow<List<Product>> = repository.wishlist.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val orders: StateFlow<List<Order>> = repository.orders.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val addresses: StateFlow<List<Address>> = repository.addresses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val notifications: StateFlow<List<Notification>> = repository.notifications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        fetchProducts()
        viewModelScope.launch {
            repository.seedInitialData()
        }
    }

    private fun fetchProducts() {
        _products.value = com.example.data.MockData.products
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                val currentCart = cart.value
                val existing = currentCart.find { it.product.id == product.id }
                if (existing != null) {
                    repository.insertCartItem(CartItem(product, existing.quantity + 1))
                } else {
                    repository.insertCartItem(CartItem(product, 1))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            try {
                if (quantity < 1) return@launch
                val currentCart = cart.value
                val item = currentCart.find { it.product.id == productId }
                if (item != null) {
                    repository.insertCartItem(CartItem(item.product, quantity))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            try {
                repository.removeCartItem(productId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val isWished = wishlist.value.any { it.id == product.id }
            if (isWished) {
                repository.removeWishlist(product.id)
            } else {
                repository.addWishlist(product)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun placeOrder(paymentMethod: String, addressId: Int, finalTotal: Int) {
        viewModelScope.launch {
            try {
                val currentCart = cart.value
                if (currentCart.isEmpty()) return@launch
                
                val newOrderId = "ORD${(10000000..99999999).random()}"
                val newOrder = Order(
                    id = newOrderId,
                    title = if (currentCart.size == 1) currentCart.first().product.title else "${currentCart.size} items",
                    price = finalTotal,
                    image = currentCart.first().product.image,
                    status = "processing",
                    statusText = "Order Placed",
                    trackingSteps = listOf(
                        TrackingStep("Order Confirmed", "Just Now", true),
                        TrackingStep("Shipped", "Pending", false),
                        TrackingStep("Out for Delivery", "Pending", false),
                        TrackingStep("Delivered", "Pending", false)
                    )
                )
                
                repository.placeOrder(newOrder)
                repository.addNotification(Notification(System.currentTimeMillis().toInt(), "Order Confirmed", "Your order #$newOrderId has been placed successfully.", "success", "Just Now", true))
                repository.clearCart()
                
                simulateRealTimeTracking(newOrder)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun simulateRealTimeTracking(order: Order) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(5000)
            val step1 = order.copy(
                statusText = "Shipped",
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "Done", true),
                    TrackingStep("Shipped", "Just Now", true),
                    TrackingStep("Out for Delivery", "Pending", false),
                    TrackingStep("Delivered", "Pending", false)
                )
            )
            repository.updateOrder(step1)
            repository.addNotification(Notification((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), "Order Shipped", "Your order #${order.id} has been shipped.", "shipping", "Just Now", true))

            kotlinx.coroutines.delay(8000)
            val step2 = step1.copy(
                statusText = "Out for Delivery",
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "Done", true),
                    TrackingStep("Shipped", "Done", true),
                    TrackingStep("Out for Delivery", "Just Now", true),
                    TrackingStep("Delivered", "Pending", false)
                )
            )
            repository.updateOrder(step2)

            kotlinx.coroutines.delay(5000)
            val step3 = step2.copy(
                statusText = "Delivered",
                status = "delivered",
                trackingSteps = listOf(
                    TrackingStep("Order Confirmed", "Done", true),
                    TrackingStep("Shipped", "Done", true),
                    TrackingStep("Out for Delivery", "Done", true),
                    TrackingStep("Delivered", "Just Now", true)
                )
            )
            repository.updateOrder(step3)
            repository.addNotification(Notification((System.currentTimeMillis() % Int.MAX_VALUE).toInt() + 1, "Order Delivered", "Your order #${order.id} has been delivered successfully.", "success", "Just Now", true))
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            repository.addAddress(address)
        }
    }
    
    fun removeAddress(id: Int) {
        viewModelScope.launch {
            repository.removeAddress(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun removeNotification(id: Int) {
        viewModelScope.launch {
            repository.removeNotification(id)
        }
    }

    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            // Add user message
            repository.addChatMessage(ChatMessage(text = text, isUser = true))
            
            // Emulate support response
            kotlinx.coroutines.delay(1000)
            repository.addChatMessage(
                ChatMessage(
                    text = "Thank you for reaching out. A support agent will be with you shortly to help with: \"$text\"", 
                    isUser = false
                )
            )
        }
    }

    fun clearChatMessages() {
        viewModelScope.launch {
            repository.clearChatMessages()
        }
    }
}
