package com.nmdx86.cgplus.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.task.MoveItemsTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(MoveItemsTask.class)
public interface MoveItemsTaskAccessor {

    @Invoker("getSearchBoundingBox")
    Box invokeGetSearchBoundingBox(PathAwareEntity entity);

    @Invoker("getHorizontalRange")
    int invokeGetHorizontalRange(PathAwareEntity entity);

    @Invoker("getStorageFor")
    MoveItemsTask.Storage invokeGetStorageFor(
            PathAwareEntity entity,
            World world,
            BlockEntity blockEntity,
            Set<GlobalPos> visitedPositions,
            Set<GlobalPos> unreachablePositions,
            Box box
    );

    @Invoker("getVisitedPositions")
    static Set<GlobalPos> invokeGetVisitedPositions(PathAwareEntity entity) {
        throw new AssertionError();
    }

    @Invoker("getUnreachablePositions")
    static Set<GlobalPos> invokeGetUnreachablePositions(PathAwareEntity entity) {
        throw new AssertionError();
    }
}