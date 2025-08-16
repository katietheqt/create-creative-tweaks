package cat.katie.createcreativetweaks.mixin.contraption_order.entity;

import cat.katie.createcreativetweaks.duck.IContraptionEntityWithRotation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ControlledContraptionEntity.class, remap = false)
public abstract class ControlledContraptionEntityClientMixin extends AbstractContraptionEntity implements IContraptionEntityWithRotation {
    public ControlledContraptionEntityClientMixin(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow public abstract float getAngle(float partialTicks);

    @Shadow public abstract Direction.Axis getRotationAxis();

    @Override
    public Matrix4f cct$inverseRotationMatrix(float partialTicks) {
        float angle = getAngle(partialTicks);
        Direction.Axis axis = getRotationAxis();

        if (axis != null) {
            PoseStack stack = new PoseStack();

            TransformStack.of(stack)
                    .center()
                    .rotateDegrees(angle, axis)
                    .uncenter();

            return stack.last().pose().invert();
        } else {
            return new Matrix4f();
        }
    }
}
