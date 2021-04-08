FROM adoptopenjdk:11-jre-openj9

RUN mkdir /opt/app
COPY ./vending-machine /opt/app/vending-machine

CMD ["/opt/app/vending-machine/bin/vending-machine"]