package esoterum.world.blocks.binary;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;

public class LogicGraph {
    public Seq<BinaryBlock.BinaryBuild> members = new Seq<>();
    public Seq<BinaryBlock.BinaryBuild> inputs = new Seq<>();
    public int power = 0;
    public Color color;

    public LogicGraph(){
        color = new Color(Mathf.random(0, 255) / 255f, Mathf.random(0, 255) / 255f, Mathf.random(0, 255) / 255f);
    }
}
