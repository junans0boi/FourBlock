#!/bin/bash

# 프로젝트 디렉토리로 이동
cd /home/ubuntu/Project/FourBlock

# 기존 빌드 파일 정리 후 새로 빌드
./gradlew clean build

# 새로운 WAR 파일을 Tomcat의 웹 애플리케이션 폴더로 복사하고 ROOT.war로 이름 변경
sudo cp build/libs/NaverShoppingSearch_Project.war /opt/tomcat/webapps/ROOT.war

# Tomcat 재시작
sudo systemctl restart tomcat

echo "WAR 파일이 ROOT.war로 복사되고 Tomcat이 재시작되었습니다."
