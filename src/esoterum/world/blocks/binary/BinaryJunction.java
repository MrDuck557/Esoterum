package esoterum.world.blocks.binary;

public class BinaryJunction extends BinaryBlock{
    public BinaryJunction(String name){
        super(name);
        emits = true;
    }

    public class BinaryJunctionBuild extends BinaryBuild {
        @Override
        public void updateTile() {
            super.updateTile();
            lastSignal = false;
            for(BinaryBuild b : nb){
                lastSignal |= getSignal(b, this);
            };
        }

        @Override
        public boolean signalFront() {
            return getSignal(nb.get(2), this);
        }
        @Override
        public boolean signalBack() {
            return getSignal(nb.get(0), this);
        }
        @Override
        public boolean signalLeft() {
            return getSignal(nb.get(3), this);
        }
        @Override
        public boolean signalRight() {
            return getSignal(nb.get(1), this);
        }
    }
}
