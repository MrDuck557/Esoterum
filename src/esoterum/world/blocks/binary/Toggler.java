package esoterum.world.blocks.binary;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.logic.LAccess;

public class Toggler extends BinaryBlock{
    public Toggler(String name){
        super(name);
        rotate = true;
        rotatedBase = true;
        baseType = 1;
        emits = true;
        outputs = new boolean[]{true, false, false, false};
        inputs = new boolean[]{false, true, true, true};


    }

    public class TogglerBuild extends BinaryBuild {
        @Override
        public void updateTile() {
            super.updateTile();
            lastSignal = nextSignal;
            nextSignal = signal();

            if(front() != null){
                front().control(LAccess.enabled, lastSignal ? 1d : 0d, 0d, 0d, 0d);
            }
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            if(front() != null){
                Draw.color(lastSignal ? Pal.accent : Color.white);
                Lines.square(front().x, front().y, 4 * front().block.size);
            }
        }

        @Override
        public boolean signal() {
            return getSignal(nb.get(1), this) | getSignal(nb.get(2), this) | getSignal(nb.get(3), this);
        }

        @Override
        public boolean signalFront() {
            return lastSignal;
        }
    }
}
