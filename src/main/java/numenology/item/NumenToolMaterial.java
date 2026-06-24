package numenology.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum NumenToolMaterial implements ToolMaterial {
    NUMEN_STEEL(
            3,       // Mining level (4 — уровень Незерита)
            900,    // Durability (Прочность, сколько ударов выдержит)
            7.0f,    // Mining speed (Скорость копания блоков)
            2.5f,    // Attack damage base (Базовый урон материала, к нему прибавится урон меча)
            12,      // Enchantability (Насколько хорошо чаруется)
            () -> Ingredient.ofItems(ModItems.NUMEN_STEEL_INGOT) // Чем чинится на наковальне
    );

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    NumenToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getDurability() { return this.itemDurability; }
    @Override public float getMiningSpeedMultiplier() { return this.miningSpeed; }
    @Override public float getAttackDamage() { return this.attackDamage; }
    @Override public int getMiningLevel() { return this.miningLevel; }
    @Override public int getEnchantability() { return this.enchantability; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
}