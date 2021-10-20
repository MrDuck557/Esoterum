package esoterum.world.blocks.binary;

import java.util.*;

import esoterum.util.EsoUtil;

public class SignalGraphBlock {
    public HashMap<BinaryBlock.BinaryBuild, HashSet<BinaryBlock.BinaryBuild>> hm = new HashMap<>();
    public HashSet<BinaryBlock.BinaryBuild> sources = new HashSet<>();

    public void addVertex(BinaryBlock.BinaryBuild b){
        hm.put(b, new HashSet<>());
        if(!b.propagates()) sources.add(b);
    }

    public void addEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).add(b);
        }
    }

    public void removeVertex(BinaryBlock.BinaryBuild b){
        hm.remove(b);
    }

    public void removeEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).remove(b);
        }
    }

    public void dfs(BinaryBlock.BinaryBuild b){
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

    public void update(){
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

    public void updateSignal(BinaryBlock.BinaryBuild b){

    }
}
