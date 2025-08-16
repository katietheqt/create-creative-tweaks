package cat.katie.createcreativetweaks.features.contraption_order;

import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.Function;

/**
 * Stores extra state for client contraption assembly simulation.
 */
public final class ClientContraptionExtra {
    private final List<BlockPos> assemblyOrder;
    private Map<BlockPos, BlockPos> blockToSourcePos;

    public ClientContraptionExtra() {
        this.assemblyOrder = new ArrayList<>();
        this.blockToSourcePos = new HashMap<>();
    }

    public List<BlockPos> assemblyOrder() {
        return assemblyOrder;
    }

    public Map<BlockPos, BlockPos> blockToSourcePos() {
        return blockToSourcePos;
    }

    public void convertBlockToSourcePosToLocal(Function<BlockPos, BlockPos> toLocalPos) {
        Map<BlockPos, BlockPos> converted = new HashMap<>(blockToSourcePos.size());

        for (Map.Entry<BlockPos, BlockPos> entry : blockToSourcePos.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockPos source = entry.getValue();

            if (source != null) {
                converted.put(toLocalPos.apply(pos), toLocalPos.apply(source));
            } else {
                converted.put(toLocalPos.apply(pos), null);
            }
        }

        blockToSourcePos = converted;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ClientContraptionExtra) obj;
        return Objects.equals(this.assemblyOrder, that.assemblyOrder) &&
                Objects.equals(this.blockToSourcePos, that.blockToSourcePos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assemblyOrder, blockToSourcePos);
    }

    @Override
    public String toString() {
        return "ContraptionOrderState[" +
                "assemblyOrder=" + assemblyOrder + ", " +
                "blockToSourcePos=" + blockToSourcePos + ']';
    }

}
