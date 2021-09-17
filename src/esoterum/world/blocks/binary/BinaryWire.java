package esoterum.world.blocks.binary;

import arc.Core;

public class BinaryWire extends BinaryBlock{
    public BinaryWire(String name){
        super(name);
        outputs = new boolean[]{true, false, false, false};
        emits = true;
        rotate = true;
    }

    @Override
    public void load() {
        super.load();
        connectionRegion = Core.atlas.find("esoterum-connection-large");
        topRegion = Core.atlas.find("esoterum-wire-top");
    }

    public class BinaryWireBuild extends BinaryBuild {

        @Override
        public void updateTile() {
            super.updateTile();
            // retrieving signal from back is unnecessary here, but needed for drawing
            // (might remove later)
            lastSignal = nextSignal | getSignal(nb.get(2), this);

            // get input from sides
            nextSignal = getSignal(nb.get(1), this) | getSignal(nb.get(3), this);
        }

        public boolean signalFront(){
            // if the block behind is facing the same rotation as this block,
            // get its signal input directly for instant transmission
            return (nb.get(2) != null ?
                nb.get(2).rotation == rotation || !nb.get(2).block.rotate ?
                    getSignal(nb.get(2), this) :
                    nextSignal
                : nextSignal )

                | nextSignal;
        }
    }
}
