package numenology.codex;

public class CodexCategory {

    private final String id;
    private final String title;

    public CodexCategory(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}