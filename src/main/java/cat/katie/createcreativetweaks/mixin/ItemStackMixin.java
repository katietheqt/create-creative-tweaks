package cat.katie.createcreativetweaks.mixin;

import cat.katie.createcreativetweaks.features.ToolboxTooltip;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @SuppressWarnings({"UnresolvedMixinReference", "LocalMayBeArgsOnly"}) // MCDev plugin doesn't correctly report this
    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"
            )
    )
    private void appendToolboxTooltip(Player player, TooltipFlag isAdvanced,
                                      CallbackInfoReturnable<List<Component>> cir, @Local List<Component> tooltip) {
        ToolboxTooltip.onItemTooltip((ItemStack) (Object) this, tooltip);
    }
}
