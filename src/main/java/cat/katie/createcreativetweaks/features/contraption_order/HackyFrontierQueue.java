package cat.katie.createcreativetweaks.features.contraption_order;

import net.createmod.catnip.data.UniqueLinkedList;
import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Hacky solution to finding what order the frontier queue ended up in by tracking additions to it with a source tag.
 */
public class HackyFrontierQueue extends UniqueLinkedList<BlockPos> {
    private final ClientContraptionExtra state;

    @Nullable
    private BlockPos currentSourcePos;

    public HackyFrontierQueue(ClientContraptionExtra state) {
        this.state = state;
    }

    public void setCurrentSourcePos(@Nullable BlockPos currentSourcePos) {
        this.currentSourcePos = currentSourcePos;
    }

    @Override
    public boolean add(@Nonnull BlockPos pos) {
        if (super.add(pos)) {
            state.blockToSourcePos().put(pos, currentSourcePos);

            return true;
        } else {
            return false;
        }
    }
}
