package cat.katie.createcreativetweaks.mixin.limits;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.logistics.packagePort.PackagePortScreen;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PackagePortScreen.class, remap = false)
public class PackagePortScreenMixin {
    @WrapOperation(
            method = "init",
            remap = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/EditBox;setMaxLength(I)V"
            )
    )
    private void uncapAddressMaxLength(EditBox instance, int length, Operation<Void> original) {
        if (!AllConfigs.client().uncapPackageAddresses.get()) {
            length = 32767; // max packet length
        }

        original.call(instance, length);
    }
}
