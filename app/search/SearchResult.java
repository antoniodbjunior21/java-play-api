package search;

import java.util.List;

public class SearchResult<T> {
    public List<T> data;
    public Long pages;
    public Long count;
    public Integer size;

    public SearchResult(List<T> data, Long count, Integer size) {
        this.data = data;
        this.count = count;
        this.size = size;
        this.pages = this.count / this.size;
        if ((this.count % this.size) > 0) {
            this.pages++;
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }
}
