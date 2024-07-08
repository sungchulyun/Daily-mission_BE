# Daily-mission
### 미션 인증 프로젝트 :smiley:
 :+1: 기존의 시스템을 개선해 체계적으로 관리가 이루어질 수 있는 프로젝트를 기획하였습니다.

- 누구나 본인들만의 미션을 생성하고 참여자들을 모집해 각자의 미션을 진행
- 미션에 참여하는 사용자는 인증 제출 요일에 반드시 인증 포스트를 작성
- 제출 요일에 포스트를 작성하지 않은 참여자는 자동으로 해당 미션에서 강퇴 처리

#### 기능요구사항 ☀️
🏠 홈
 - 참여자가 많은 미션, 신규 생성된 미션을 조회 기능
  - 전체 미션(종료된 미션 포함)조회 기능
  - 미션 디테일 정보 확인 (현재 참여중인 사용자, 해당 미션에 제출된 포스트 목록 조회 가능)

🙋 미션 참여
- 미션 생성 후 전달받은 참여코드 입력을 통해 미션에 참가

📮 인증 포스팅 목록
- 전체 인증 포스트 목록 조회 기능


⭐ 내 미션 목록
- 내가 참여중인 미션 목록과 제출한 포스트 목록 조회 가능 - 강퇴 미션 재입장 불가
- 참여중인 미션의 weekly 포스트 history 조회 가능
- 제목/내용/사진을 입력해 인증 포스트 작성

#### 기술 스택 🧰
 - Spring Boot(API SERVER)
 - Spring Security(Security)
 - Spring Batch(Batch) : 😢 추후 적용 예정
 - MariaDB (RDB)
 - JPA & QueryDSL (ORM)
 - OAuth2.0 + JWT(Authentication & Authorization)
 - Redis (Cache)
 - Jenkins & GitLab (CI/CD)
 - AWS (Infra)
 - Nginx, Tomcat (WEB Server, WAS)

<details>
  <summary>ERD 🖼️ </summary>
 <img src="https://github.com/sungchulyun/Daily-mission/assets/97434717/3ccad2e0-b28b-4935-9c38-8dfbf9a597cd">
</details>
