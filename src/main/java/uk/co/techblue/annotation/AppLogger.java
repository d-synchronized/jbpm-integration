package uk.co.techblue.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ METHOD, TYPE, FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogger {

    boolean logToFile() default false;

}
