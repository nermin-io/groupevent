package me.nerminsehic.groupevent.exception;

public class UniqueConstraintException extends RuntimeException {

    public UniqueConstraintException(Class<?> entityClass, String field, Object value) {
        super("A %s with %s %s already exists".formatted(
                entityClass.getSimpleName().toLowerCase(),
                field,
                value
        ));
    }
}
