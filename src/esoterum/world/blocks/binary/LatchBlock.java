package esoterum.world.blocks.binary;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class LatchBlock extends BinaryBlock{
    public TextureRegion latchRegion;

    public LatchBlock(String name){
        super(name);
        outputs = new boolean[]{true, false, false, false};
        inputs = new boolean[]{false, true, true, true};
        emits = true;
        rotate = true;
        rotatedBase = true;
        drawArrow = true;
        baseType = 1;
        
        config(Boolean.class, (LatchBuild l, Boolean b) -> {
            l.signal[0] = b;
        });
    }

    @Override
    public void load() {
        super.load();
        latchRegion = Core.atlas.find(name + "-latch");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            region,
            topRegion,
            latchRegion
        };
    }

    public class LatchBuild extends BinaryBuild {
        public boolean tmp = true;

        // laggy?
        @Override
        public void updateTile() {
            super.updateTile();
            if(signal[0] != tmp) propagateSignal(true, false, false, false);
            tmp = signal[0];
        }

        @Override
        public void updateSignal(int source) {
            try {
                signal[4] = getSignal(nb.get(1), this) | getSignal(nb.get(3), this);
                if(getSignal(nb.get(2), this)) configure(signal[4]);
            } catch(Exception e){}
        }

        @Override
        public void draw() {
            drawBase();
            drawConnections();
            Draw.color(Color.white, team.color, Mathf.num(getSignal(nb.get(1), this) | getSignal(nb.get(2), this) | getSignal(nb.get(3), this)));
            Draw.rect(topRegion, x, y, (rotate && drawRot) ? rotdeg() : 0f);

            Draw.color(signal[0] ? team.color : Color.white);
            Draw.rect(latchRegion, x, y);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, (byte)(revision + 1));

            if(revision >= 2){
                signal[0] = read.bool();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.bool(signal[0]);
        }

        @Override
        public byte version() {
            return 2;
        }
    }
}
