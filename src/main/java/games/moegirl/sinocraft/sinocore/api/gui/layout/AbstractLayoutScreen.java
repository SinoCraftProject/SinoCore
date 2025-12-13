package games.moegirl.sinocraft.sinocore.api.gui.layout;

import games.moegirl.sinocraft.sinocore.api.gui.screen.AbstractScreen;
import lombok.Getter;
import net.minecraft.network.chat.Component;

@Getter
public class AbstractLayoutScreen extends AbstractScreen {
    protected final Layout layout;

    protected int startX;
    protected int startY;

    public AbstractLayoutScreen(Layout layout, Component title) {
        super(title);
        this.layout = layout;
    }

    @Override
    protected void init() {
        super.init();
        startX = (getWidth() - layout.getWidth()) / 2;
        startY = (getHeight() - layout.getHeight()) / 2;
    }

    @Override
    protected void createChildren() {

    }
}
