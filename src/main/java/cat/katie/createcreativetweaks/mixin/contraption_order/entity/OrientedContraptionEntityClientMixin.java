package cat.katie.createcreativetweaks.mixin.contraption_order.entity;

import cat.katie.createcreativetweaks.duck.IContraptionEntityWithRotation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = OrientedContraptionEntity.class, remap = false)
public abstract class OrientedContraptionEntityClientMixin extends AbstractContraptionEntity implements IContraptionEntityWithRotation {
    public OrientedContraptionEntityClientMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow public abstract float getInitialYaw();

    @Shadow public abstract float getViewYRot(float partialTicks);

    @Shadow public abstract float getViewXRot(float partialTicks);

    @Override
    public Matrix4f cct$inverseRotationMatrix(float partialTicks) {
        float angleInitialYaw = getInitialYaw();
        float angleYaw = getViewYRot(partialTicks);
        float anglePitch = getViewXRot(partialTicks);

        PoseStack stack = new PoseStack();

        TransformStack.of(stack)
                .center()
                .rotateYDegrees(angleYaw)
                .rotateZDegrees(anglePitch)
                .rotateYDegrees(angleInitialYaw)
                .uncenter();

        return stack.last().pose().invert();
    }
}
