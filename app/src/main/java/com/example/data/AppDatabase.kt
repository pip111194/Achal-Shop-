package com.example.data

import androidx.room.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()

    private val trackingStepsAdapter = moshi.adapter<List<TrackingStep>>(
        Types.newParameterizedType(List::class.java, TrackingStep::class.java)
    )

    private val productAdapter = moshi.adapter(Product::class.java)

    @TypeConverter
    fun fromTrackingSteps(list: List<TrackingStep>): String = trackingStepsAdapter.toJson(list)

    @TypeConverter
    fun toTrackingSteps(json: String): List<TrackingStep> = trackingStepsAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromProduct(product: Product): String = productAdapter.toJson(product)

    @TypeConverter
    fun toProduct(json: String): Product? = productAdapter.fromJson(json)
}

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val flat: String,
    val area: String,
    val city: String,
    val state: String,
    val pincode: String,
    val phone: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val desc: String,
    val type: String,
    val time: String,
    val isUnread: Boolean
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val price: Int,
    val image: String,
    val status: String,
    val statusText: String,
    val trackingSteps: List<TrackingStep>
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: Int,
    val product: Product,
    val quantity: Int
)

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val productId: Int,
    val product: Product
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

@Dao
interface AppDao {
    // Addresses
    @Query("SELECT * FROM addresses")
    fun getAllAddresses(): kotlinx.coroutines.flow.Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteAddress(id: Int)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): kotlinx.coroutines.flow.Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isUnread = 0")
    suspend fun markAllNotificationsRead()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    // Orders
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): kotlinx.coroutines.flow.Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
    
    @Update
    suspend fun updateOrder(order: OrderEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    // Cart
    @Query("SELECT * FROM cart_items")
    fun getCart(): kotlinx.coroutines.flow.Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Wishlist
    @Query("SELECT * FROM wishlist_items")
    fun getWishlist(): kotlinx.coroutines.flow.Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(item: WishlistEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteWishlist(productId: Int)

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): kotlinx.coroutines.flow.Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)
    
    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()
}

@Database(entities = [AddressEntity::class, NotificationEntity::class, OrderEntity::class, CartItemEntity::class, WishlistEntity::class, ChatMessageEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
