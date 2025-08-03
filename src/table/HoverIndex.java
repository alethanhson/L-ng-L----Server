package table;


public class HoverIndex {

    javax.swing.JFrame frmMain;
    public static HoverIndex gI;
    HoverIndex(javax.swing.JFrame frmMain) {
        this.frmMain = frmMain;
        gI = this;
    }

    public int getIndex() {
      
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public  int index = -1;
}
