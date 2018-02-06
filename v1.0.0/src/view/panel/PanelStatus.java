package view.panel;

import java.awt.*;
import javax.swing.*;

/** painel inferior responsável por mostrar algumas informações de forma rapida
 *  implementa o padrão de projeto de software Singleton.
 */
public class PanelStatus extends JPanel{
   
    /**
     * @return a única instância da classe */
    public static PanelStatus getUnit(){
            if(unit == null)
                    unit = new PanelStatus();
            return unit;
    } // getUnit
    
    /** constrói o painel */
    private PanelStatus(){
            setLayout(new GridLayout(0, 3));
            setFocusable(true);
            add(info);
            add(action);
            add(cursor);
    }

    /** atualiza a informação do campo info
     * @param a nova informação */
    public void setInfo(String a){
            info.setText(a);
    }// setInfo

    /** atualiza a informação do campo action
     * @param a nova informação */    
    public void setAction(String a){
            action.setText(a);
    }// setAction

    /** atualiza a informação do campo cursor
     * @param a nova informação */    
    public void setCursor(String a){
            cursor.setText(a);
    }// setCursor

    /** única instância da classe */
    private static PanelStatus unit;
    /** labels de informação do painel de status */
    private JLabel info = new JLabel("info"), action = new JLabel("action"), cursor = new JLabel("cursor");

}// PanelStatus