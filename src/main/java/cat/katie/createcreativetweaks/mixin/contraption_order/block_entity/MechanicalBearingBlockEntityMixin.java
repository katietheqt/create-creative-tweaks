package cat.katie.createcreativetweaks.mixin.contraption_order.block_entity;

import cat.katie.createcreativetweaks.duck.IMovingContraptionAnchorBlockEntity;
import cat.katie.createcreativetweaks.features.contraption_order.ContraptionOrder;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MechanicalBearingBlockEntity.class, remap = false)
public abstract class MechanicalBearingBlockEntityMixin extends GeneratingKineticBlockEntity implements IMovingContraptionAnchorBlockEntity {
    @Shadow protected ControlledContraptionEntity movedContraption;

    public MechanicalBearingBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public AbstractContraptionEntity cct$getExistingContraptionEntity() {
        return movedContraption;
    }

    @Inject(
            method = "attach",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/contraptions/bearing/MechanicalBearingBlockEntity;movedContraption:Lcom/simibubi/create/content/contraptions/ControlledContraptionEntity;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void convertStationaryToAssembledContraption(ControlledContraptionEntity contraption, CallbackInfo ci) {
        if (level == null || !level.isClientSide) {
            return;
        }

        ContraptionOrder.INSTANCE.onStationaryContraptionAssembly(getBlockPos(), movedContraption);
    }
}
