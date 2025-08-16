package cat.katie.createcreativetweaks;

import cat.katie.createcreativetweaks.features.PermaGoggles;
import cat.katie.createcreativetweaks.features.contraption_order.GantryCarriageMovingInteraction;
import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateCreativeTweaks.ID)
public class CreateCreativeTweaks {
    public static final String ID = "create_creative_tweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateCreativeTweaks() {
        PermaGoggles.setup();

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CreateCreativeTweaks::init);
        AllConfigs.register(modLoadingContext);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CCTClient.onCtorClient(modEventBus));
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MovingInteractionBehaviour existingBehaviour = MovingInteractionBehaviour.REGISTRY.get(AllBlocks.GANTRY_CARRIAGE.get());

            if (existingBehaviour == null) {
                MovingInteractionBehaviour.REGISTRY.register(AllBlocks.GANTRY_CARRIAGE.get(), new GantryCarriageMovingInteraction());
            } else {
                LOGGER.warn("A different mod already registered a gantry interaction behaviour - ours will be skipped");
            }
        });
    }
}