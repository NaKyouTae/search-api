# 특정고객 거래내역 조회서비스
## 목차
[1. 개발 환경](#개발 환경)  
[2. 빌드 및 실행 방법](#빌드 및 실행 방법)   
[3. 문제 해결 방법](#문제 해결 방법)

## 개발 환경
> **자바** : OpenJdk 14  
> **프레임워크** : Spring Boot 2.5.6  
> **빌드** : Gradle 7.2  
> **ORM** : JPA  
> **DataBase** : H2 DB  
> **Log** : logback 
> - 로깅 주기 : 1 Day 
> - 변경 주기 : 60초 마다 확인  
> 
> **API 명세** : Swagger2 2.9.2 
> - URL : /swagger-ui.html

## 빌드 및 실행 방법
### 빌드
> - Gradle Terminal을 이용한 빌드
>   1. gradle 명령어 실행 가능한 Terminal에서 ***gradlew clean build*** 명령어 실행
> 
> - IDE를 이용한 빌드
>   1. IDE에서 gradle tasks의 ***clean task*** 후 ***build task*** 진행
> 
> 위의 빌드 과정을 거치고 나서 ***/build/lib***에 위치한 ***kpsec-search-api-xxxx.jar*** 파일을 ***java -jar kpsec-search-api-xxxx.jar*** 명령어를 통해서 실행 후 어플리케이션 접속

### 실행 방법
#### 어플리케이션 정보 및 실행
> **port** : 8080  
> **실행 URL** : http://localhost:8080/...
> - IDE를 이용한 실행
>   1. git repository를 내려받아 제공된 build 방법을 통해 프로젝트를 빌드한다.
>   2. SearchApiApplication
#### Swagger 실행
> **실행 URL** : http://localhost:8080/swagger-ui.html
> 
## API 기능 명세
1. 연도별 합계 금액이 가장 많은 고객 조회 API
2. 거래가 없는 고객을 조회하는 API
3. 연도별, 관리점별 거래금액 합계가 가장 큰 순서로 조회하는 API
4. 지점의 거래금액 합계를 조회하는 API


1. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발
 - 단, 취소여부가 'Y'인 거래는 취소된 거래임,
 - 합계 금액은 거래금액에서 수수료를 차감한 금액임

2. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발
 - 단, 취소여부가 'Y'인 거래는 취소된 거래임

3. 연도별, 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발
 - 단, 취소여부가 'Y'인 거래는 취소된 거래임

4. 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면 해당 지점의 거래금액 합계를 출력하는 API 개발
 - 단, 취소여부가 'Y'인 거래는 취소된 거래임
 - 분당점 출력시 http status 404 출력