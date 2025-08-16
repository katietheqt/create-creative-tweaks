package cat.katie.createcreativetweaks.features;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class PermaGoggles {
    public static void setup() {
        GogglesItem.addIsWearingPredicate(player -> {
            Minecraft client = Minecraft.getInstance();

            if (player != client.player) {
                return false;
            }

            PermaGogglesState state = AllConfigs.client().permaGoggles.get();
            return state.check(player);
        });
    }

    public enum PermaGogglesState {
        DISABLED($ -> false),
        CREATIVE(player -> player.isCreative() || player.isSpectator()),
        ALWAYS($ -> true);

        private final Predicate<Player> isWearingGoggles;

        PermaGogglesState(Predicate<Player> isWearingGoggles) {
            this.isWearingGoggles = isWearingGoggles;
        }

        public boolean check(Player player) {
            return this.isWearingGoggles.test(player);
        }
    }
}
