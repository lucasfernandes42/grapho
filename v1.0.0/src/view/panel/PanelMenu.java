package view.panel;

import control.FrameControl;
import control.ResidualGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import model.Graph;
import view.Frame;
import view.FrameEdge;
import model.GraphMap;
import view.FrameEdgeEdit;
import view.FrameSave;
import view.FrameSpeed;
import view.input.SelectionHandler;

/** barra de menu superior da janela principal
 *  implementa o padrão de projeto de software Singleton.
 */
public class PanelMenu extends JMenuBar{
    
    /**
     * @return a única instância da classe  */
    public static PanelMenu getUnit(){
        if(unit == null){
            unit = new PanelMenu();
        }
        return unit;
    }// getUnit 

    /** cria o menu. */
    private PanelMenu(){
        setFocusable(true);
        for(int index = 0 ; index < menu.length ; index++){
            menuBar[index] = new JMenu(menu[index]);
            add(menuBar[index]);
        }
        
        for(int index = 0 ; index < menuItem.length ; index++){
            menuItemBar[index] = new JMenuItem(menuItem[index]);
            menuItemBar[index].addActionListener(handler);
        }
        
        menuBar[0].add(menuItemBar[0]); menuBar[0].add(menuItemBar[1]);
        menuBar[0].add(menuItemBar[2]); menuBar[0].add(menuItemBar[3]);
        
        menuBar[1].add(menuItemBar[4]); menuBar[1].add(menuItemBar[5]);
        menuBar[1].add(menuItemBar[6]); menuBar[1].add(menuItemBar[7]);
        
        menuBar[2].add(menuItemBar[8]); menuBar[2].add(menuItemBar[9]);
        menuBar[2].add(menuItemBar[10]); menuBar[2].add(menuItemBar[11]);
        menuBar[2].add(menuItemBar[12]);
        
        menuBar[3].add(menuItemBar[13]); menuBar[3].add(menuItemBar[14]);
        menuBar[3].add(menuItemBar[15]); menuBar[3].add(menuItemBar[16]);
        
        if(!Graph.getUnit().isWeighted()){
            menuItemBar[10].setEnabled(false); menuItemBar[11].setEnabled(false);
            menuItemBar[12].setEnabled(false);
        }
        
        
    }// constructor
    
    /** desativa parte do menu durante a execução de um algoritmo. */
    public void disableButtons(){
            menuBar[1].setEnabled(false); menuBar[3].setEnabled(false);
            menuBar[2].add(menuItemBar[17]); menuBar[2].add(menuItemBar[18]);
            menuBar[2].add(menuItemBar[19]);
            menuItemBar[17].setEnabled(false);
    }  
    
    /** mantém certos botões de algoritmos ativados na condição
     *  do grafo ser ponderado ou não.
     */
    public void disableAlgorithms(){
            if(Graph.getUnit().isWeighted()){
                    menuItemBar[8].setEnabled(false); menuItemBar[9].setEnabled(false);
                    menuItemBar[10].setEnabled(true); menuItemBar[11].setEnabled(true); menuItemBar[12].setEnabled(true);
                    PanelStatus.getUnit().setAction("weighted graph");
            } else {
                    menuItemBar[8].setEnabled(true); menuItemBar[9].setEnabled(true);
                    menuItemBar[10].setEnabled(false); menuItemBar[11].setEnabled(false); menuItemBar[12].setEnabled(false);
                    PanelStatus.getUnit().setAction("nonweighted graph");
            }
    }    

    /** ativa parte do menu durante a execução de um algoritmo. */
    public void enableButtons(){
        menuBar[2].remove(menuItemBar[17]);
        menuBar[2].remove(menuItemBar[18]); menuBar[2].remove(menuItemBar[19]);
        menuBar[1].setEnabled(true); menuBar[3].setEnabled(true);
        GraphMap.getUnit().setAlgorithmRunning(false);
        ResidualGraph.getUnit().setRunning(false);
        GraphMap.getUnit().setShowPredecessor(false);
        SelectionHandler.getUnit().resetSelection();
        FrameControl.interruptAlgorithm();            
        PanelLeft.getUnit().disableButtonsAlgorithm(true);
    }
    
    /** classe privada que trata os eventos dos menu's */
    private class MenuHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
           Graph graph = Graph.getUnit();
           Frame f = Frame.getUnit();
           //file
           if(e.getSource() == menuItemBar[0]){
                new FrameSave(0);
           }
           if(e.getSource() == menuItemBar[1])
                f.save();
           if(e.getSource() == menuItemBar[2]){
               new FrameSave(2);
           }
           if(e.getSource() == menuItemBar[3]){
                   System.exit(0);
           }

           // edit
           if(e.getSource() == menuItemBar[4]){
                   f.addVertex();
           }// edit1

           if(e.getSource() == menuItemBar[5]){
                   f.removeVertex();
           }// edit2

           if(e.getSource() == menuItemBar[6]){
                if(!Graph.getUnit().isWeighted())
                    new FrameEdge(1);
                else
                    new FrameEdgeEdit(2);
           }// edit3

           if(e.getSource() == menuItemBar[7]){
                   new FrameEdge(2);
           }// edit4


           // alg
           if(e.getSource() == menuItemBar[8]){
               f.bfs();
           }
           if(e.getSource() == menuItemBar[9]){
               FrameControl.playDFS();
           }

           if(e.getSource() == menuItemBar[10])
                   f.prim();

           if(e.getSource() == menuItemBar[11])
                f.dijkstra();
           if(e.getSource() == menuItemBar[12]){
               disableButtons();
               new FrameEdge(3);   
           }

           if(e.getSource() == menuItemBar[18]){
                GraphMap.getUnit().setShowPredecessor(!GraphMap.getUnit().getShowPredecessor());
           }
           if(e.getSource() == menuItemBar[19]){
               enableButtons();
           }

           // config
           if(e.getSource() == menuItemBar[13]){
               new FrameSave(1);
           }
           if(e.getSource() == menuItemBar[14]){
               new FrameSave(3);
               
           }
           if(e.getSource() == menuItemBar[15]){
               new FrameSave(4);
           }
           if(e.getSource() == menuItemBar[16]){
               new FrameSpeed();
           }

           Panel.getUnit().repaint();
           f.requestFocus();
        }
    }

    /** unica instancia da classe */
    private static PanelMenu unit;
    /** nome das listas do menu */
    private String menu[] = {"file", "edit", "algorithm", "configuration"};
    /** nome dos itens das listas de menu */
    private String menuItem[] = {"new graph", "save graph", "load graph", "exit",
                                "add vertice", "remove vertice", "add edge",
                                "remove edge", "play BFS", "play DFS", "play Prim",
                                "play Dijkstra", "play Edmonds-Karp",
                                "generate random graph", "digraph / simple graph",
                                "weighted / nonweighted graph", "set algorithm speed", "----------",
                                "show / hide predecessor subgraph", "finish algorithm"};
    /** contem as listas do menu */
    private JMenu menuBar[] = new JMenu[menu.length];
    /** item das listas de menu */
    private JMenuItem menuItemBar[] = new JMenuItem[menuItem.length];
    /** tratador de eventos */
    private MenuHandler handler = new MenuHandler();
    
}
