package numenology.codex;

import java.util.List;

public class CodexEntry {

    private final String id;
    private final String categoryId;
    private final List<String> pages;

    public CodexEntry(String id, String categoryId, List<String> pages) {
        this.id = id;
        this.categoryId = categoryId;
        this.pages = pages;
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public List<String> getPages() {
        return pages;
    }
}