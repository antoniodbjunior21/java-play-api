package search;

import java.lang.annotation.ElementType;

@java.lang.annotation.Target(value={ElementType.TYPE, ElementType.FIELD})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SearchField {
    String field();
}
