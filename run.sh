rm -rf bin
mkdir -p bin
scalac -d bin PriceBasket.scala
scala -classpath bin PriceBasket