package com.example.data

object MockData {
    val categories = listOf(
        Category(1, "Smartphones", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=300&q=80", "Latest Mobiles"),
        Category(2, "Laptops", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=300&q=80", "Work & Play"),
        Category(3, "Audio", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=300&q=80", "Best Sound"),
        Category(4, "Watches", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=300&q=80", "Smart Wearables"),
        Category(5, "Cameras", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=300&q=80", "Photography"),
        Category(6, "Gaming", "https://images.unsplash.com/photo-1605901309584-818e25960b8f?auto=format&fit=crop&w=300&q=80", "Console & More")
    )

    val products = listOf(
        Product(101, "Pro Max Smartphone 5G", 85000, 95000, 4.8f, 2300, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", true),
        Product(102, "Ultra Slim Notebook 14\"", 65000, 75000, 4.5f, 1200, "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", true),
        Product(103, "Noise Cancelling Headphones", 12000, 15000, 4.7f, 3400, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", false),
        Product(104, "Smartwatch Series X", 25000, 30000, 4.6f, 850, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", true),
        Product(105, "4K Mirrorless Camera", 150000, 165000, 4.9f, 420, "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", false),
        Product(106, "Next-Gen Gaming Console", 49990, 54990, 4.8f, 5600, "https://images.unsplash.com/photo-1605901309584-818e25960b8f?auto=format&fit=crop&w=400&q=80", listOf(
            "https://images.unsplash.com/photo-1605901309584-818e25960b8f?auto=format&fit=crop&w=800&q=80"
        ), "Free Delivery", true)
    )
    
    val banners = listOf(
        Banner(1, "https://images.unsplash.com/photo-1550009158-9effb64fda70?auto=format&fit=crop&q=80&w=800", "Gadget Carnival!", "Up to 50% off on electronics"),
        Banner(2, "https://images.unsplash.com/photo-1531297172867-4f50f1641031?auto=format&fit=crop&q=80&w=800", "Upgrade Your Tech", "Exchange offers on Laptops"),
        Banner(3, "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?auto=format&fit=crop&q=80&w=800", "Free Delivery", "On orders above ₹499")
    )
    
    val subcategoriesData = mapOf(
        1 to listOf(
            Subcategory("1-1", "Apple", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=200&q=80"),
            Subcategory("1-2", "Samsung", "https://images.unsplash.com/photo-1610945264803-c22b6272bc8b?auto=format&fit=crop&w=200&q=80"),
            Subcategory("1-3", "OnePlus", "https://images.unsplash.com/photo-1598327105666-5b89351aff97?auto=format&fit=crop&w=200&q=80"),
            Subcategory("1-4", "Google Pixel", "https://images.unsplash.com/photo-1596742578443-7682ef5251cd?auto=format&fit=crop&w=200&q=80"),
            Subcategory("1-5", "Xiaomi", "https://images.unsplash.com/photo-1621644755106-ca70ab034b7f?auto=format&fit=crop&w=200&q=80"),
            Subcategory("1-6", "Accessories", "https://images.unsplash.com/photo-1593344484962-796055d4a3a4?auto=format&fit=crop&w=200&q=80")
        ),
        2 to listOf(
            Subcategory("2-1", "MacBooks", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=200&q=80"),
            Subcategory("2-2", "Gaming Laptops", "https://images.unsplash.com/photo-1603302576837-37561b2e2302?auto=format&fit=crop&w=200&q=80"),
            Subcategory("2-3", "Ultrabooks", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=200&q=80"),
            Subcategory("2-4", "2-in-1s", "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?auto=format&fit=crop&w=200&q=80"),
            Subcategory("2-5", "Business", "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?auto=format&fit=crop&w=200&q=80"),
            Subcategory("2-6", "Peripherals", "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?auto=format&fit=crop&w=200&q=80")
        ),
        3 to listOf(
            Subcategory("3-1", "Headphones", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=200&q=80"),
            Subcategory("3-2", "Earbuds", "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?auto=format&fit=crop&w=200&q=80"),
            Subcategory("3-3", "Speakers", "https://images.unsplash.com/photo-1545454675-a631fca618be?auto=format&fit=crop&w=200&q=80"),
            Subcategory("3-4", "Soundbars", "https://images.unsplash.com/photo-1611854580630-fce411bd1317?auto=format&fit=crop&w=200&q=80"),
            Subcategory("3-5", "Home Theater", "https://images.unsplash.com/photo-1593640498182-edeb0a624aa7?auto=format&fit=crop&w=200&q=80"),
            Subcategory("3-6", "Microphones", "https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?auto=format&fit=crop&w=200&q=80")
        ),
        4 to listOf(
            Subcategory("4-1", "Apple Watch", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=200&q=80"),
            Subcategory("4-2", "Smartwatches", "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=200&q=80"),
            Subcategory("4-3", "Fitness Bands", "https://images.unsplash.com/photo-1575311373937-040b8e1fd5b0?auto=format&fit=crop&w=200&q=80"),
            Subcategory("4-4", "Analog Sync", "https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3?auto=format&fit=crop&w=200&q=80"),
            Subcategory("4-5", "Kids Watches", "https://images.unsplash.com/photo-1514316454349-750a7fd3da3a?auto=format&fit=crop&w=200&q=80"),
            Subcategory("4-6", "Straps", "https://images.unsplash.com/photo-1559864778-9533f009f583?auto=format&fit=crop&w=200&q=80")
        ),
        5 to listOf(
            Subcategory("5-1", "Mirrorless", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=200&q=80"),
            Subcategory("5-2", "DSLR", "https://images.unsplash.com/photo-1502982720700-baf97d4220a2?auto=format&fit=crop&w=200&q=80"),
            Subcategory("5-3", "Action Cams", "https://images.unsplash.com/photo-1560271167-93822a36b3f7?auto=format&fit=crop&w=200&q=80"),
            Subcategory("5-4", "Drones", "https://images.unsplash.com/photo-1527977966376-1c8408f9f108?auto=format&fit=crop&w=200&q=80"),
            Subcategory("5-5", "Lenses", "https://images.unsplash.com/photo-1534067332308-f86a249c5e3d?auto=format&fit=crop&w=200&q=80"),
            Subcategory("5-6", "Tripods", "https://images.unsplash.com/photo-1616161942171-84196c8028ff?auto=format&fit=crop&w=200&q=80")
        ),
        6 to listOf(
            Subcategory("6-1", "PlayStation", "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?auto=format&fit=crop&w=200&q=80"),
            Subcategory("6-2", "Xbox", "https://images.unsplash.com/photo-1605901309584-818e25960b8f?auto=format&fit=crop&w=200&q=80"),
            Subcategory("6-3", "Nintendo", "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?auto=format&fit=crop&w=200&q=80"),
            Subcategory("6-4", "Controllers", "https://images.unsplash.com/photo-1600000455018-971dc3183863?auto=format&fit=crop&w=200&q=80"),
            Subcategory("6-5", "VR Headsets", "https://images.unsplash.com/photo-1622979135225-d2ba269cf1ac?auto=format&fit=crop&w=200&q=80"),
            Subcategory("6-6", "Games", "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=200&q=80")
        )
    )
    
    val orders = listOf(
        Order("ORD12345678", "Ultra Slim Notebook 14\"", 65000, "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=400&q=80", "in_transit", "Arriving by Tomorrow", listOf(
            TrackingStep("Order Confirmed", "Today, 10:30 AM", true),
            TrackingStep("Shipped", "Today, 02:15 PM", true),
            TrackingStep("Out for Delivery", "Pending", false),
            TrackingStep("Delivered", "Pending", false)
        )),
        Order("ORD98765432", "Noise Cancelling Headphones", 12000, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=400&q=80", "delivered", "Delivered Successfully", listOf(
            TrackingStep("Order Confirmed", "10 May, 11:00 AM", true),
            TrackingStep("Shipped", "11 May, 09:30 AM", true),
            TrackingStep("Out for Delivery", "12 May, 08:45 AM", true),
            TrackingStep("Delivered", "12 May, 02:20 PM", true)
        ))
    )
}

data class Category(val id: Int, val name: String, val image: String, val label: String)
@com.squareup.moshi.JsonClass(generateAdapter = true)
data class Product(
    val id: Int, val title: String, val price: Int, val originalPrice: Int, 
    val rating: Float, val reviews: Int, val image: String, val images: List<String>, 
    val delivery: String, val isPopular: Boolean
)
data class Banner(val id: Int, val image: String, val title: String, val subtitle: String)
data class Subcategory(val id: String, val name: String, val image: String)
data class Order(val id: String, val title: String, val price: Int, val image: String, val status: String, val statusText: String, val trackingSteps: List<TrackingStep>)
@com.squareup.moshi.JsonClass(generateAdapter = true)
data class TrackingStep(val label: String, val date: String, val completed: Boolean)
data class CartItem(val product: Product, var quantity: Int)
data class Address(val id: Int, val name: String, val type: String, val flat: String, val area: String, val city: String, val state: String, val pincode: String, val phone: String)
data class Notification(val id: Int, val title: String, val desc: String, val type: String, val time: String, val isUnread: Boolean)
data class ChatMessage(val id: Int = 0, val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

