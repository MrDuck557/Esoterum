package esoterum.world.blocks.binary.transmission;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import esoterum.world.blocks.binary.*;

// too similar to BinaryRouter?
public class BinaryCJunction extends BinaryBlock{
    public TextureRegion[] directionRegions = new TextureRegion[2];

    public BinaryCJunction(String name){
        super(name);
        emits = true;
        inputs = new boolean[]{true, true, true, true};
        outputs = new boolean[]{true, true, true, true};
        propagates = true;
        rotate = true;
    }

    @Override
    public void load(){
        super.load();

        for(int i = 0; i < 2; i++){
            directionRegions[i] = Core.atlas.find(name + "-direction-" + i);
        }
    }

    @Override
    protected TextureRegion[] icons(){
        return new TextureRegion[]{
            region,
            topRegion,
            directionRegions[0],
            directionRegions[1]
        };
    }

    public class BinaryCJunctionBuild extends BinaryBuild {
        @Override
        public void updateSignal(){
            signal[0] = getSignal(nb[1], this);
            signal[1] = getSignal(nb[0], this);
            signal[2] = getSignal(nb[3], this);
            signal[3] = getSignal(nb[2], this);
        }

        @Override
        public void drawConnections(){
            Draw.color(Color.white, team.color, Mathf.num(signal[2] || signal[3]));
            Draw.rect(directionRegions[0], x, y, rotdeg());
            Draw.color(Color.white, team.color, Mathf.num(signal[0] || signal[1]));
            Draw.rect(directionRegions[1], x, y, rotdeg());
        }
    }
}