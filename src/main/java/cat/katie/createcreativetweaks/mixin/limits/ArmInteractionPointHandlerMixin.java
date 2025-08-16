package cat.katie.createcreativetweaks.mixin.limits;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ArmInteractionPointHandler.class, remap = false)
public class ArmInteractionPointHandlerMixin {
    @WrapOperation(
            method = "flushSettings",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z",
                    remap = true
            )
    )
    private static boolean disableMechanicalArmRangeLimits(BlockPos instance, Vec3i vector, double distance,
                                                           Operation<Boolean> original) {
        if (AllConfigs.client().disableMechanicalArmRangeChecks.get()) {
            return true;
        }

        return original.call(instance, vector, distance);
    }
}
