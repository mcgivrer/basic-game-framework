#!/bin/bash
# setup full build environment
# copyright 2018
# Frédéric Delorme
#-----
# update environment
apt-get update 
# install minimum platform
apt-get -y install xvfb
apt-get -y install git
apt-get -y install wget
apt-get -y install openjdk-8-jdk
apt-get -y install openjfx
# Install Maven 
mkdir -p ~/tools/maven
cd ~/tools/maven
wget ftp://ftp.cixug.es/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.zip
unzip -x apache-maven-3.5.4-bin.zip
export PATH=$PATH:~/tools/maven/apache-maven-3.5.4-bin/bin:.
# prepapre project
mkdir ~/projects/
cd ~/projects
git clone git@github.com:Snapgames/basic-game-framework.git
# start X11 for tests
#export DISPLAY=:99.0
#sh -e /etc/init.d/xvfb start
service x11-common start
sleep 3
# Build project
mvn clean install -f "~/projects/basic-game-framework/pom.xml"
