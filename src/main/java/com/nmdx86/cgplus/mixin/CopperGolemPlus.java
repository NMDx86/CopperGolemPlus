package com.nmdx86.cgplus.mixin;

import com.nmdx86.cgplus.CopperGolemPlusMod;
import com.nmdx86.cgplus.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.CopperGolemBrain;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(CopperGolemBrain.class)
public class CopperGolemPlus {

    @ModifyArg(
            method = "addIdleActivities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/task/MoveItemsTask;<init>(FLjava/util/function/Predicate;Ljava/util/function/Predicate;IILjava/util/Map;Ljava/util/function/Consumer;Ljava/util/function/Predicate;)V"
            ),
            index = 1
    )
    private static Predicate<BlockState> rcg$addCustomInputChests(Predicate<BlockState> original) {
        ModConfig config = CopperGolemPlusMod.getConfig();

        if (!config.allowReinforcedAsInput) {
            return original;
        }

        return (state) -> {
            if (original.test(state)) {
                return true;
            }

            for (String chestId : config.inputChests) {
                try {
                    Identifier id = Identifier.of(chestId);
                    Block targetBlock = Registries.BLOCK.get(id);
                    if (targetBlock != Blocks.AIR && state.isOf(targetBlock)) {
                        return true;
                    }
                } catch (Exception e) {
                    CopperGolemPlusMod.LOGGER.warn("Invalid chest identifier in config: {}", chestId);
                }
            }

            return false;
        };
    }

    @ModifyArg(
            method = "addIdleActivities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/task/MoveItemsTask;<init>(FLjava/util/function/Predicate;Ljava/util/function/Predicate;IILjava/util/Map;Ljava/util/function/Consumer;Ljava/util/function/Predicate;)V"
            ),
            index = 2
    )
    private static Predicate<BlockState> rcg$addCustomOutputChests(Predicate<BlockState> original) {
        ModConfig config = CopperGolemPlusMod.getConfig();

        if (!config.allowReinforcedAsOutput) {
            return original;
        }

        return (state) -> {
            if (original.test(state)) {
                return true;
            }

            for (String chestId : config.outputChests) {
                try {
                    Identifier id = Identifier.of(chestId);
                    Block targetBlock = Registries.BLOCK.get(id);
                    if (targetBlock != Blocks.AIR && state.isOf(targetBlock)) {
                        return true;
                    }
                } catch (Exception e) {
                    CopperGolemPlusMod.LOGGER.warn("Invalid chest identifier in config: {}", chestId);
                }
            }

            return false;
        };
    }
}