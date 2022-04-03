package v1.kogutex.model

import play.api.libs.json.{Format, Json}
import play.api.mvc.AnyContent

case class ChickenBreed(id:Int, name:String,
                        eggs_per_year:String,
                        hen_picture:String,
                        rooster_picture:String,
                        cold_hardy:Boolean)


object ChickenBreed{
  implicit val format: Format[ChickenBreed] = Json.format
}

private case class ChickenBreedIn1(name:String,
                                   eggs_per_year:String,
                                   hen_picture:Option[String],
                                   rooster_picture:Option[String],
                                   cold_hardy:Boolean)



private object ChickenBreedIn1{
  implicit val format: Format[ChickenBreedIn1] = Json.format
}


object ChickenBreedRepository{
  private var id_counter = 4
  private var breedsList = List[ChickenBreed](
    ChickenBreed(0, "Sussex", "250-300", "/hens/sussex1.png", "/roosters/sussex.png", true),
    ChickenBreed(1, "Rhode Island Red", "200+", "/hens/r1.png", "/roosters/r1.png", true),
    ChickenBreed(2, "Red Jungle Fowl", "~100", "/hens/rjf.png", "/roosters/rjf.png", false),
    ChickenBreed(3, "Zielononóżka", "~100", null, null, true),
  )

  def all() : List[ChickenBreed] = {
    breedsList
  }

  def get(id:Int): Option[ChickenBreed] = {
    this.breedsList.find(o=>o.id==id)
  }

  def delete(id:Int): Boolean = {
    val tmp = this.get(id)
    if(tmp.isDefined){
      this.breedsList = this.breedsList.filter(o=>o.id!=id)
      true
    }
    else{
      false
    }
  }

  private def processJson(id:Int, content:AnyContent): Option[ChickenBreed] = {
    val jsonObj = content.asJson
    val tmp1 = jsonObj.flatMap(Json.fromJson[ChickenBreedIn1](_).asOpt)
    if(tmp1.isDefined) {
      val r =ChickenBreed(id,tmp1.get.name,
        tmp1.get.eggs_per_year,
        tmp1.get.hen_picture.getOrElse(null),
        tmp1.get.rooster_picture.getOrElse(null),
        tmp1.get.cold_hardy)

      Some(r)
    }
    else{
      None
    }
  }

  def post(content:AnyContent): Option[ChickenBreed] = {
    val id = id_counter
    id_counter+=1

    val result = processJson(id, content)
    if(result.isDefined){
      this.breedsList = this.breedsList :+ result.get
    }
    result
  }

  def put(id:Int, content: AnyContent) : Option[ChickenBreed] = {
    if(id >= id_counter) id_counter = id+1

    val result = processJson(id, content)
    if(result.isDefined) {

      if (this.get(id).isDefined) {
        this.delete(id)
      }

      this.breedsList = this.breedsList :+ result.get
    }
    result
  }
}