package v1.kogutex.model

import play.api.libs.json.{Format, Json}
import play.api.mvc.{AnyContent, Request}

case class Category(id:Int, name:String,live_birds:Boolean=false, child_categories:List[Int] = List())

object Category {
  implicit val format: Format[Category] = Json.format
}


private case class CategoryJsonIn(name:String,live_birds:Boolean=false, child_categories:Option[List[Int]]=None)

private object CategoryJsonIn{
  implicit val format: Format[CategoryJsonIn] = Json.format
}


object CategoryRepository{
  private var id_counter = 6

  private var categoryList = List[Category](
    Category(0, "Żywe kurczaki", true, List(1,2)),
    Category(1, "Kury", true),
    Category(2, "Koguty", true),
    Category(3, "Pokarmy", false),
    Category(4, "Kurniki", false),
    Category(5, "Akcesoria", false),
  )

  def all(): List[Category] = {
    this.categoryList
  }

  private def processCategoryJson(id:Int, content: AnyContent): Option[Category] = {
    val jsonObj = content.asJson

    val c1: Option[CategoryJsonIn] = jsonObj.flatMap(Json.fromJson[CategoryJsonIn](_).asOpt)
    if(c1.isDefined) {
      Some(Category(id, c1.get.name, c1.get.live_birds, c1.get.child_categories.getOrElse(List())))
    }
    else {
      None
    }
  }

  def post(content: AnyContent): Option[Category] = {
    val id = id_counter
    id_counter+=1

    val category = processCategoryJson(id, content)
    if(category.isDefined)
      {
        this.categoryList = this.categoryList :+ category.get
      }
      category
  }

  def put(id:Int, content: AnyContent) = {

    if(id>=id_counter)
      id_counter = id+1

    val category = processCategoryJson(id, content)
    if(category.isDefined) {
      if(this.get(id).isDefined){
        this.delete(id)
      }
      this.categoryList = this.categoryList :+ category.get
    }

    category
  }

  def get(id: Int): Option[Category] = {
    this.categoryList.find(o => o.id==id)
  }

  def delete(id: Int): Boolean = {

    val category = get(id);
    if(category.isEmpty) {
      false
    }
    else {
      this.categoryList = this.categoryList.filter(o => o.id!=id)
      true
    }
  }

}
