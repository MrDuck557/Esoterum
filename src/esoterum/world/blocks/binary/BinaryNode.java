package esoterum.world.blocks.binary;

public class BinaryNode extends BinaryBlock{
    public int range = 45;
    public BinaryNode(String name, int linkRange){
        super(name);
        rotate = false;
        emits = true;
        range = linkRange;

        outputs = new boolean[]{true, true, true, true};
        inputs = new boolean[]{true, true, true, true};

    }

    // TODO Node connections
    public class BinaryNodeBuild extends BinaryBuild{
        public BinaryNodeBuild link = null;

        @Override
        public void updateTile() {
            super.updateTile();
            lastSignal = signal() || link != null && link.signal();
        }

        @Override
        public boolean signal() {
            for(BinaryBuild b : nb){
                if(getSignal(b, this))return true;
            }
            return false;
        }

        // yes, there is no other way to do this
        // absolutely no way.
        @Override
        public boolean signalFront() {
            return lastSignal;
        }

        @Override
        public boolean signalLeft() {
            return lastSignal;
        }

        @Override
        public boolean signalBack() {
            return lastSignal;
        }

        @Override
        public boolean signalRight() {
            return lastSignal;
        }
    }
}
