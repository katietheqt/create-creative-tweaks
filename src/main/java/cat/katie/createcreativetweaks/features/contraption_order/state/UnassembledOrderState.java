package cat.katie.createcreativetweaks.features.contraption_order.state;

import cat.katie.createcreativetweaks.duck.ContraptionDuck;
import cat.katie.createcreativetweaks.features.contraption_order.ClientContraptionExtra;
import cat.katie.createcreativetweaks.mixin.accessors.ContraptionAccessor;
import cat.katie.createcreativetweaks.util.CCTLang;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Anchor state representing an unassembled contraption starting at a given anchor block.
 */
public class UnassembledOrderState extends OrderState {
    private final Level level;
    private final BlockPos anchorPos;
    private final BlockState expectedAnchorState;
    private final Supplier<Contraption> contraptionSupplier;

    @MonotonicNonNull
    private Contraption contraption;

    @Nullable
    private AssemblyException lastException;

    public UnassembledOrderState(Level level, BlockPos anchorPos, BlockState expectedAnchorState, Supplier<Contraption> contraptionSupplier) {
        this.level = level;
        this.anchorPos = anchorPos;
        this.expectedAnchorState = expectedAnchorState;
        this.contraptionSupplier = contraptionSupplier;
    }

    public boolean isInvalid() {
        Minecraft client = Minecraft.getInstance();

        if (client.level != level) {
            // client changed level
            return true;
        }

        if (!level.isLoaded(anchorPos)) {
            // chunk isn't loaded
            return true;
        }

        // check state is the same
        return level.getBlockState(anchorPos) != expectedAnchorState;
    }

    @Override
    public void tick() {
        super.tick();

        contraption = contraptionSupplier.get();

        ClientContraptionExtra extra = new ClientContraptionExtra();
        ((ContraptionDuck) contraption).cct$setClientExtra(extra);

        try {
            if (!contraption.assemble(level, anchorPos)) {
                return;
            }

            lastException = null;
        } catch (AssemblyException e) {
            lastException = e;
        }

        extra.convertBlockToSourcePosToLocal((pos) -> ((ContraptionAccessor) contraption).cct$toLocalPos(pos));
    }

    @Override
    @MonotonicNonNull
    public Contraption contraption() {
        return contraption;
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    @Nullable
    public BlockPos anchorPos() {
        return anchorPos;
    }

    @Nullable
    public AssemblyException lastException() {
        return lastException;
    }

    private boolean addAssemblyExceptionToTooltip(List<Component> tooltip) {
        if (lastException == null) {
            return false;
        }

        if (!tooltip.isEmpty())
            tooltip.add(CommonComponents.EMPTY);

        CCTLang.translate("gui.client_assembly.exception").style(ChatFormatting.GOLD)
                .forGoggles(tooltip);

        String text = lastException.component.getString();
        Arrays.stream(text.split("\n"))
                .forEach(l -> TooltipHelper.cutStringTextComponent(l, FontHelper.Palette.GRAY_AND_WHITE)
                        .forEach(c -> CCTLang.builder().add(c).forGoggles(tooltip)));

        return true;
    }

    @Override
    public boolean addGogglesTooltip(List<Component> tooltip) {
        boolean added = super.addGogglesTooltip(tooltip);

        if (!tooltip.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
        }

        if (addAssemblyExceptionToTooltip(tooltip)) {
            added = true;
        } else if (!tooltip.isEmpty()) {
            tooltip.remove(tooltip.size() - 1);
        }

        return added;
    }

    @Override
    public void setupRenderTransform(PoseStack stack, float partialTicks) {
        Vec3 anchor = Vec3.atLowerCornerOf(contraption.anchor);

        stack.translate(anchor.x, anchor.y, anchor.z);
    }

    @Override
    public Matrix4f inverseRotationMatrix(float partialTicks) {
        return new Matrix4f();
    }
}
