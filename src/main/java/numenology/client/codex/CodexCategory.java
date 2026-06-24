package numenology.client.codex;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.util.List;

public class CodexCategory {
    private final String id;
    private final String title_key;
    private final String home_page_key;
    private final String icon_item;
    private final List<String> research_ids;

    public CodexCategory(String id, String titleKey, String homePageKey, String iconItem, List<String> researchIds) {
        this.id = id;
        this.title_key = titleKey;
        this.home_page_key = homePageKey;
        this.icon_item = iconItem;
        this.research_ids = researchIds;
    }

    public String getId() { return id; }
    public String getTitleKey() { return title_key; }
    public String getHomePageKey() { return home_page_key; }
    public List<String> getResearchIds() { return research_ids; }

    public ItemStack getIconStack() {
        try {
            var item = Registries.ITEM.get(new Identifier(icon_item));
            return new ItemStack(item);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
}