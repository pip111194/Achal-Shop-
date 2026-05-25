package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.channels.awaitClose

class AppRepository(private val dao: AppDao) {

    val addresses: Flow<List<Address>> = dao.getAllAddresses().map { entities -> 
        entities.map { Address(it.id, it.name, it.type, it.flat, it.area, it.city, it.state, it.pincode, it.phone) }
    }

    suspend fun addAddress(address: Address) {
        dao.insertAddress(AddressEntity(id = address.id, name = address.name, type = address.type, flat = address.flat, area = address.area, city = address.city, state = address.state, pincode = address.pincode, phone = address.phone))
    }

    suspend fun removeAddress(id: Int) {
        dao.deleteAddress(id)
    }

    val notifications: Flow<List<Notification>> = dao.getAllNotifications().map { entities -> 
        entities.map { Notification(it.id, it.title, it.desc, it.type, it.time, it.isUnread) } 
    }

    suspend fun markAllNotificationsRead() {
        dao.markAllNotificationsRead()
    }
    
    suspend fun removeNotification(id: Int) {
        dao.deleteNotification(id)
    }
    
    suspend fun addNotification(notification: Notification) {
        dao.insertNotification(NotificationEntity(notification.id, notification.title, notification.desc, notification.type, notification.time, notification.isUnread))
    }

    suspend fun seedInitialData() {
        // Only seed once if tables are empty. However, we'll just insert with IGNORE.
        dao.insertNotifications(
            listOf(
                Notification(1, "Order Shipped!", "Your order #ORD12345678 has been shipped and is on its way.", "shipping", "2 hours ago", true),
                Notification(2, "Mega Sale Alert \uD83C\uDF89", "Up to 80% off on Winter Collection. Grab your favorites now!", "offer", "1 day ago", true),
                Notification(3, "Delivered Successfully", "Your order #ORD98765432 was delivered successfully.", "success", "5 days ago", false)
            ).map { 
                NotificationEntity(id = it.id, title = it.title, desc = it.desc, type = it.type, time = it.time, isUnread = it.isUnread)
            }
        )
        dao.insertOrders(
            MockData.orders.map {
                OrderEntity(it.id, it.title, it.price, it.image, it.status, it.statusText, it.trackingSteps)
            }
        )
        dao.insertAddress(AddressEntity(1, "Home", "Home", "Flat 402, Building A", "Koramangala", "Bangalore", "Karnataka", "560034", "9876543210"))
    }

    val orders: Flow<List<Order>> = dao.getAllOrders().map { entities -> 
        entities.map { Order(it.id, it.title, it.price, it.image, it.status, it.statusText, it.trackingSteps) }
    }

    suspend fun placeOrder(order: Order) {
        dao.insertOrder(OrderEntity(order.id, order.title, order.price, order.image, order.status, order.statusText, order.trackingSteps))
    }

    suspend fun updateOrder(order: Order) {
        dao.updateOrder(OrderEntity(order.id, order.title, order.price, order.image, order.status, order.statusText, order.trackingSteps))
    }

    val cart: Flow<List<CartItem>> = dao.getCart().map { entities -> 
        entities.map { CartItem(it.product, it.quantity) } 
    }

    suspend fun insertCartItem(item: CartItem) {
        dao.insertCartItem(CartItemEntity(item.product.id, item.product, item.quantity))
    }

    suspend fun removeCartItem(productId: Int) {
        dao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    val wishlist: Flow<List<Product>> = dao.getWishlist().map { entities -> 
        entities.map { it.product } 
    }

    suspend fun addWishlist(product: Product) {
        dao.insertWishlist(WishlistEntity(product.id, product))
    }

    suspend fun removeWishlist(productId: Int) {
        dao.deleteWishlist(productId)
    }

    // Chat
    val chatMessages: Flow<List<ChatMessage>> = dao.getChatMessages().map { entities -> 
        entities.map { ChatMessage(it.id, it.text, it.isUser, it.timestamp) } 
    }

    suspend fun addChatMessage(message: ChatMessage) {
        dao.insertChatMessage(ChatMessageEntity(text = message.text, isUser = message.isUser, timestamp = message.timestamp))
    }

    suspend fun clearChatMessages() {
        dao.clearChatMessages()
    }
}

