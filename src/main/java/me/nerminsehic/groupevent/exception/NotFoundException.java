package me.nerminsehic.groupevent.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> entityClass, Object id) {
        super("Cannot find %s with ID %s".formatted(entityClass.getSimpleName().toLowerCase(), id));
    }
}
