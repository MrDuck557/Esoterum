package esoterum.world.blocks.binary;

import arc.math.Mathf;
import arc.util.*;
import arc.util.io.*;

public class BinaryBuffer extends LogicGate{
    public BinaryBuffer(String name){
        super(name);
        single = true;
    }

    public class BinaryBufferBuild extends LogicGateBuild{
        public float delayTimer = 0f;

        // 0.5 seconds
        // TODO make configurable
        public float delay = 30;

        @Override
        public void updateTile() {
            if(signal()){
                delayTimer += Time.delta;
            }else{
                delayTimer -= Time.delta;
            }

            // this looks terrible
            if(delayTimer > delay){
                lastSignal  = true;
                delayTimer = delay;
            }
            if(delayTimer < 0f){
                lastSignal = false;
                delayTimer = 0f;
            }
        }

        @Override
        public boolean signal() {
            return getSignal(nb.get(configs.first()), this);
        }

        @Override
        public boolean signalFront() {
            return lastSignal;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 1){
                delayTimer = read.f();
                Log.info(delayTimer);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(delayTimer);
        }

        @Override
        public byte version() {
            return 1;
        }
    }
}
