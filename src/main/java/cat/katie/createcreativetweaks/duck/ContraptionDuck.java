package cat.katie.createcreativetweaks.duck;

import cat.katie.createcreativetweaks.features.contraption_order.ClientContraptionExtra;

public interface ContraptionDuck {
    /**
     * Sets the extra state. This being non-null changes contraption behaviour to client-side behaviour so this MUST NOT
     * be called on server contraptions.
     */
    void cct$setClientExtra(ClientContraptionExtra clientExtra);

    ClientContraptionExtra cct$getClientExtra();
}
