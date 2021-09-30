package esoterum.world.blocks.binary;

import arc.util.*;
import arc.util.io.*;

public class BinaryMonostable extends BinaryBuffer{
    public BinaryMonostable(String name){
        super(name);
    }

    public class BinaryMonostableBuild extends BinaryBufferBuild{
        public boolean on;

        @Override
        public void updateTile(){
            if(!on && signal()){
                on = true;
                delayTimer = trueDelay();
            }else if(!signal()){
                on = false;
                delayTimer = 0f;
            }

            delayTimer -= Time.delta;

            lastSignal = delayTimer > 0 && signal();
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.bool(on);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            on = read.bool();
        }
    }
}
