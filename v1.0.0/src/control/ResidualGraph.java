/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Graph;
import model.Vertex;
import observer.ObservableGraphMap;
import observer.ObserverGraphMapPaint;
import model.GraphMap;
import view.panel.Panel;
import view.panel.PanelStatus;
import thread.ThreadAlgorithm;
import thread.ThreadPaint;

/** Classe de instância única para o grafo residual, utilizado no algortimo de fluxo máximo de Edmonds-Karp.
 * @link https://en.wikipedia.org/wiki/Maximum_flow_problem
 * @link https://en.wikipedia.org/wiki/Flow_network
 */
public class ResidualGraph{

	/** Método que retorna a instância única da classe. 
	  * @return ResidualGraph - a instância única da classe
	  */
    public static ResidualGraph getUnit(){
        if(unit == null)
            unit = new ResidualGraph();
        return unit;
    }
    
    /**
     * recupera a flag que indica se o algoritmo está executando
     * @return flag que indica se o algoritmo está executando
      */
    public boolean isRunning(){
        return isRunning;
    }
    
    /**
     * configura a flag de execução do algoritmo.
     * @param b novo valor da flag.
     */
    public void setRunning(boolean b){
        isRunning = b;
    }
    
    /** Construtor padrão da classe. */
    private ResidualGraph() {

    }

	/** Método que configura o fluxo de todas as arestas para zero. */
    public void startFlow(){
        set = Graph.getUnit().getV();
        GraphMap.getUnit().setAlgorithmRunning(true);
        for(Vertex u : set){
            for (Vertex v : u.getAdj()) {
                u.putFlow(v,0);
            }
            
        }
    }
    
    /** 
     * configura a flag de interrupção
     * 
     * @param b novo valor para a flag
      */
    public void setInterruption(boolean b){
        interruption = b;
    }
    
    /** Método que roda o algoritmo de fluxo máximo de Edmonds-Karp
     * @throws java.lang.InterruptedException
      * @link https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm
      * @param s - Vértice fonte
      * @param t - Vértice sumidouro(destino)
      */
    public void Edmonds_Karp(Vertex s, Vertex t) throws InterruptedException{
        startFlow();
        
        end = t;
        Vertex temp, pred;
        int capac;
        isRunning = true;
        
        while(isAugmentedPath){
            minimumCapacity = Integer.MAX_VALUE;
            try{
                thread = new ThreadAlgorithm(s, 6);
                thread.getThread().join();
            } catch (InterruptedException ex){
                JOptionPane.showMessageDialog(null, "Error Thread");
            }
            
            
            if(interruption){
                interruption = !interruption;
                throw new InterruptedException();
            }
            
            temp = t;
            while(temp.getPredecessor() != null){
                pred = temp.getPredecessor();
                capac = pred.getWeight(temp) - pred.getFlow(temp);
                System.out.print(capac + " →");
                if(capac < minimumCapacity)
                    minimumCapacity = capac;
                temp = pred;
            
            }
            
            temp = t;
            while(temp.getPredecessor() != null){
                pred = temp.getPredecessor();
                pred.putFlow(temp, minimumCapacity + pred.getFlow(temp));
                temp = pred;                    
            }
            

        }// while
        resetVertexPainting();
        getMaximumFlowValue(s);
        Panel.getUnit().repaint();
    }
    
    /**
     * calcula o fluxo máximo da rede residual.
     * @param u vertice base para se calcular.
      */
    public void getMaximumFlowValue(Vertex u){
        PanelStatus sp = PanelStatus.getUnit();
        int flow = 0;
        for(Vertex x : u.getAdj()){
            flow += u.getFlow(x);
        }
        sp.setAction("Maximum Flow Value = " + flow);
    }
    
    /** Após executar o algoritmo de Edmonds-Karp, o grafo precisa ser
     *  configurado para se retirar toda a interface gráfica da última
     *  execução do BFS sobre o grafo.
     */
    public void resetVertexPainting(){
        Map<String, Integer> map = GraphMap.getUnit().getMapSelection();
        Set<String> subset = map.keySet();
        isAugmentedPath = true;
        
        for(String s : subset){
            map.put(s, 1);
        }
    }
    
    /** envia a notificação de alteração gráfica dos vértices e também
     *  trata a thread de pintura dos vértices.
     * @param o observa e aguarda modificaçao de um vertice para tomar as devidas
     *          alteraçoes.
     * @param ob observavel que no caso recebe o vertice como parametro
     * @param color nova cor do vertice
     * @param u Vertice alvo de modificaçao
     * @throws java.lang.InterruptedException termina a execuçao do algoritmo
     * quando lnçada
     */
    public void handleThread(ObserverGraphMapPaint o, ObservableGraphMap ob, int color, Vertex u)
    throws InterruptedException{
        o.setColor(color);
        ob.setState(u);
        ob.send();
        ThreadPaint.getUnit().run();
        if(interruption){
            throw new InterruptedException();
        }
    }            

	/** Método que roda o BFS no grafo residual.
	  * @param s - Vértice fonte
	  */
    public void BFS(Vertex s) throws InterruptedException{
        Vertex v, u;
        int i,j;

        ObservableGraphMap obs = new ObservableGraphMap(null);
        ObserverGraphMapPaint ob = new ObserverGraphMapPaint(3);
        obs.attach(ob);

        LinkedList<Vertex> F;
        GraphMap.getUnit().setAlgorithmRunning(true);

        for (Vertex x : set){
            x.setPredecessor(null);
            x.setDistance(-1); //-1 representa infinito
            x.setColor(0);
            PanelStatus.getUnit().setAction("Color ["+ x +"] = white");
            handleThread(ob, obs, 3, x);
        }

        s.setDistance(0);
        s.setColor(2);
        PanelStatus.getUnit().setAction("Color ["+ s +"] = black");
        handleThread(ob, obs, 5, s);

        F = new LinkedList<Vertex>();
        F.add(s);

        try{
        int capacity;
        ListIterator iter;
        while(!F.isEmpty())
        {
            u = F.removeFirst();
            iter = u.getAdj().listIterator();
            while(iter.hasNext())
            {
                v = (Vertex)iter.next();
                capacity = u.getWeight(v) - u.getFlow(v);
                if(v.getColor()==0 && capacity > 0 && u.getColor() != 0)
                {
                    v.setColor(2);
                    v.setPredecessor(u);
                    v.setDistance(u.getDistance() + 1);
                    F.add(v);
                    PanelStatus.getUnit().setAction("Color ["+ v +"] = black; d[" + v + "] = " + v.getDistance());
                    handleThread(ob, obs, 5, v);
                }
            }
            u.setColor(2);
            PanelStatus.getUnit().setAction("Color ["+ u +"] = black");
            handleThread(ob, obs, 5, u);
        }
        
        if(end != null && end.getColor() == 0){
            isAugmentedPath = false;
        } else{
            isAugmentedPath = true;            
        }
        
        } catch(NullPointerException ex){
            isAugmentedPath = false;
        }
        
    }
	
	/** Instância do grafo residual. */
    private static ResidualGraph unit; 
    
    /** Conjunto de vertices do grafo */   
    ArrayList<Vertex> set = Graph.getUnit().getV();
    
    /** Atributo que diz se o grafo residual possui caminho aumentante entre a fonte e o sumidouro.*/
    private boolean isAugmentedPath = true;
    
    /** flag que indica se um o algoritmo esta em execuçao */
    private boolean isRunning = false;
    
    /** capacidade minima suportada calculada toda vez que se acha um caminho aumentante */
    private int minimumCapacity = Algorithm.INF;
    
    /** vertice final de um caminho aumentante, usado para percorrer o caminho
     *  aumentante no sentido inverso para verificar se o caminho existe
     */
    private Vertex end;
    
    /** thread de execuçao do algoritmo */
    private ThreadAlgorithm thread;    
    
    /** flag utilizada para avisar se houve interrupçao do algoritmo */
    private boolean interruption;    
}
