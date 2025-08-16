package cat.katie.createcreativetweaks.mixin.contraption_order.block;

import cat.katie.createcreativetweaks.duck.IContraptionSimulationAnchorBlock;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MechanicalPistonBlock.class, remap = false)
public class MechanicalPistonBlockMixin implements IContraptionSimulationAnchorBlock {
    @Override
    public Contraption cct$createContraption(BlockState state) {
        Direction direction = state.getValue(BlockStateProperties.FACING);
        return new PistonContraption(direction, false); // TODO: retracting mechanical pistons
    }
}
