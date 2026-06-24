package numenology.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import numenology.block.ModBlocks;
import numenology.block.custom.TurquoiseBushBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GardenBagItem extends Item {
    public GardenBagItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getStack();
        NbtCompound nbt = stack.getOrCreateNbt();

        // 1. Забираем куст[cite: 12]
        if (state.isOf(ModBlocks.TURQUOISE_BUSH) && !nbt.getBoolean("hasBush")) {
            if (!world.isClient) {
                // Сохраняем стадию роста куста![cite: 11]
                int age = state.get(TurquoiseBushBlock.AGE);
                nbt.putInt("bushAge", age);
                nbt.putBoolean("hasBush", true);

                world.removeBlock(pos, false);
                world.playSound(null, pos, net.minecraft.sound.SoundEvents.ITEM_BUNDLE_INSERT, net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            return ActionResult.success(world.isClient);
        }

        // 2. Высаживаем куст[cite: 12]
        if (nbt.getBoolean("hasBush")) {
            BlockPos placePos = pos.offset(context.getSide());
            if (world.getBlockState(placePos).isAir()) {
                if (!world.isClient) {
                    // Восстанавливаем сохраненную стадию роста[cite: 12]
                    int savedAge = nbt.getInt("bushAge");
                    world.setBlockState(placePos, ModBlocks.TURQUOISE_BUSH.getDefaultState().with(TurquoiseBushBlock.AGE, savedAge));

                    world.playSound(null, placePos, net.minecraft.sound.SoundEvents.BLOCK_GRASS_PLACE, net.minecraft.sound.SoundCategory.BLOCKS, 1.0f, 1.0f);

                    if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                        nbt.remove("hasBush");
                        nbt.remove("bushAge");
                    }
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && stack.getNbt().getBoolean("hasBush")) {
            tooltip.add(Text.translatable("item.numenology.garden_bag.full").formatted(Formatting.AQUA));
        } else {
            tooltip.add(Text.translatable("item.numenology.garden_bag.empty").formatted(Formatting.GRAY));
        }
    }
}