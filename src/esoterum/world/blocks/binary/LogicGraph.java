package esoterum.world.blocks.binary;

import arc.struct.Seq;

public class LogicGraph {
    public Seq<BinaryBlock.BinaryBuild> members = new Seq<>();
    public Seq<BinaryBlock.BinaryBuild> inputs = new Seq<>();
    public int power = 0;
}
