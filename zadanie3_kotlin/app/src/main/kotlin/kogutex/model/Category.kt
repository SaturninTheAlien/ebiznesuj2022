package kogutex.model;


data class Category(
    val id:Int,
    val name:String,
    val parent_id: Int? = null,
    val abstract:Boolean=false,
)

object CategoryRepository{
    private var mCategoryList:Array<Category> = arrayOf(
        Category(0, "Żywe kurczaki", abstract=true),
        Category(1, "Kury", parent_id=0),
        Category(2, "Koguty", parent_id=0),
        Category(3, "Pokarmy"),
        Category(5, "Akcesoria"),
        Category(6, "Kurniki"),
    )

    fun allCategories(): Array<Category>{
        return this.mCategoryList;
    }

    fun getCategotyById(id:Int): Category?{
        return this.mCategoryList.find { category -> category.id==id }
    }

    fun getCategoryByName(name:String): Category?{
        return this.mCategoryList.find { category -> category.name==name }
    }

    fun getParentCategory(c:Category): Category?{
        if(c.parent_id==null)return null;
        return getCategotyById(c.parent_id);
    }

    fun getChildCategoriesID(id:Int): Set<Int> {
        var r = this.mCategoryList.filter { c -> c.parent_id == id }.map { c -> c.id }.toMutableSet();
        r.add(id);
        return r;
    }
}