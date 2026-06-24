package numenology.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import java.util.function.Supplier;

public enum NumenArmorMaterial implements ArmorMaterial {
    NUMEN_STEEL(
            "numen_steel",
            24, // Множитель прочности
            new int[]{3, 6, 8, 3}, // Защита: [Шлем, Нагрудник, Поножи, Ботинки] (взяли 3 для шлема)
            10, // Чародейство
            SoundEvents.ITEM_ARMOR_EQUIP_IRON, // Звук при надевании
            1.0f, // Твердость (Toughness)
            0.0f, // Стойкость к отбрасыванию (Knockback Resistance)
            () -> Ingredient.ofItems(ModItems.NUMEN_STEEL_INGOT) // Чем чинится
    ),

    LAMELLAR(
            "lamellar",
                    12, // Множитель прочности (у кожи: 5, у железа: 15)
                    new int[]{2, 4, 5, 2}, // Защита: [Шлем, Нагрудник, Поножи, Ботинки] (у кожи: 1-2-3-1, у железа: 2-5-6-2)
            11, // Чародейство (у кожи: 15, у железа: 9)
    SoundEvents.ITEM_ARMOR_EQUIP_IRON, // Звук надевания (так как пластины металлические)
            0.0f, // Твердость (Toughness) — у железа и кожи её нет
            0.0f, // Стойкость к отбрасыванию
            () -> Ingredient.ofItems(ModItems.LAMELLAR_PLATE) // Предмет для починки
            );

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    // Массив базовой прочности слотов ванилы: [Шлем, Нагрудник, Поножи, Ботинки]
    private static final int[] BASE_DURABILITY = {11, 16, 15, 13};

    NumenArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts[type.ordinal()];
    }   @Override public int getEnchantability() { return this.enchantability; }
    @Override public SoundEvent getEquipSound() { return this.equipSound; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    @Override public String getName() { return this.name; }
    @Override public float getToughness() { return this.toughness; }
    @Override public float getKnockbackResistance() { return this.knockbackResistance; }
}