package cat.katie.createcreativetweaks.mixin.limits;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.logistics.AddressEditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AddressEditBox.class, remap = false)
public class AddressEditBoxMixin {
    @WrapOperation(
            method = "<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/Font;IIIIZLjava/lang/String;)V",
            remap = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/AddressEditBox;setMaxLength(I)V"
            )
    )
    private void uncapAddressMaxLength(AddressEditBox instance, int length, Operation<Void> original) {
        if (!AllConfigs.client().uncapPackageAddresses.get()) {
            length = 65536; // nbt limit
        }

        original.call(instance, length);
    }
}
