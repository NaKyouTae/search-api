# 특정고객 거래내역 조회 서비스
## 목차
[1. 개발 환경](#개발-환경)  
[2. 빌드 및 실행 방법](#빌드-및-실행-방법)   
[3. 문제 해결 방법](#문제-해결-방법)

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
> 위의 빌드가 성공하고 나서 /build/lib 폴더에 위치한 ***kpsec-search-api-xxxx.jar*** 파일을 ***java -jar kpsec-search-api-xxxx.jar*** 명령어를 통해서 실행 후 어플리케이션 접속

### 실행 방법
#### 어플리케이션 정보 및 실행
> **port** : 8080  
> **실행 URL** : http://localhost:8080/...
> - IDE를 이용한 실행
>   1. git repository를 내려받아 제공된 build 방법을 통해 프로젝트를 빌드한다.
>   2. SearchApiApplication 메인 클래스를 실행시켜 어플리케이션을 구동한다.
> - jar파일을 이용한 실행
>   1. git repository를 내려 받아 제공된 build 방법을 통해 프로젝트를 빌드한다.
>   2. 빌드가 성공적으로 끝나면 /build/lib 폴더 안에 ***kpsec-search-api-0.0.1-SNAPSHOT.jar*** 파일을 ***java -jar kpsec-search-api-0.0.1-SNAPSHOT.jar*** 명령어를 통해 어플리케이션을 구동한다. 
#### Swagger 이용 방법
> 어플리케이션이 실행 되어 있는 상태에서 접근 가능 하며 아래의 URL을 통해 접근한다.  
> 
> **실행 URL** : http://localhost:8080/swagger-ui.html

## 문제 해결 방법
> * **참고 사항**   
> ORM으로 JPA를 사용하였지만 문제를 보고 어떤 데이터가 나와야 할지 머리속으로 그려지지 않아 H2 DB에서 SQL로 먼저 결과를 간략하게 만들어 참고하여 비즈니스 로직을 구현하였습니다.  
> 그래서 사용한 SQL문도 같이 공유드리니 참고 부탁드립니다.

### 문제 1. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발
> * **문제 1의 조건**
>   1. 단, 취소여부가 'Y'인 거래는 취소된 거래임,
>   2. 합계 금액은 거래금액에서 수수료를 차감한 금액임
> 
> 
> * **문제 1의 해결 방법**
>   1. 2018년, 2019년의 데이터만 조회하는 것이 아닌 입력으로 연도의 목록을 받아 받은 연도들의 데이터를 반환하도록 확장하였습니다.
>   2. 연도 목록과 계좌 전체 목록을 이중 배열로 구성하여 각 연도별 계좌들의 총 거래 금액을 구하는 로직을 구현하였습니다. 
>   3. 2번에서 구한 데이터에서 총 금액(총 거래 금액 - 수수료)으로 정렬을 하여 가장 높은 금액의 계좌를 추출하는 로직을 구현하였습니다.
>   4. 연도를 기준으로 오름차순으로 정렬하여 결과를 반환하였습니다.
>
> 
> * **참고 SQL**  
>   - 2019년도 거래 금액이 가장 많은 고객 조회 SQL   
>     > select   
      a.*,  
      (select **sum(amount)** from **kpsec_tb_transaction_history** h where a.account_no = h.account_no and transaction_date like '2019%' and cancel_yn = false) as **sumAmt**,  
      (select **sum(commission)** from **kpsec_tb_transaction_history** h where a.account_no = h.account_no and transaction_date like '2019%' and cancel_yn = false) as **sumCommission**,  
      from **kpsec_tb_account** a  
      order by **sumAmt** desc;

### 문제 2. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발
> * **문제 2의 조건**
>   - 단, 취소여부가 'Y'인 거래는 취소된 거래임
> 
> 
> * **문제 2의 해결 방법**
>   1. 2018년, 2019년의 데이터만 조회하는 것이 아닌 입력으로 연도의 목록을 받아 받은 연도들의 데이터를 반환하도록 확장하였습니다.
>   2. 연도를 기준으로 거래 내역들을 조회하는 기능을 구현했습니다.
>   3. 연도별 거래 내역들의 계좌 번호를 중복제거하여 리스트로 추출하였습니다.
>   4. 추출한 거래내역 내의 계좌 번호들을 전체 계좌에 비교하여 거래 내역이 없는 계좌를 추출하여 반환하였습니다. 
> 
> 
> * **참고 SQL**
>   - 거래 내역이 없는 고객 조회 SQL  
>     > select * from **kpsec_tb_account** a   
            where a.account_no not in   
       (select **distinct(h.account_no)** from **kpsec_tb_transaction_history** h where transaction_date like '2018%' and cancel_yn = false);

### 문제 3. 연도별, 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발
> * **문제 3의 조건**
>   - 단, 취소여부가 'Y'인 거래는 취소된 거래임
> 
> 
> * **문제 3의 해결 방법**
>   1. 거래 내역에서 중복 제거된 연도 목록을 받아오는 기능을 구현했습니다.
>   2. 중복 제거된 연도를 기준으로 연도별 거래내역을 조회하는 기능을 구현했습니다.
>   3. 연도별 거래 내역에서 계좌 번호를 이용하여 계좌 정보를 조회하는 기능을 구현했습니다.
>   4. 전체 관리점 목록을 배열로 구성하여 각 관리점의 코드와 일치하는 계좌 정보의 관리점 코드를 찾는 기능을 구현했습니다.
>   5. 4번에서 찾은 계좌 정보에서 금액을 구해서 관리점의 총 거래 금액에 합산하여 관리점의 총 거래 금액을 구하였습니다.
>   6. 연도를 기준으로 각 관리점의 합산 거래 금액을 구하고 거래 금액을 내림차순으로, 연도를 오름차순으로 정렬하여 결과를 반환하였습니다.
> 
> 
> * **참고 SQL**
>   - 관리지점의 2018년도 총 금액 조회 SQL
>     > select  
    **sum(amount) as sumAmt**,  
    **sum(commission) as sumCommission**,  
    **b.branch_code**,  
    **b.branch_name**,  
    from **kpsec_tb_transaction_history** h  
    left join **kpsec_tb_account** a on a.account_no = h.account_no  
    left join **kpsec_tb_branch** b on b.branch_code = a.branch_code  
    where cancel_yn = false  
    and h.transaction_date like '2018%'  
    group by **b.branch_code**  
    order by **sumAmt desc**;        

### 문제 4. 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면 해당 지점의 거래금액 합계를 출력하는 API 개발
> * **문제 4의 조건**
>     - 단, 취소여부가 'Y'인 거래는 취소된 거래임
>     - 분당점 출력시 http status 404 출력
> 
> 
> * **문제 4의 해결 방법**
>     1. 관리지점 이름을 query parameter로 입력 받도록 구현하였습니다.
>     2. validation check하는 서비스(BranchValidationService)를 만들어 Controller에서 입력 받은 관리지점 명이 '분당점' 이면 Not Found Exception이 발생하도록 구현하였습니다.
>     3. 입력 받은 관리지점명을 이용하여 관리지점 정보를 조회하는 기능을 구현하였습니다.
>     4. 전체 거래 내역을 배열로 구성하여 계좌 번호를 가져오고 가져온 계좌 번호를 이용해서 계좌의 정보를 조회하는 기능을 구현했습니다.
>     5. 가져온 계좌 정보에서 관리지점 코드를 이용해서 입력된 관리점과 동일한 코드인지 비교하였습니다.
>     6. 입력된 관리지점과 맞는 거래내역이라면 거래 금액을 총 거래 금액에 합산하여 관리점의 총 거래 금액을 구하여 반환하였습니다.
> 
> 
> * **참고 SQL**
>     - 판교점의 총 거래 금액 조회 SQL  
>     > select  
        b.branch_code,  
        b.branch_name,  
        (select sum(amount) from kpsec_tb_transaction_history h where h.account_no = a.account_no and cancel_yn = false) as sumAmt  
        from kpsec_Tb_branch b  
        left join kpsec_tb_account a on a.branch_code = b.branch_code  
        where b.branch_name = '판교점';  