# ReadMe

# SKT Backend 사전과제 _ 통합 SNS Feed 서비스

---

## 시작하기

- 웹 URL (front) : [https://skt-test.netlify.app/](https://skt-test.netlify.app/)
- API URL (back) : `52.23.32.206` or `localhost:8080`
    - 만약 서버가 꺼져있거나 응답이 없는 경우 [localhost](http://localhost) 사용
- DB : AWS RDS mySQl 사용
- 서버 : AWS EC2 사용

### Test 계정 사용

Facebook Developers 권한 정책에 따라 앱이 검수되지 않은 상태에서는 발급받는 테스트 계정만 사용이 가능하다.
- ID : ffyammfipy_1651844834@tfbnw.net
- Password : Test1234!
---

## OAuth2.0 로그인

페이지는 `Facebook`, `Intagram` SNS의 통합 피드를 제공하며 OAuth2.0 로그인 이용. 
[https://skt-test.netlify.app/](https://skt-test.netlify.app/) 로 접속

- 로그인 순서 : Facebook Login → Instagram Login → Start
- 로그인 후 F12로 개발자 모드 진입, console에서 JWT 토근을 발급 받기 위한 json object 확인 가능 (`/api/login api` 에 그대로 복사 + 붙여넣기로 사용)

<img width="1440" alt="Screen Shot 2022-05-08 at 12 40 33 PM" src="https://user-images.githubusercontent.com/46706279/167280788-57b10f39-551b-4c98-ad41-924f3a2ce628.png">

---

## JWT Token 발급

소셜 로그인 후 자체 서비스 내의 유저 토큰

OAuth 로그인 후 발급 받은 `facebook access_token`과 `instagram code`를 이용해 `서버 access_token`을 발급한다. access_token은 client의 세션에 저장되어야 하며 서버에 모든 요청을 보낼 때 Request Header에 포함되어야 한다.

- API URL :
    - [`http://localhost:8080/api/login`](http://localhost:8080/api/login)
    - `http://52.23.32.206:8080/api/login`
- HTTP Method : `POST`
- Request Body
    - `{fb_name}` : facebook name
    - `{fb_id}` : facebook id
    - `{fb_token}` : facebook access token
    - `{ig_code}` : istagram code
    - example :
        
        ```json
        {
          "fb_name": "Daniel Alhjhedbdhgfi Riceman",
          "fb_id": "110097898362018",
          "fb_token": "EAAJgiWmJlj0BAAmezyPz1ptq1KzAn9MnZBxm6L9ZCJkkjoN2ueNROJasP2kXPsJoLsbGkBbArHuc8TResAgyOEopjSLbTZA2hFqSlyzgTypzwL6MaBPTuE1vdMTduTcD37DOWSZBpUeYY1rWGxxxgR7oC1KSd7XVhYfTZAiOLLVZAqaFpYZCQITbhuG44YvUxttTC2Jb3uDXPehx1SHGm08IdPVDGkvAgUZD",
          "ig_code": "AQCxfdaL3tiIS91JkddtnPQivfU7eiWBTg1GqUUgmwIDiX9TJW56gks6DOvROoaeZyrFHoRURirn7Ox_2YwZ6diiKryX5ZS5LjokYVs0QOw7LzZgxJzgXyNq2fgzvosMmSc1hI3jZgoJHihpD2w9k8jOw8PWmWsmP5bayljPFua-URRA_brhzYc1dWIw7ylQMLFMglIgegxNzwEpVqQY-1UPPv6aNeUpLBr7OeBu0s9x9g"
        }
        
        ```
        
    - 로그인 후 F12로 console에서 JWT 토근을 발급 받기 위한 json object 확인 가능 (`/api/login api` 에 그대로 복사 + 붙여넣기로 사용) (위 OAuth2.0 로그인 참조)
- Response
    - `{access_token}` : access_token, Client가 세션에 가지고있어야 하며, 일정 기간 동안 유효하고 시간이 지나면 만료된다.
    - `{refresh_token}` : access_token 만료 시 새로 유효한 access_token을 발급 받기 위한 token
    - examle:
    
    ```jsx
    {
        "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJFQUFKZ2lXbUpsajBCQUVrNmVIRzRKVm5GMUhjSlpBZnQ0SzRUYVcwOU9IWkNHVm9vSVRodEh2WkFZaUlwWkNEVkNON3VyOW04S1FYOVpDNGluODVFUlVaQTRYRk5PQ081eXhGS0xoa0Z0SUVtWkFIZEp6QUJvM0ExM2FnZTA5WkJ0ZEgxN1ZiZXhPZTN5WkJjYW5vRHB5VDBzYkJUWkJJWkFOY3hCZnlFNUU4U1ZDRDhDTXQ1RDcxc3hQWkNJWkFBamI0SzZTUFpCM3ZYV201S2dhNXRJRnpnYmJYRVVpMFRaQWJra291OXMwWkRJR1FWSlZiSE42U2tFNGNVTkRiR3h2WVRsdFZVdGpPVTVYY25WV2EyRk9VMDFQUlhCS1IzTllYMUpGTkhoeVpBVXRsZDFSemFuWkFSWWxwcFYxVlVhVUZhWkFUbFdRV2wwVW1VNVIwZzJaQWpkamNrVkxVSFZIWVhOS1NqRkNZVm90VDNZMFFsaDBOVjlYYW5odVh6WkFsUVVKbVdrOUhkbTB3VkVGT1ZqWTQiLCJleHAiOjE2NTIwMzk1MTd9.Pyko2Dn9ZS0PQC0tuebwiYXz5UzX1xUAeU9HJgXdhws",
        "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJFQUFKZ2lXbUpsajBCQUVrNmVIRzRKVm5GMUhjSlpBZnQ0SzRUYVcwOU9IWkNHVm9vSVRodEh2WkFZaUlwWkNEVkNON3VyOW04S1FYOVpDNGluODVFUlVaQTRYRk5PQ081eXhGS0xoa0Z0SUVtWkFIZEp6QUJvM0ExM2FnZTA5WkJ0ZEgxN1ZiZXhPZTN5WkJjYW5vRHB5VDBzYkJUWkJJWkFOY3hCZnlFNUU4U1ZDRDhDTXQ1RDcxc3hQWkNJWkFBamI0SzZTUFpCM3ZYV201S2dhNXRJRnpnYmJYRVVpMFRaQWJra291OXMwWkRJR1FWSlZiSE42U2tFNGNVTkRiR3h2WVRsdFZVdGpPVTVYY25WV2EyRk9VMDFQUlhCS1IzTllYMUpGTkhoeVpBVXRsZDFSemFuWkFSWWxwcFYxVlVhVUZhWkFUbFdRV2wwVW1VNVIwZzJaQWpkamNrVkxVSFZIWVhOS1NqRkNZVm90VDNZMFFsaDBOVjlYYW5odVh6WkFsUVVKbVdrOUhkbTB3VkVGT1ZqWTQiLCJleHAiOjE2NTIwNDA1MTd9.tDoSbmXghfRYkPnmE6Bv0UUwLzNPnoaUYAd_zZe0HJo"
    }
    ```
    
    
---

## 최신 피드 조회

사용자의 최신 피드를 통합하여 조회한다.

내용, 이미지, 작성 시간, 글쓴이 정보가 포함되며 최근 시간 순서대로 조회된다.

- HTTP URL
    - [`http://localhost:8080/api/feed`](http://localhost:8080/api/feed)
    - `http://52.23.32.206:8080/api/feed`
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Response
    - JSON Array 형태로 반환된다
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed_list": [
            {
                "post_id": "17926840211148143",
                "sns_type": "instagram",
                "post_message": "SKT Backend Test",
                "post_create_time": "2022-05-08 12:31:00.0",
                "post_creator": "ffyammfipy_1651844834",
                "post_image": "https://scontent-gmp1-1.cdninstagram.com/v/t51.29350-15/280177214_730007924840505_5704215464912488579_n.webp?stp=dst-jpg&_nc_cat=110&ccb=1-6&_nc_sid=8ae9d6&_nc_ohc=WOEPQgXTT5EAX9F8vK_&_nc_ht=scontent-gmp1-1.cdninstagram.com&edm=ANo9K5cEAAAA&oh=00_AT9bf6T1NL3Za75eRRwUCOnCbv_H9QSr8lemlU-4GvERDQ&oe=627B988E"
            },
            {
                "post_id": "110097898362018_111255711579570",
                "sns_type": "facebook",
                "post_message": "SKT 가고십슨디아..",
                "post_create_time": "2022-05-08 06:44:04.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": null
            },...
    ```

---

## 피드의 글 세부정보 조회

특정 피드의 세부 정보를 조회할 수 있다.

- HTTP URL
    - `http://localhost:8080/apigetFeed/{post_id}`
    - `http://52.23.32.206:8080/api/getFeed/{post_id}`
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Params
    - `{post_id}` : 조회할 Feed의 ID
- Response
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed_list": {
            "post_id": "110097898362018_110096518362156",
            "sns_type": "facebook",
            "post_message": "test1",
            "post_create_time": "2022-05-06 22:48:02.0",
            "post_creator": "Daniel Alhjhedbdhgfi Riceman",
            "post_image": null
        }
    }
    ```
    

---

## 피드의 기간별 조회 _ 특정 날짜로 조회

`정확한 날짜` ex) 2022.01.01 ~ 2022.02.02로 통합 피드를 조회할 수 있다.

- HTTP URL
    - `http://localhost:8080/api/search?start={start}&end={end}`
    - `http://52.23.32.206:8080/api/search?start={start}&end={end}`
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Params
    - `{start}` : 조회를 시작할 날짜, YYYY-MM-DD 형식, ex) 2022-05-05
    - `{end}` : 조회를 마칠 날짜 , YYYY-MM-DD 형식, ex) 2022-05-08
- Response
    - JSON Array 형태로 반환된다
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed_list": [
            {
                "post_id": "17926840211148143",
                "sns_type": "instagram",
                "post_message": "SKT Backend Test",
                "post_create_time": "2022-05-08 12:31:00.0",
                "post_creator": "ffyammfipy_1651844834",
                "post_image": "https://scontent-gmp1-1.cdninstagram.com/v/t51.29350-15/280177214_730007924840505_5704215464912488579_n.webp?stp=dst-jpg&_nc_cat=110&ccb=1-6&_nc_sid=8ae9d6&_nc_ohc=WOEPQgXTT5EAX9F8vK_&_nc_ht=scontent-gmp1-1.cdninstagram.com&edm=ANo9K5cEAAAA&oh=00_AT9bf6T1NL3Za75eRRwUCOnCbv_H9QSr8lemlU-4GvERDQ&oe=627B988E"
            },
            {
                "post_id": "110097898362018_111255711579570",
                "sns_type": "facebook",
                "post_message": "SKT 가고십슨디아..",
                "post_create_time": "2022-05-08 06:44:04.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": null
            },...
    ```
    

---

## 피드의 기간별 조회 _ 특정 기간으로 조회

`최근 24시간`, `최근 1주일`, `최근 1달 단위`로 조회할 수 있다.

- HTTP URL
    - `http://localhost:8080/api/forDay`
    - `http://localhost:8080/api/forWeek`
    - `http://localhost:8080/api/forMonth`
    - [http://52.23.32.206:8080] + `/forDay` or `/forWeek` or `/forMonth`
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Response
    - JSON Array 형태로 반환된다
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed_list": [
            {
                "post_id": "17926840211148143",
                "sns_type": "instagram",
                "post_message": "SKT Backend Test",
                "post_create_time": "2022-05-08 12:31:00.0",
                "post_creator": "ffyammfipy_1651844834",
                "post_image": "https://scontent-gmp1-1.cdninstagram.com/v/t51.29350-15/280177214_730007924840505_5704215464912488579_n.webp?stp=dst-jpg&_nc_cat=110&ccb=1-6&_nc_sid=8ae9d6&_nc_ohc=WOEPQgXTT5EAX9F8vK_&_nc_ht=scontent-gmp1-1.cdninstagram.com&edm=ANo9K5cEAAAA&oh=00_AT9bf6T1NL3Za75eRRwUCOnCbv_H9QSr8lemlU-4GvERDQ&oe=627B988E"
            },
            {
                "post_id": "110097898362018_111255711579570",
                "sns_type": "facebook",
                "post_message": "SKT 가고십슨디아..",
                "post_create_time": "2022-05-08 06:44:04.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": null
            },...
    ```
    

---

## 최신 정보 강제 풀링

사용자가 최근 게재한 피드를 즉시 가져온다. 주기적으로 스케쥴링 되는 API와 독립적으로 진행된다.

요청이 완료된 후에는 최신 피드를 통합하여 Return한다.

- HTTP URL
    - `http://localhost:8080/api/updateNow`
    - `http://52.23.32.206:8080/api/updateNow`
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Response
    - JSON Array 형태로 반환된다
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed_list": [
            {
                "post_id": "17926840211148143",
                "sns_type": "instagram",
                "post_message": "SKT Backend Test",
                "post_create_time": "2022-05-08 12:31:00.0",
                "post_creator": "ffyammfipy_1651844834",
                "post_image": "https://scontent-gmp1-1.cdninstagram.com/v/t51.29350-15/280177214_730007924840505_5704215464912488579_n.webp?stp=dst-jpg&_nc_cat=110&ccb=1-6&_nc_sid=8ae9d6&_nc_ohc=WOEPQgXTT5EAX9F8vK_&_nc_ht=scontent-gmp1-1.cdninstagram.com&edm=ANo9K5cEAAAA&oh=00_AT9bf6T1NL3Za75eRRwUCOnCbv_H9QSr8lemlU-4GvERDQ&oe=627B988E"
            },
            {
                "post_id": "110097898362018_111255711579570",
                "sns_type": "facebook",
                "post_message": "SKT 가고십슨디아..",
                "post_create_time": "2022-05-08 06:44:04.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": null
            },...
    ```
    

---

## 피드 정보의 주기적 풀링

사용자의 피드 정보를 주기적으로 풀링한다. 풀링 주기는 `1min` 이다.

사용자가 서버에 JWT 토큰을 요청한 순간 부터 시작되며, `1분 간격으로 60번의 주기적 풀링`을 실행한다.

(Instagram의 access_token이 1시간의 만료시간을 가기 때문)

`Spring Scheduler`에 의해 실행되며, 해당 기능과 관련해서는 client에서는 별도로 요청할 사항이 없다.

---


## 검색어로 피드 찾기

사용자의 통합된 피드 중 `검색어가 포함된` 피드를 반환한다.

- HTTP URL
    - http://localhost:8080/api/searchKeyword/{keyword}
    - http://52.23.32.206:8080/api/searchKeyword/{keyword}
- HTTP Method : `GET`
- Request Header
    - `{Authorization}` : access_token
        - /api/login에서 발급받은 token을 의미한다.
- Params
    - `{keyword}` : 검색어
- Response
    
    [검색어 결과가 존재할 경우]
    
    - JSON Array 형태로 반환된다
    - `{post_id}` : 피드의 고유 ID를 의미한다. 이후 피드 상세 조회 api에 사용된다.
    - `{sns_type}` : 피드가 어떤 sns에 게재 되었는지를 의미한다. (facebook or instagram)
    - `{post_message}` : 피드의 내용
    - `{post_create_time}`  : 피드가 작성된 시간을 말한다.
    - `{post_creator}` : 피드를 작성한 사용자를 의미한다.
    - `{post_image}` : 피드에 image가 포함되어 있는 경우에만 반환되며 , image url이 반환된다.
    - example
    
    ```jsx
    {
        "feed": [
            {
                "post_id": "110097898362018_110835964954878",
                "sns_type": "facebook",
                "post_message": "Skt 보내주세요!!",
                "post_create_time": "2022-05-07 19:57:03.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": null
            },
            {
                "post_id": "110097898362018_110160465022428",
                "sns_type": "facebook",
                "post_message": "Skt가고싶다",
                "post_create_time": "2022-05-07 00:20:04.0",
                "post_creator": "Daniel Alhjhedbdhgfi Riceman",
                "post_image": "https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/280141274_110160425022432_8478173749644863242_n.jpg?stp=dst-jpg_s720x720&_nc_cat=108&ccb=1-6&_nc_sid=110474&_nc_ohc=LoIXVkACVvQAX8wS0Cf&_nc_ht=scontent-gmp1-1.xx&edm=ADqbNqUEAAAA&oh=00_AT_HsMbFAAIpLlNfFwrxFtLwK1Wit9N4ebMnxInMxaxN-g&oe=627C8052"
            }
        ]
    }
    ```
    
    [검색어 결과가 존재하지 않을 경우]
    
    ```jsx
     {
        "feed": "no match"
    }
    ```
