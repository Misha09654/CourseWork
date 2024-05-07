import javax.swing.JFrame;

public class StegoApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("StegoForm");
        frame.setContentPane(new StegoForm().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}