package by.grsu.eventlink.util.collection;

import by.grsu.eventlink.exception.common.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Collections {

    private static final String NOTHING_FOUND_MESSAGE = "Nothing was found by your query";

    public static void ifEmptyThenThrow(Collection<?> collection1, Collection<?> collection2) {
        if (io.jsonwebtoken.lang.Collections.isEmpty(collection1) &&
                io.jsonwebtoken.lang.Collections.isEmpty(collection2))
            throw new NotFoundException(NOTHING_FOUND_MESSAGE);
    }

    public static void ifEmptyThenThrow(Collection<?> collection) {
        if (io.jsonwebtoken.lang.Collections.isEmpty(collection))
            throw new NotFoundException(NOTHING_FOUND_MESSAGE);
    }

    public static void ifEmptyThenThrow(Page<?> page) {
        if (Objects.isNull(page) || page.isEmpty()) {
            throw new NotFoundException(NOTHING_FOUND_MESSAGE);
        }
    }

    public static <T> List<T> addAndGet(List<T> collection, T object) {
        collection.add(object);

        return collection;
    }

    public static <T> List<T> removeAndGet(List<T> collection, T object) {
        collection.remove(object);

        return collection;
    }

    public static boolean isNotEmpty(List<?> collection) {
        return !collection.isEmpty();
    }

}
