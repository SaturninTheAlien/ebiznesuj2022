package v1.kogutex

class ModelId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object ModelId {

  def apply(i:Int) : ModelId = {
    new ModelId(i)
  }

  def apply(raw: String): ModelId = {
    require(raw != null)
    new ModelId(Integer.parseInt(raw))
  }
}
