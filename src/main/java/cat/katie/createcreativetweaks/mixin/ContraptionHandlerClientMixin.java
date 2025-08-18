package cat.katie.createcreativetweaks.mixin;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionHandlerClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContraptionHandlerClient.class, remap = false)
public class ContraptionHandlerClientMixin {
    @WrapOperation(
            method = "rightClickingOnContraptionsGetsHandledLocally",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/AbstractContraptionEntity;handlePlayerInteraction(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/InteractionHand;)Z"
            )
    )
    private static boolean rememberNormalInteractionResult(AbstractContraptionEntity instance, Player entry, BlockPos transformedVector, Direction direction, InteractionHand player, Operation<Boolean> original, @Share("handled") LocalBooleanRef handled) {
        boolean result = original.call(instance, entry, transformedVector, direction, player);

        if (result) {
            handled.set(true);
        }

        return result;
    }

    @WrapOperation(
            method = "rightClickingOnContraptionsGetsHandledLocally",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/ContraptionHandlerClient;handleSpecialInteractions(Lcom/simibubi/create/content/contraptions/AbstractContraptionEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/InteractionHand;)Z"
            )
    )
    private static boolean rememberSpecialInteractionResult(AbstractContraptionEntity car, Player contraptionEntity, BlockPos player, Direction localPos, InteractionHand side, Operation<Boolean> original, @Share("handled") LocalBooleanRef handled) {
        boolean result = original.call(car, contraptionEntity, player, localPos, side);

        if (result) {
            handled.set(true);
        }

        return result;
    }

    @Inject(
            method = "rightClickingOnContraptionsGetsHandledLocally",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/event/InputEvent$InteractionKeyMappingTriggered;setCanceled(Z)V"
            )
    )
    private static void dontCancelEventIfWeDidntHit(InputEvent.InteractionKeyMappingTriggered event, CallbackInfo ci, @Share("handled") LocalBooleanRef handled) {
        if (!AllConfigs.client().clickThroughContraptions.get()) {
            return;
        }

        if (!handled.get()) {
            ci.cancel();
        }
    }
}
