FROM hseeberger/scala-sbt

RUN sbt

COPY build.sbt /app/
WORKDIR /app
RUN ["sbt", "update"]

COPY . /app

RUN ["sbt", "compile"]

ENTRYPOINT ["sbt", "run"]