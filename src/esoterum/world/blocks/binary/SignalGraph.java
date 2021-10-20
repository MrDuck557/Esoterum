package esoterum.world.blocks.binary;

import java.util.*;

import esoterum.util.EsoUtil;

public class SignalGraph {
    public static HashMap<BinaryBlock.BinaryBuild, HashSet<BinaryBlock.BinaryBuild>> hm = new HashMap<>();
    public static HashSet<BinaryBlock.BinaryBuild> sources = new HashSet<>();

    public static void addVertex(BinaryBlock.BinaryBuild b){
        hm.put(b, new HashSet<>());
        if(!b.propagates()) sources.add(b);
    }

    public static void addEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).add(b);
        }
    }

    public static void removeVertex(BinaryBlock.BinaryBuild b){
        hm.remove(b);
    }

    public static void removeEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).remove(b);
        }
    }

    public static void dfs(BinaryBlock.BinaryBuild b){
        Stack<BinaryBlock.BinaryBuild> s = new Stack<>();
        HashMap<BinaryBlock.BinaryBuild, Integer> h = new HashMap<>();
        s.push(b);
        BinaryBlock.BinaryBuild p;
        while(!s.isEmpty()){
            p = b;
            b = s.pop();
            updateSignal(b);
            int dir = EsoUtil.relativeDirection(b, p);
            if(h.get(b) == null || h.get(b) != dir){
                h.put(b, dir);
                for(BinaryBlock.BinaryBuild bb : hm.get(b)){
                    s.push(bb);
                }
            }
        }
    }

    public static void update(){
        Thread t = new Thread(){
            @Override
            public void run(){
                for(BinaryBlock.BinaryBuild b : sources){
                    dfs(b);
                }
            }
        };
        t.start();
        try {t.join();} catch (InterruptedException e) {e.printStackTrace();}
    }

    public static void updateSignal(BinaryBlock.BinaryBuild b){
        b.updateSignal();
    }
}
