package cat.katie.createcreativetweaks.mixin.contraption_order.contraption;

import cat.katie.createcreativetweaks.duck.ContraptionDuck;
import cat.katie.createcreativetweaks.features.contraption_order.ClientContraptionExtra;
import cat.katie.createcreativetweaks.features.contraption_order.HackyFrontierQueue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import net.createmod.catnip.data.UniqueLinkedList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.Set;

@Mixin(value = Contraption.class, remap = false)
public abstract class ContraptionMixin implements ContraptionDuck {
    @Shadow public AbstractContraptionEntity entity;
    @Unique
    private ClientContraptionExtra cct$clientExtra;

    @Unique
    @Override
    public void cct$setClientExtra(ClientContraptionExtra clientExtra) {
        cct$clientExtra = clientExtra;
    }

    @Unique
    @Override
    public ClientContraptionExtra cct$getClientExtra() {
        return cct$clientExtra;
    }

    @WrapOperation(
            method = "searchMovedStructure",
            at = @At(
                    value = "NEW",
                    target = "net/createmod/catnip/data/UniqueLinkedList"
            )
    )
    private UniqueLinkedList<BlockPos> replaceFrontierQueue(Operation<UniqueLinkedList<BlockPos>> original) {
        if (cct$clientExtra == null) {
            return original.call();
        }

        return new HackyFrontierQueue(cct$clientExtra);
    }

    @WrapMethod(method = "moveBlock")
    private boolean trackFrontierQueueAdditions(Level world, Direction forcedDirection, Queue<BlockPos> frontier, Set<BlockPos> visited, Operation<Boolean> original) {
        if (!(frontier instanceof HackyFrontierQueue hackyFrontier)) {
            return original.call(world, forcedDirection, frontier, visited);
        }

        BlockPos pos = frontier.peek();
        assert pos != null;

        hackyFrontier.setCurrentSourcePos(pos);

        try {
            return original.call(world, forcedDirection, frontier, visited);
        } finally {
            hackyFrontier.setCurrentSourcePos(null);
        }
    }

    @WrapWithCondition(
            method = "moveBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/bearing/WindmillBearingBlockEntity;disassembleForMovement()V"
            )
    )
    private boolean dontDisassembleWindmills(WindmillBearingBlockEntity instance) {
        // TODO: show disassembly order of windmills (subcontraptions)
        return cct$clientExtra == null;
    }

    @WrapWithCondition(
            method = "moveBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/chainConveyor/ChainConveyorBlockEntity;notifyConnectedToValidate()V"
            )
    )
    private boolean dontNotifyChainConveyors(ChainConveyorBlockEntity instance) {
        return cct$clientExtra == null;
    }

    @Inject(
            method = "startMoving",
            at = @At("HEAD"),
            cancellable = true
    )
    private void neverStartMoving(Level world, CallbackInfo ci) {
        if (cct$clientExtra != null) {
            ci.cancel();
        }
    }

    @Inject(
            method = "addBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;minmax(Lnet/minecraft/world/phys/AABB;)Lnet/minecraft/world/phys/AABB;"
            )
    )
    private void logOrderOfAddedBlocks(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair, CallbackInfo ci, @Local(ordinal = 1) BlockPos localPos) {
        if (cct$clientExtra == null) {
            return;
        }

        cct$clientExtra.assemblyOrder().add(localPos);
    }
}
