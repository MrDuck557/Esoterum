package esoterum.world.blocks.binary.transmission;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import esoterum.world.blocks.binary.*;

// too similar to BinaryRouter?
public class BinaryJunction extends BinaryBlock{
    public TextureRegion[] directionRegions = new TextureRegion[2];

    public BinaryJunction(String name){
        super(name);
        emits = true;
        inputs = new boolean[]{true, true, true, true};
        outputs = new boolean[]{true, true, true, true};
        propagates = false;
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

    public class BinaryJunctionBuild extends BinaryBuild{
        @Override
        public BinaryBuild[] getNeighbours(int dir){
            BinaryBuild[] nbs = new BinaryBuild[]{null, null, null, null};
            if(nb.isEmpty()) return nbs;
            for(int i=0;i<4;i++){
                if((i + 2) % 4 == dir && outputs(i) && nb.get(i) != null && connectionCheck(this, nb.get(i))) nbs[i] = nb.get(i);
            }
            return nbs;
        }

        @Override
        public void updateSignal(){
            if(nb.isEmpty()) return;
            signal[0] = getSignal(nb.get(2), this);
            signal[1] = getSignal(nb.get(3), this);
            signal[2] = getSignal(nb.get(0), this);
            signal[3] = getSignal(nb.get(1), this);
        }

        @Override
        public void drawConnections(){
            Draw.color(Color.white, team.color, Mathf.num(signal[0] || signal[2]));
            Draw.rect(directionRegions[0], x, y);
            Draw.color(Color.white, team.color, Mathf.num(signal[1] || signal[3]));
            Draw.rect(directionRegions[1], x, y);
        }
    }
}
