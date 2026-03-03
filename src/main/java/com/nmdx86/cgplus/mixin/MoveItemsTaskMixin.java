package com.nmdx86.cgplus.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ai.brain.task.MoveItemsTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(MoveItemsTask.class)
public class MoveItemsTaskMixin {

    @Inject(method = "findStorage", at = @At("HEAD"), cancellable = true)
    private void rcg$unifiedFindStorage(
            ServerWorld world,
            PathAwareEntity entity,
            CallbackInfoReturnable<Optional<MoveItemsTask.Storage>> cir
    ) {
        MoveItemsTaskAccessor accessor = (MoveItemsTaskAccessor)(Object) this;

        Box box = accessor.invokeGetSearchBoundingBox(entity);
        Set<GlobalPos> visited = MoveItemsTaskAccessor.invokeGetVisitedPositions(entity);
        Set<GlobalPos> unreachable = MoveItemsTaskAccessor.invokeGetUnreachablePositions(entity);

        MoveItemsTask.Storage best = null;
        double bestDist = Double.MAX_VALUE;

        for (ChunkPos chunkPos : ChunkPos.stream(
                new ChunkPos(entity.getBlockPos()),
                Math.floorDiv(accessor.invokeGetHorizontalRange(entity), 16) + 1
        ).toList()) {
            WorldChunk chunk = world.getChunkManager().getWorldChunk(chunkPos.x, chunkPos.z);
            if (chunk == null) continue;

            for (BlockEntity be : chunk.getBlockEntities().values()) {
                // Accept any block entity that exposes an Inventory
                if (!(be instanceof Inventory)) continue;

                double dist = be.getPos().getSquaredDistance(entity.getEntityPos());
                if (dist < bestDist) {
                    MoveItemsTask.Storage storage = accessor.invokeGetStorageFor(
                            entity, world, be, visited, unreachable, box);
                    if (storage != null) {
                        best = storage;
                        bestDist = dist;
                    }
                }
            }
        }

        cir.setReturnValue(best != null ? Optional.of(best) : Optional.empty());
    }

    @Inject(method = "getContainerStorages", at = @At("HEAD"), cancellable = true)
    private void rcg$safeGetContainerStorages(
            MoveItemsTask.Storage storage,
            World world,
            CallbackInfoReturnable<Stream<MoveItemsTask.Storage>> cir
    ) {
        if (!(storage.blockEntity() instanceof ChestBlockEntity)) {
            cir.setReturnValue(Stream.of(storage));
        }
    }
}