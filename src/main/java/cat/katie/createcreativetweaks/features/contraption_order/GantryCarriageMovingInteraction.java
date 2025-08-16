package cat.katie.createcreativetweaks.features.contraption_order;

import cat.katie.createcreativetweaks.features.contraption_order.state.ContraptionOrderState;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * Custom moving interaction for gantry carriages to visualize their attached contraptions.
 */
public class GantryCarriageMovingInteraction extends MovingInteractionBehaviour {
    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity entity) {
        if (!entity.level().isClientSide || !player.getItemInHand(activeHand).isEmpty() || !player.isShiftKeyDown()) {
            return false;
        }

        if (!localPos.equals(BlockPos.ZERO)) {
            // only allow clicking the main anchor
            return false;
        }

        if (!ContraptionOrder.INSTANCE.handleExistingMovingAnchorClick(entity)) {
            // if no anchor exists, add a new one
            ContraptionOrder.INSTANCE.addNewMovingState(new ContraptionOrderState(entity, null));
        }

        return false; // don't send server packet
    }
}
