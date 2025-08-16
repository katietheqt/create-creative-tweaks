package cat.katie.createcreativetweaks.mixin;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ExtendoGripRenderHandler.class, remap = false)
public class ExtendoGripRenderHandlerMixin {
    @ModifyExpressionValue(
            method = "onRenderPlayerHand",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/equipment/extendoGrip/ExtendoGripRenderHandler;getRenderedOffHandStack()Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private static ItemStack forceOffhandRenderingOff(ItemStack stack) {
        boolean enabled = AllConfigs.client().renderOffhandExtendoGrip.get();

        if (enabled) {
            return stack;
        }

        // if offhand rendering is disabled just return an empty stack
        return ItemStack.EMPTY;
    }
}
