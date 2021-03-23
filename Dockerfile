FROM hseeberger/scala-sbt:11.0.6_1.3.7_2.12.10

RUN mkdir -p /talkdeskdockerapp
RUN mkdir -p /talkdeskdockerapp/out

WORKDIR /talkdeskdockerapp

EXPOSE 8080/tcp

COPY . /talkdeskdockerapp
CMD ["sbt", "run"]