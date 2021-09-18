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
            lastSignal = signal();
        }

        @Override
        public boolean signal() {
            return getSignal(nb.get(1), this) | getSignal(nb.get(2), this) | getSignal(nb.get(3), this);
        }

        public boolean signalFront(){
            return signal();
        }
    }
}
