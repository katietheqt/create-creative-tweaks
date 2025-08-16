package cat.katie.createcreativetweaks.mixin.contraption_order;

import cat.katie.createcreativetweaks.features.contraption_order.ContraptionOrder;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "onClientRemoval",
            at = @At("HEAD")
    )
    private void handleAbstractContraptionEntityRemoval(CallbackInfo ci) {
        if ((Object) this instanceof AbstractContraptionEntity contraptionEntity) {
            Level level = contraptionEntity.level();

            if (!level.isClientSide) {
                return;
            }

            ContraptionOrder.INSTANCE.onContraptionDisassembly(contraptionEntity);
        }
    }
}
