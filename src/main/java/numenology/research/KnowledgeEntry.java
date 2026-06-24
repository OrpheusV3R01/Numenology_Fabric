package numenology.research;

import java.util.List;

public class KnowledgeEntry {

    public final String id;
    public final List<String> requirements;

    public KnowledgeEntry(String id, List<String> requirements) {
        this.id = id;
        this.requirements = requirements;
    }
}