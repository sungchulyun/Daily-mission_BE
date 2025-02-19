package dailymissionproject.demo.global;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.METHOD})
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactoryImpl.class)
public @interface WithMockCustomUser{

    String username() default "google 106088487779653945150";
    String role() default "USER";
}
