package cat.katie.createcreativetweaks.mixin.accessors;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
    @Invoker("create")
    static RenderType.CompositeRenderType cct$create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, RenderType.CompositeState state) {
        throw new AssertionError();
    }
}
