FROM ubuntu:20.04

ENV TZ=Europe/Warsaw

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update
RUN apt-get install -y openjdk-17-jre-headless 

RUN apt-get install -y sudo curl unzip zip

RUN useradd -ms /bin/bash saturnin
RUN adduser saturnin sudo

RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> \
/etc/sudoers


WORKDIR /home/saturnin

RUN curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.44.0/ktlint && chmod a+x ktlint
RUN mv /home/saturnin/ktlint /usr/bin/ktlint

USER saturnin

RUN curl -s https://get.sdkman.io | bash

RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    sdk install kotlin && sdk install gradle"

COPY ./ ./
