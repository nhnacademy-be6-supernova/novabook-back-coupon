# novabook-back-coupon
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/H2-007396?style=flat-square&logo=H2&logoColor=white"/>
  <img src="https://img.shields.io/badge/REST-000000?style=flat-square&logo=REST&logoColor=white"/>
</p>

`novabook-back-coupon`은 쿠폰 등록 및 발급 기능을 제공하는 REST API 서버입니다. 다양한 유형의 쿠폰을 관리하고, 사용자가 쿠폰을 발급받을 수 있는 기능을 포함하고 있습니다.


## 요구 사항
- Java 21
- Maven 4.0.0


## 쿠폰 유형
`CouponType` 열거형은 다음과 같은 다양한 쿠폰 유형을 나타냅니다.

- GENERAL: 일반 쿠폰
- BIRTHDAY: 생일 쿠폰
- WELCOME: 환영 쿠폰
- BOOK: 도서 쿠폰
- CATEGORY: 카테고리 쿠폰
- LIMITED: 선착순 쿠폰

