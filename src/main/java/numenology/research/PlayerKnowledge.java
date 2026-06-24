package numenology.research;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerKnowledge {

    private final Map<String, Long> suspected = new HashMap<>();
    private final Set<String> completed = new HashSet<>();
    private final Map<String, Long> cooldowns = new HashMap<>();

    // =========================
    // SUSPECTED
    // =========================

    public void addSuspected(String id, long expireTime) {
        suspected.put(id, expireTime);
    }

    public boolean hasSuspected(String id) {
        return suspected.containsKey(id);
    }

    public void removeSuspected(String id) {
        suspected.remove(id);
    }

    public Map<String, Long> getAllSuspected() {
        return suspected;
    }

    // 🔥 ВАЖНО (для sync)
    public void copyFrom(PlayerKnowledge other) {
        this.completed.clear();
        this.completed.addAll(other.completed);

        this.suspected.clear();
        this.suspected.putAll(other.suspected);
    }

    // =========================
    // COMPLETED
    // =========================

    public void addCompleted(String id) {
        completed.add(id);
    }

    public boolean hasCompleted(String id) {
        return completed.contains(id);
    }

    public Set<String> getCompleted() {
        return completed;
    }

    // =========================
    // REQUIREMENTS
    // =========================

    public boolean canComplete(String id) {

        var entry = KnowledgeRegistry.get(id);

        if (entry == null) return true;
        if (entry.requirements == null || entry.requirements.isEmpty()) return true;

        for (String req : entry.requirements) {
            if (!hasCompleted(req)) return false;
        }

        return true;
    }

    // =========================
    // COOLDOWN
    // =========================

    public void setCooldown(String id, long time) {
        cooldowns.put(id, time);
    }

    public boolean isOnCooldown(String id, long currentTime) {
        return cooldowns.containsKey(id) && cooldowns.get(id) > currentTime;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        NbtCompound suspectedTag = new NbtCompound();
        for (var entry : suspected.entrySet()) {
            suspectedTag.putLong(entry.getKey(), entry.getValue());
        }

        NbtCompound cooldownTag = new NbtCompound();
        for (var entry : cooldowns.entrySet()) {
            cooldownTag.putLong(entry.getKey(), entry.getValue());
        }

        NbtList completedList = new NbtList();
        for (String id : completed) {
            completedList.add(NbtString.of(id));
        }

        nbt.put("Suspected", suspectedTag);
        nbt.put("Cooldowns", cooldownTag);
        nbt.put("Completed", completedList);

        return nbt;
    }

    public static PlayerKnowledge fromNbt(NbtCompound nbt) {
        PlayerKnowledge data = new PlayerKnowledge();

        if (nbt.contains("Suspected")) {
            NbtCompound suspectedTag = nbt.getCompound("Suspected");
            for (String key : suspectedTag.getKeys()) {
                data.suspected.put(key, suspectedTag.getLong(key));
            }
        }

        if (nbt.contains("Cooldowns")) {
            NbtCompound cooldownTag = nbt.getCompound("Cooldowns");
            for (String key : cooldownTag.getKeys()) {
                data.cooldowns.put(key, cooldownTag.getLong(key));
            }
        }

        if (nbt.contains("Completed")) {
            NbtList list = nbt.getList("Completed", 8); // 8 = string
            for (int i = 0; i < list.size(); i++) {
                data.completed.add(list.getString(i));
            }
        }

        return data;
    }

    // =========================
    // SAVE / LOAD
    // =========================

    public void writeNbt(NbtCompound nbt) {

        // completed
        var completedList = new net.minecraft.nbt.NbtList();
        for (String id : completed) {
            completedList.add(net.minecraft.nbt.NbtString.of(id));
        }
        nbt.put("Completed", completedList);

        // suspected
        var suspectedTag = new net.minecraft.nbt.NbtCompound();
        for (var entry : suspected.entrySet()) {
            suspectedTag.putLong(entry.getKey(), entry.getValue());
        }
        nbt.put("Suspected", suspectedTag);

        // cooldowns
        var cooldownTag = new net.minecraft.nbt.NbtCompound();
        for (var entry : cooldowns.entrySet()) {
            cooldownTag.putLong(entry.getKey(), entry.getValue());
        }
        nbt.put("Cooldowns", cooldownTag);
    }

    public void readNbt(NbtCompound nbt) {

        completed.clear();
        suspected.clear();
        cooldowns.clear();

        // ✅ ПРОВЕРКА НАЛИЧИЯ

        if (nbt.contains("Completed")) {
            var list = nbt.getList("Completed", net.minecraft.nbt.NbtElement.STRING_TYPE);
            for (int i = 0; i < list.size(); i++) {
                completed.add(list.getString(i));
            }
        }

        if (nbt.contains("Suspected")) {
            var tag = nbt.getCompound("Suspected");
            for (String key : tag.getKeys()) {
                suspected.put(key, tag.getLong(key));
            }
        }

        if (nbt.contains("Cooldowns")) {
            var tag = nbt.getCompound("Cooldowns");
            for (String key : tag.getKeys()) {
                cooldowns.put(key, tag.getLong(key));
            }
        }
    }
    }