package cat.katie.createcreativetweaks.features.contraption_order.state;

import cat.katie.createcreativetweaks.util.CCTLang;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.Contraption;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joml.Matrix4f;

import java.util.List;

/**
 * State of a contraption tied to an anchor block.
 */
public abstract class OrderState {
    protected DisplayState displayState = DisplayState.FRONTIER;

    public abstract boolean isInvalid();

    /**
     * Handles a click on the anchor block of an existing unassembled contraption.
     *
     * @return {@literal true} if custom handling was applied, or {@literal false} to apply the default handling (
     * removing the contraption).
     */
    public final boolean handleClick() {
        do {
            displayState = displayState.next();
        } while (!isStateValid(displayState));

        return displayState != DisplayState.DISABLED;
    }

    public DisplayState displayState() {
        return displayState;
    }

    public final void setDisplayState(DisplayState displayState) {
        if (displayState == DisplayState.DISABLED) {
            throw new IllegalStateException("attempted to set display state to disabled");
        }

        while (!isStateValid(displayState)) {
            displayState = displayState.next();
        }

        if (displayState == DisplayState.DISABLED) {
            return; // keep the default display state if there is no further valid ones
        }

        this.displayState = displayState;
    }

    public abstract Level level();

    @MonotonicNonNull
    public abstract Contraption contraption();

    public boolean isStateValid(DisplayState state) {
        return true;
    }

    public void tick() {

    }

    @Nullable
    public abstract BlockPos anchorPos();

    public boolean addGogglesTooltip(List<Component> tooltip) {
        return addInfoToTooltip(tooltip);
    }

    private boolean addInfoToTooltip(List<Component> tooltip) {
        CCTLang.translate("gui.client_assembly.header").style(ChatFormatting.GOLD)
                .forGoggles(tooltip);

        LangBuilder builder = switch (displayState) {
            case FRONTIER -> CCTLang.translate("gui.client_assembly.state.all");
            case ACTORS_STORAGES -> CCTLang.translate("gui.client_assembly.state.actors");
            case DISABLED -> throw new IllegalStateException();
        };

        builder.style(ChatFormatting.GRAY).forGoggles(tooltip);

        return true;
    }

    public abstract void setupRenderTransform(PoseStack stack, float partialTicks);
    public abstract Matrix4f inverseRotationMatrix(float partialTicks);
}
