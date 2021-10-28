package esoterum.world.blocks.binary.transmission;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.io.*;
import esoterum.world.blocks.binary.*;

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
        baseHighlight = "gold";
        propagates = true;
        config(Boolean.class, (LatchBuild l, Boolean b) -> l.signal[0] = b);
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
        @Override
        public void updateSignal() {
            if(getSignal(nb[2], this)) signal[0] = getSignal(nb[1], this) | getSignal(nb[3], this);
        }

        @Override
        public void draw() {
            drawBase();
            drawConnections();
            Draw.color(Color.white, team.color, Mathf.num(getSignal(nb[1], this) | getSignal(nb[2], this) | getSignal(nb[3], this)));
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
            if(revision >= 3){
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
