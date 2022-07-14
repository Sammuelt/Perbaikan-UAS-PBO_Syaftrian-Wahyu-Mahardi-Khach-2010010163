import frame.IkanViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
    Koneksi.getConnection();
    IkanViewFrame viewFrame = new IkanViewFrame();
    viewFrame.setVisible(true);
}
}
