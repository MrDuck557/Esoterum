package esoterum.interfaces;

import arc.Core;
import arc.util.Log;
import esoterum.util.EsoUtil;
import esoterum.world.blocks.binary.*;
import mindustry.gen.*;


public interface Binaryc {
    default boolean signal(){
        return false;
    }

    default boolean signalFront(){
        return false;
    }
    default boolean signalLeft(){
        return false;
    }
    default boolean signalRight(){
        return false;
    }
    default boolean signalBack(){
        return false;
    }

    // get relative direction of "To" from "From"'s perspective then get the associated signal output.
    default boolean getSignalRelativeTo(BinaryBlock.BinaryBuild from, BinaryBlock.BinaryBuild to){
        // if the building does not emit signals, return false.
        if(!from.emits())return false;

        return switch(EsoUtil.relativeDirection(from, to)){
            case 0 -> from.signalFront();
            case 1 -> from.signalLeft();
            case 2 -> from.signalBack();
            case 3 -> from.signalRight();
            default -> false;
        };
    }

    // this still hurts me.
    // check code taken from esoterum v1-1.3 because i don't know how the fuck i did this
    default boolean connectionCheck(Building from, BinaryBlock.BinaryBuild to){
        if(from instanceof BinaryBlock.BinaryBuild b){
            return !b.block.rotate
                || (b.front() == to || b.back() == to)
                || to.front() == b
                && !(b.back() == this && to.front() != b);
        }
        return false;
    }

    // thanks sk
    default boolean getSignal(Building from, BinaryBlock.BinaryBuild to){
        if(from instanceof BinaryBlock.BinaryBuild b){
            if(Core.graphics.getFrameId() == to.getLastFrame()) return to.getLastGet();
            to.setLastFrame(Core.graphics.getFrameId());
            to.setLastGet(getSignalRelativeTo(b, to));
            return getSignalRelativeTo(b, to);
        }
        return false;
    }

    default BinaryBlock.BinaryBuild checkType(Building b){
        if(b instanceof BinaryBlock.BinaryBuild bb)return bb;
        return null;
    }
}
