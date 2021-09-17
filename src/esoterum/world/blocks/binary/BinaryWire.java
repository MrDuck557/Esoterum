package esoterum.world.blocks.binary;

public class BinaryWire extends BinaryBlock{
    public BinaryWire(String name){
        super(name, true, false, false, false);
        emits = true;
        rotate = true;
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
                    lastSignal
                : lastSignal )

                | nextSignal;
        }
    }
}
