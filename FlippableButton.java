import javax.swing.*;


public class FlippableButton extends JButton
{
    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    // Card front icons
    private Icon front;
    private Icon back = new ImageIcon(loader.getResource("res/blank.png"));
    private Icon flag = new ImageIcon(loader.getResource("res/flag.png"));

    // ID + Location (i, j)
    private int id, i, j;

    // Is current card been revealed?
    boolean revealed;
    public static int revealCount;

    // Default constructor
    public FlippableButton() {
        super();
        super.setIcon(back);
        id = 0;
    }

    // Constructor with card front initialization
    public FlippableButton(int x, int y, int k)
    {
        super();
        i = x;
        j = y;
        id = k;

        String imgPath = "res/" + id + ".png";
        //System.out.println(imgPath);
        ImageIcon img = new ImageIcon(loader.getResource(imgPath));
        front = img;

        super.setIcon(back);
    }


    // Card flipping functions
    public void showFront() { /* To-Do: Show the card front */ super.setIcon(front); revealed = true; revealCount++; }
    public void showFlag() { /* To-Do: Show the card front */ super.setIcon(flag); }
    public void showBack() { /* To-Do: Show the card front */ super.setIcon(back); }


    // Metadata: ID number
    public int id() { return id; }
    public void setID(int i) { id = i; }
    public int getI() { return i; }
    public int getJ() { return j; }

}
