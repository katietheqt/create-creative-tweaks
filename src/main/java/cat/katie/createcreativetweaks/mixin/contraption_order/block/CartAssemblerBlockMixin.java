package cat.katie.createcreativetweaks.mixin.contraption_order.block;

import cat.katie.createcreativetweaks.duck.IContraptionSimulationAnchorBlock;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CartAssemblerBlock.class, remap = false)
public class CartAssemblerBlockMixin implements IContraptionSimulationAnchorBlock {
    @Override
    public Contraption cct$createContraption(BlockState state) {
        return new MountedContraption();
    }
}
