package cat.katie.createcreativetweaks.mixin.contraption_order.block;

import cat.katie.createcreativetweaks.duck.IContraptionSimulationAnchorBlock;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.gantry.GantryContraption;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GantryCarriageBlock.class, remap = false)
public class GantryCarriageBlockMixin implements IContraptionSimulationAnchorBlock {
    @Override
    public Contraption cct$createContraption(BlockState state) {
        Direction direction = state.getValue(GantryCarriageBlock.FACING);
        return new GantryContraption(direction);
    }
}
