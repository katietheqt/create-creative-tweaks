package cat.katie.createcreativetweaks.mixin.contraption_order.block;

import cat.katie.createcreativetweaks.duck.IContraptionSimulationAnchorBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BearingBlock.class, remap = false)
public class BearingBlockMixin implements IContraptionSimulationAnchorBlock {
    @Override
    public Contraption cct$createContraption(BlockState state) {
        Direction direction = state.getValue(BearingBlock.FACING);
        return new BearingContraption(AllBlocks.WINDMILL_BEARING.has(state), direction);
    }
}
