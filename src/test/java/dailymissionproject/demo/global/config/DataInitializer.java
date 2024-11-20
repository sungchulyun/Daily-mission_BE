package dailymissionproject.demo.global.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@Component
@Profile("integration-test")
public class DataInitializer {

    private static final String OFF_FOREIGN_CONSTRAINTS = "SET foreign_key_checks = false";
    private static final String ON_FOREIGN_CONSTRAINTS = "SET foreign_key_checks = true";
    private static final String TRUNCATE_SQL_FORMAT = "TRUNCATE %s";

    private static final List<String> truncationDMLs = new ArrayList<>();
    @PersistenceContext
    private EntityManager em;

    protected DataInitializer() {

    }

    @Transactional(value = REQUIRES_NEW)
    public void deleteAll(){
        if(truncationDMLs.isEmpty()){
            init();
        }
        em.createNativeQuery(OFF_FOREIGN_CONSTRAINTS).executeUpdate();
        truncationDMLs.stream()
                .filter(Predicate.not(name -> name.startsWith("B")))
                .map(em::createNativeQuery)
                .forEach(Query::executeUpdate);
        em.createNativeQuery(ON_FOREIGN_CONSTRAINTS).executeUpdate();
    }

    private void init() {
        final List<String> tableNames = em.createNativeQuery("SHOW TABLES ").getResultList();

        tableNames.stream()
                .filter(Predicate.not(name -> name.startsWith("B")))
                .map(tableName -> String.format(TRUNCATE_SQL_FORMAT, tableName))
                .forEach(truncationDMLs::add);
    }
}
