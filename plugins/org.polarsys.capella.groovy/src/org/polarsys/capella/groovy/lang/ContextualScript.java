package org.polarsys.capella.groovy.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface ContextualScript {
  String name() default "";
  Class<?> applyOn() default Object.class;
  String section() default "additions";
}
