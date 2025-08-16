package cat.katie.createcreativetweaks.features.contraption_order.state;

import cat.katie.createcreativetweaks.duck.IContraptionEntityWithRotation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joml.Matrix4f;

import java.util.Objects;

/**
 * Anchor state tied to a contraption entity.
 */
public class ContraptionOrderState extends OrderState {
    private final AbstractContraptionEntity entity;

    @Nullable
    private final BlockPos anchorPos;

    public ContraptionOrderState(UnassembledOrderState unassembled, AbstractContraptionEntity entity) {
        this(entity, Objects.requireNonNull(unassembled.anchorPos()));
    }

    public ContraptionOrderState(AbstractContraptionEntity entity, @Nullable BlockPos anchorPos) {
        super();
        this.entity = entity;
        this.displayState = DisplayState.ACTORS_STORAGES;
        this.anchorPos = anchorPos;
    }

    @Override
    public boolean isStateValid(DisplayState state) {
        return state != DisplayState.FRONTIER;
    }

    @Override
    public boolean isInvalid() {
        return this.entity.isRemoved();
    }

    public AbstractContraptionEntity entity() {
        return entity;
    }

    @Override
    public @MonotonicNonNull Contraption contraption() {
        return entity.getContraption();
    }

    @Override
    public Level level() {
        return entity.level();
    }

    @Override
    @Nullable
    public BlockPos anchorPos() {
        return this.anchorPos;
    }

    @Override
    public void setupRenderTransform(PoseStack stack, float partialTicks) {
        double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTicks, entity.yOld, entity.getY());
        double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        stack.translate(x, y, z);

        entity.applyLocalTransforms(stack, partialTicks);
    }

    @Override
    public Matrix4f inverseRotationMatrix(float partialTicks) {
        return ((IContraptionEntityWithRotation) entity).cct$inverseRotationMatrix(partialTicks);
    }
}
