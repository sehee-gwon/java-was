# Java WAS 만들기
#### 1. HTTP/1.1의 Host 헤더 해석
```
1. host 파일에 등록
> 127.0.0.1   www.a.com
> 127.0.0.1   www.b.com
2. port 8080 호출
> http://www.a.com:8080
> http://www.b.com:8080
```
#### 2. 설정 파일 관리
```
1. 설정 경로: /src/main/resources/application.json
2. JSON 포맷, File(Json) -> String -> Object
3. 포트, Host별 디렉터리, 오류일 때 출력할 HTML 파일 이름 설정
```
#### 3. 403, 404, 500 오류 처리
```
1. Exception 을 이용한 분기 처리
> RequestHandler, HttpValidator
2. 설정 파일에 적은 파일 이름 이용
```
#### 4. 보안 규칙 작업 (403 반환)
```
1. HTTP_ROOT 디렉터리의 상위 디렉터리에 접근할 때 403 반환
2. 확장자가 exe인 파일을 요청받았을 때 403 반환
3. 추후 규칙을 추가할 것을 고려하여 로직 분리
> HttpValidator -> validate403
```
#### 5. logback 프레임워크를 이용한 로깅 작업
```
1. 설정 경로: /src/main/resources/logback.xml
2. 로그 저장 경로: /logs
3. 로그 레벨별 StackTrace 일별로 파일 저장
4. 오류 발생 시 StackTrace 전체를 로그 파일에 남김
```
#### 6. 간단한 WAS 구현
```
1. URL을 SimpleServlet 구현체로 매핑
> simple.service.Hello, simple.Hello 
2. 설정 파일을 이용한 매핑 구현
> application.json -> mappings array
> /Greeting, /super.Greeting
```
#### 7. 현재 시각을 출력하는 SimpleServlet 구현체 작성
```
앞서 구현한 WAS 이용하여 구현
> simple.Time
```
#### 8. JUnit4로 여러 스펙을 검증하는 테스트 케이스 작성
```
미구현
```