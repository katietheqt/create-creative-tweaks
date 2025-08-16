package cat.katie.createcreativetweaks.infrastructure.rendering;

import cat.katie.createcreativetweaks.mixin.accessors.RenderTypeAccessor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class CustomRenderTypes extends RenderStateShard {
    public static final RenderType.CompositeRenderType THROUGH_WALL_LINES = RenderTypeAccessor.cct$create(
            "lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public CustomRenderTypes(String name, Runnable setupState, Runnable clearState) {
        super(name, setupState, clearState);
        throw new IllegalStateException();
    }

    public static void init() {}
}
