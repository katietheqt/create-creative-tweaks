package cat.katie.createcreativetweaks.mixin;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CogWheelBlock.class, remap = false)
public class CogWheelBlockMixin {
    @Inject(
            method = "isValidCogwheelPosition",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void alwaysAllowCogWheelPlacement(boolean large, LevelReader worldIn, BlockPos pos, Direction.Axis cogAxis, CallbackInfoReturnable<Boolean> cir) {
        if (!worldIn.isClientSide()) {
            return;
        }

        boolean allowIllegalCogPlacement = AllConfigs.client().allowIllegalCogPlacement.get();

        if (allowIllegalCogPlacement) {
            cir.setReturnValue(true);
        }
    }
}
