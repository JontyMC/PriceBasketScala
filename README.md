Build the code with sbt (requires Java 8, Scala and sbt):
```
sbt compile "run Soup Soup Bread Apples Milk"
```
Alternatively, run with Docker without installing any dependencies (other than Docker):
```
docker build -t pricebasket .
docker run --rm --entrypoint=sbt pricebasket "run Soup Soup Bread Apples Milk"
```

A port of a [program I wrote in F#](https://github.com/JontyMC/Price-Basket-F-). Not production ready, eg doesn't check for input errors. My first attempt at writing a program in Scala, go easy...