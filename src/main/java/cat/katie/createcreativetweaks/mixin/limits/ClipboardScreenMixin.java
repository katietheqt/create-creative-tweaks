package cat.katie.createcreativetweaks.mixin.limits;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.equipment.clipboard.ClipboardScreen;
import net.createmod.catnip.gui.AbstractSimiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClipboardScreen.class, remap = false)
public abstract class ClipboardScreenMixin extends AbstractSimiScreen {
    @ModifyReturnValue(
            method = "validateTextForEntry",
            at = @At("RETURN")
    )
    private boolean allowIllegalClipboardText(boolean original) {
        boolean enabled = AllConfigs.client().uncapClipboards.get();

        if (enabled) {
            return true;
        } else {
            return original;
        }
    }
}
