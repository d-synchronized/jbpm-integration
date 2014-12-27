package uk.co.techblue.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Target({ METHOD, PARAMETER, FIELD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    boolean auditToFile() default false;

}
