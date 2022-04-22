package kogutex.model;


data class Product(
    val id: Int,
    val category_id: Int,
    val name: String,
    val price: Int,
    val description: String? = null,
    val picture: String? = null,
)

object ProductRepository{
    private var mProductsList: Array<Product> = arrayOf(
        Product(0, 3, "Pasza Kurka Naturka 25 kg", 80,
        "Pasza KURKA NATURKA w formie kruszonki dedykowana dla piskląt w wieku 0 - 6 tygodnia życia."),
        Product(1, 4, "Inkubator do jaj klujnik wylęgarka", 349,
        "Prosty w obsłudze, posiada możliwość regulacji temperatury", "/data/accessories/t01.png"),
        Product(2, 1, "Kura zielononóżka", 50,
        "Data wyklucia 15 lipca 2021", "/data/live_chicken/001.png"),
        Product(3, 2, "Kogut sussex", 100,
        "Data wyklucia 7 lutego 2020", "/data/live_chicken/002.png"),
        Product(4, 1, "Kura Rhode Island Red", 80),
        Product(5, 4, "Kask dla kurczaka. xD", 300),
        Product(6, 3, "Larwy mącznika młynarka, porcja", 10, "Robaki jako \"karmówka\""),
    )

    fun allProducts(): Array<Product>{
        return this.mProductsList;
    }

    fun getProductById(id:Int): Product?{
        return this.mProductsList.find { p->p.id==id };
    }

    fun getProductByName(name:String): Product?{
        return this.mProductsList.find { p-> p.name==name };
    }

    fun getProductsByCategory(category_id:Int): Array<Product>{
        val s1:Set<Int> = CategoryRepository.getChildCategoriesID(category_id);
        return this.mProductsList.filter {p->s1.contains(p.category_id)}.toTypedArray()
    }
}