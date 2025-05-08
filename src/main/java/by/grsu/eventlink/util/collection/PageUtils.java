package by.grsu.eventlink.util.collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    public static Pageable getPage(Integer page, Integer size) {
        return PageRequest.of(page, size);
    }

    public static Pageable getPage(Integer page, Integer size, Sort sort) {
        return PageRequest.of(page, size).withSort(sort);
    }

    public static Page<?> emptyIfNull(Page<?> page) {
        return page == null ? Page.empty() : page;
    }

}
