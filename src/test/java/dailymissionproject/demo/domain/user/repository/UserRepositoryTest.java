package dailymissionproject.demo.domain.user.repository;

import dailymissionproject.demo.common.config.JPAConfig;
import dailymissionproject.demo.common.config.QueryDSLConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @DataJpaTest 어노테이션을 통해서 Repository에 대한 Bean만 등록한다.
 * @DataJpaTest는 기본적으로 메모리 데이터베이스에 대한 테스트를 수행한다.
 * JpaRepository에서 기본적으로 제공하는 findById, findByAll 등은 테스트 하지 않는다.
 * 주로 커스텀하게 작성된 쿼리 메서드, QueryDSL 등의 커스텀하게 작성된 메서드를 테스트한다.
 * 실제로 작성된 쿼리가 어떻게 동작하는지 show-SQL 옵션을 통해서 확인한다.
 */

@Tag(value = "data-jpa-test")
@DisplayName("[Database] [repository] JpaUserRepository")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JPAConfig.class}
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    EntityManager em;

    @Autowired
    private UserRepository userRepository;


}
