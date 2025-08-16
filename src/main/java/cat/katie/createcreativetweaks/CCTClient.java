package cat.katie.createcreativetweaks;

import cat.katie.createcreativetweaks.infrastructure.rendering.CustomRenderTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CCTClient {
    private CCTClient() {
        throw new UnsupportedOperationException("Cannot instantiate CCTClient");
    }

    public static void onCtorClient(IEventBus modEventBus) {
        CustomRenderTypes.init();

        modEventBus.addListener(CCTClient::clientInit);
    }

    private static void clientInit(FMLClientSetupEvent event) {

    }
}
