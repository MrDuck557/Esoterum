package esoterum.world.blocks.binary;

public class BinaryButton extends BinaryBlock{
    // whether the button emits continuously (like a switch).
    public boolean continuous;
    // Buttons will have a pulse length of 60 ticks by default
    public float duration = 60;

    public BinaryButton(String name, boolean cont){
        super(name);
        outputs = new boolean[]{true, true, true, true};
        configurable = true;
        continuous = cont;
        emits = true;

        config(Boolean.class, (BinaryButtonBuild b, Boolean on) -> {
            b.lastSignal = on;
            b.timer = duration;
        });
    }

    public class BinaryButtonBuild extends BinaryBuild {
        public float timer;

        @Override
        public void updateTile() {
            super.updateTile();
            if(!continuous){
                if((timer -= delta()) <= 0){
                    lastSignal = false;
                }
            }
        }

        @Override
        public boolean configTapped(){
            if(continuous){
                configure(!lastSignal);
            }else{
                configure(true);
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
