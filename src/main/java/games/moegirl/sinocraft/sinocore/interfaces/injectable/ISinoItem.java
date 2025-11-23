package games.moegirl.sinocraft.sinocore.interfaces.injectable;

import games.moegirl.sinocraft.sinocore.client.item.ISinoClientItem;

public interface ISinoItem {

    @Deprecated(forRemoval = true, since = "1.2.0")
    default ISinoClientItem sino$getClientItem() {
        return null;
    }
}
