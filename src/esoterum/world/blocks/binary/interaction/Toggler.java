package esoterum.world.blocks.binary.interaction;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import esoterum.world.blocks.binary.*;
import mindustry.logic.*;

public class Toggler extends BinaryBlock{
    public TextureRegion[] states = new TextureRegion[2];

    public Toggler(String name){
        super(name);
        rotate = true;
        drawRot = false;
        rotatedBase = true;
        baseType = 1;
        emits = true;
        outputs = new boolean[]{true, false, false, false};
        inputs = new boolean[]{false, true, true, true};
        propagates = true;
    }

    @Override
    public void load(){
        super.load();
        states[0] = Core.atlas.find(name + "-off");
        states[1] = Core.atlas.find(name + "-on");
        topRegion = states[0];
    }

    public class TogglerBuild extends BinaryBuild{
        @Override
        public void updateSignal(){
            signal[0] = getSignal(nb[1], this) | getSignal(nb[2], this) | getSignal(nb[3], this);
            if(front() != null) front().control(LAccess.enabled, Mathf.num(signal()), 0d, 0d, 0d);
        }

        @Override
        public void draw(){
            drawBase();
            drawConnections();
            Draw.color();
            Draw.rect(states[Mathf.num(signal())], x, y, (rotate && drawRot) ? rotdeg() : 0f);
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            if(front() != null){
                Draw.color(signal() ? team.color : Color.white);
                Lines.square(front().x, front().y, 4 * front().block.size);
            }
        }
    }
}
