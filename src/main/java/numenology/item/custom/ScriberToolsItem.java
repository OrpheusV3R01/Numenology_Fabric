package numenology.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import numenology.item.ModItems;

public class ScriberToolsItem extends Item {

    private static final int MAX_USES = 2;

    public ScriberToolsItem(Settings settings) {
        super(settings);
    }

    // 🔥 Получить текущие использования
    public static int getUses(ItemStack stack) {
        if (!stack.hasNbt()) return 0;
        return stack.getNbt().getInt("Uses");
    }

    // 🔥 Установить использования
    public static void setUses(ItemStack stack, int uses) {
        stack.getOrCreateNbt().putInt("Uses", uses);
    }

    // 🔥 Добавить использование
    public static void use(ItemStack stack) {
        int uses = getUses(stack) + 1;
        setUses(stack, uses);
    }

    // 🔥 Проверка — можно ли использовать
    public static boolean canUse(ItemStack stack) {
        return getUses(stack) < MAX_USES;
    }

    // 🔥 Проверка — пустой ли инструмент
    public static boolean isEmpty(ItemStack stack) {
        return getUses(stack) >= MAX_USES;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockState(pos).isOf(Blocks.BOOKSHELF)) {

            // 🔒 серверная проверка
            if (world.isClient) return ActionResult.SUCCESS;

            // 🔮 Магический звук
            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
            );

            // ✨ Частицы (только на сервере!)
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        ParticleTypes.ENCHANT,
                        pos.getX() + 0.5,
                        pos.getY() + 0.8,
                        pos.getZ() + 0.5,
                        40,          // количество
                        0.4, 0.4, 0.4, // разброс
                        0.1           // скорость
                );
            }

            // 📚 Удаляем книжную полку (без дропа)
            world.setBlockState(pos, Blocks.AIR.getDefaultState());

            // 📖 Создаём Codex в мире
            ItemStack codex = new ItemStack(ModItems.NUMEN_CODEX);

            ItemEntity entity = new ItemEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    codex
            );

            // ✨ Лёгкий "магический" выброс вверх
            entity.setVelocity(
                    world.random.nextGaussian() * 0.05,
                    0.25,
                    world.random.nextGaussian() * 0.05
            );

            world.spawnEntity(entity);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

}