### 미션 인증 프로젝트 📝 :
기존의 시스템을 개선해 체계적으로 관리가 이루어질 수 있는 프로젝트를 기획하였습니다.

- 누구나 본인들만의 미션을 생성하고 참여자들을 모집해 각자의 미션을 진행
- 미션에 참여하는 사용자는 인증 제출 요일에 반드시 인증 포스트를 작성
- 제출 요일에 포스트를 작성하지 않은 참여자는 자동으로 해당 미션에서 강퇴 처리

#### 기술 스택 🧰
 - Spring Boot(API SERVER)
 - Spring Security(Security)
 - OAuth2.0 + JWT(Authentication & Authorization)
 - Spring Batch(Batch)
 - MariaDB (RDB)
 - JPA & QueryDSL (ORM)
 - Redis (Cache)
 - AWS S3 (Object Storage)
 - Jenkins & GitLab (CI/CD) - 추후 적용 예정 😢
 - AWS EC2, Docker (Infra)

<details>
  <summary>ERD 모델링 🖼️ </summary>
 <img src="https://github.com/sungchulyun/Daily-mission/assets/97434717/3ccad2e0-b28b-4935-9c38-8dfbf9a597cd">
</details>
