import java.util.Calendar

sealed case class Product(id: String, price: BigDecimal)
sealed case class AppliedSpecialOffer(displayText: String, discount: BigDecimal)

trait SpecialOffer {
  def IsValid(x: Unit => Boolean)
  def DisplayText(x: Unit => String)
  def CalculateDiscount(x: Seq[Product] => BigDecimal)
}

object PriceBasket {
  val availableProducts = List(
    ("Soup", 0.65),
    ("Bread", 0.8),
    ("Milk", 1.3),
    ("Apples", 1)
  )

  val total = (subtotal:BigDecimal) => {
    subtotal + 1
  }
  
  def main(args: Array[String]): Unit = {
    val now = Calendar.getInstance().getTime()
    println(availableProducts.length)
  }
}