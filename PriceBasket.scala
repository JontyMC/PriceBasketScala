import com.github.nscala_time.time.Imports._

sealed case class Product(id: String, price: BigDecimal)
sealed case class AppliedSpecialOffer(displayText: String, discount: BigDecimal)

trait SpecialOffer {
  def isValid(): Boolean
  def displayText(): String
  def calculateDiscount(products: Seq[Product]): BigDecimal
}

class ScheduledPercentageOffer(qualifyingProduct: Product, percentageDiscount: Int, now: DateTime, start: DateTime, end: DateTime) extends SpecialOffer {
  def isValid = { start < now && now < end }
  def displayText = { f"${qualifyingProduct.id} ${percentageDiscount}%% off" }
  def calculateDiscount(products: Seq[Product]) = {
    products
      .filter(x => x == qualifyingProduct)
      .map(x => x.price * BigDecimal(percentageDiscount) / BigDecimal(100))
      .sum 
  }
} 

class HalfPriceOffer(qualifyingProduct: Product, quantityToQualify: Int, discountProduct: Product) extends SpecialOffer {
  def isValid = { true }
  def displayText = { f"Buy $quantityToQualify ${qualifyingProduct.id} get ${discountProduct.id} half price" }
  def calculateDiscount(products: Seq[Product]) = {
    val qualifyingProductCount = products.filter(x => x == qualifyingProduct).length
    val qualifyingCount = qualifyingProductCount / quantityToQualify
    val discountProductCount = products.filter(x => x == discountProduct).take(qualifyingCount).length
    BigDecimal(qualifyingCount) * discountProduct.price / BigDecimal(2)
  }
}

object PriceBasket {
  val availableProducts = List(
    Product("Soup", 0.65),
    Product("Bread", 0.80),
    Product("Milk", 1.30),
    Product("Apples", 1.00)
  )
  
  val productById = (id: String) => availableProducts.find(x => x.id == id).get
  
  val getSelectedProducts = (productIds: List[String]) => productIds.map(productById)
  
  val getAvailableOffers = (now: DateTime) => {
    List(
      new ScheduledPercentageOffer(productById("Apples"), 10, now, DateTime.parse("2016-1-1"), DateTime.parse("2017-1-1")),
      new HalfPriceOffer(productById("Soup"), 2, productById("Bread"))
    )
  }
  
  val calculateSubtotal = (selectedProducts: List[Product]) => selectedProducts.map(x => x.price).sum
  
  val applyOffers = (availableOffers: List[SpecialOffer], selectedProducts: List[Product]) => {
    availableOffers
      .filter(x => x.isValid())
      .map(x => AppliedSpecialOffer(x.displayText(), x.calculateDiscount(selectedProducts)))
      .filter(x => x.discount > 0)
  }

  val calculateTotal = (subtotal: BigDecimal, appliedOffers: List[AppliedSpecialOffer]) => {
    val totalDiscount = appliedOffers.map(x => x.discount).sum
    subtotal - totalDiscount
  }
  
  val renderTotal = (subtotal: BigDecimal, appliedOffers: List[AppliedSpecialOffer], total: BigDecimal) => {
    val displayOffer = (appliedOffer: AppliedSpecialOffer) => {
      println(f"${appliedOffer.displayText}: -${appliedOffer.discount}%1.2f")
    }
    
    println(f"Subtotal: $subtotal%1.2f")
    if (appliedOffers.isEmpty) {
      println("(No offers available)")
    } else {
      appliedOffers.foreach { displayOffer }
    }
    println(f"Total: $total%1.2f")
  }
  
  val totalBasket = (productIds: List[String], now: DateTime) => {
    val selectedProducts = getSelectedProducts(productIds)
    val subtotal = calculateSubtotal(selectedProducts)
    val availableOffers = getAvailableOffers(now)
    val appliedOffers = applyOffers(availableOffers, selectedProducts)
    val total = calculateTotal(subtotal, appliedOffers)
    renderTotal(subtotal, appliedOffers, total)
  }
  
  def main(args: Array[String]): Unit = {
    totalBasket(args.toList, DateTime.now)
  }
}