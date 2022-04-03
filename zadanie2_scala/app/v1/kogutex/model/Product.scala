package v1.kogutex.model

import play.api.http.Status.NOT_FOUND
import play.api.libs.json.{Format, Json}
import play.api.mvc.AnyContent

case class Product(id:Int,
                   category_id:Int,
                   name:String,
                   price:Int,
                   description:String,
                   picture:String=null,
                   chicken_breed_id:Option[Int]=None)


object Product {
  implicit val format: Format[Product] = Json.format
}


private case class ProductIn(category_id:Int,
                             name:String,
                             price:Int,
                             description:String=null,
                             picture:String=null,
                             chicken_breed_id:Option[Int]=None
                            )

private object ProductIn {
  implicit val format: Format[ProductIn] = Json.format
}


object ProductRepository{
  private var id_counter = 4
  private var productsList = List[Product](
    Product(0, 3, "Pasza Kurka Naturka 25 kg", 80,
      "Pasza KURKA NATURKA w formie kruszonki dedykowana dla piskląt w wieku 0 - 6 tygodnia życia."),
    Product(1, 5, "Inkubator do jaj klujnik wylęgarka", 349,
      "Prosty w obsłudze, posiada możliwość regulacji temperatury", "/data/accessories/t01.png"),
    Product(2, 1, "Kura zielononóżka", 50,
      "Data wyklucia 15 lipca 2021", "/data/live_chicken/001.png", Some(3)),
    Product(3, 2, "Kogut sussex", 100,
     "Data wyklucia 7 lutego 2020", "/data/live_chicken/002.png", Some(0))
  )

  def all: List[Product] ={
    this.productsList
  }

  def get(id:Int) : Option[Product] = {
    this.productsList.find((product=>product.id==id))
  }

  def delete(id:Int): Boolean = {
    val tmp = this.get(id)
    if(tmp.isDefined){
      this.productsList=this.productsList.filter(product=>product.id!=id)
      true
    }
    else{
      false
    }
  }


  private def processJson(id:Int, content:AnyContent): Option[Product] = {
    val jsonObj = content.asJson
    val tmp1 = jsonObj.flatMap(Json.fromJson[ProductIn](_).asOpt)

    if(tmp1.isDefined){

      val category_id = tmp1.get.category_id
      val category = CategoryRepository.get(category_id)
      val chicken_breed_id = tmp1.get.chicken_breed_id

      if(category.isEmpty ){
        throw new RuntimeException("Incorrect category id")
      }
      else if((category.get.live_birds != chicken_breed_id.isDefined)){
        if(category.get.live_birds){
          throw new RuntimeException("Trying to give chicken_breed_id to a product which is not a live bird")
        }
        else{
          throw new RuntimeException("chicken_breed_id field required for this product category")
        }
      }

      else{
        val product = Product(id, category_id, tmp1.get.name, tmp1.get.price,tmp1.get.description, tmp1.get.picture, tmp1.get.chicken_breed_id)
        Some(product)
      }
    }
    else{
      None
    }
  }

  def post(content:AnyContent): Option[Product] = {
    val id = id_counter
    id_counter+=1
    val product = processJson(id, content)
    if(product.isDefined) {
      this.productsList = this.productsList :+ product.get
    }
    product
  }

  def put(id:Int, content:AnyContent): Option[Product] = {
    if(id >= id_counter)id_counter=id+1
    val product = processJson(id, content)
    if(product.isDefined){
      if(this.get(id).isDefined){
        this.delete(id)
      }
      this.productsList = this.productsList :+ product.get
    }
    product
  }

}