package esoterum.content;

import esoterum.world.blocks.binary.*;
import mindustry.ctype.ContentList;
import mindustry.world.*;

public class EsoBlocks implements ContentList {
    public Block
        esoWire, esoSwitch, esoButton;

    @Override
    public void load() {
        esoWire = new BinaryWire("wire");

        esoSwitch = new BinaryButton("switch", true);

        esoButton = new BinaryButton("button", false);
    }
}
