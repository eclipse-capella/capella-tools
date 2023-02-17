package org.polarsys.capella.groovy.lang;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface SemanticQuery {
  String name();
  SemanticSection section() default SemanticSection.CURRENT;
}
