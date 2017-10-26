import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Bombs extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;

    // Labels to display game info
    public JLabel timerLabel, bombsLeftLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Game timer: will be configured to trigger an event every second
    private int gameTime = 0;
    private Timer gameTimer;


    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();


    private int toWin, height, width, numBombs;
    private boolean gameWon, gameLost;
    private Icon smiley = new ImageIcon(loader.getResource("res/facesmile.png"));
    private JRadioButtonMenuItem setupItems[]; // setup menu items
    private ButtonGroup setupButtonGroup; // manages setup menu items
    JButton restart;


    public Bombs() {
        Bombs M = new Bombs(5, 5, 5);
    }

    public Bombs(int h, int w, int n)
    {
        // Call the base class constructor
        super("Minesweeper");

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        createMenus();

        height = h;
        width = w;
        numBombs = n;
        toWin = h * w - n;

        bombsLeftLabel = new JLabel("Bombs: " + numBombs);
        restart = new JButton(smiley);
        restart.addActionListener(this);
        timerLabel = new JLabel("Timer: 0");

        ActionListener seconds = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameTime += 1;
                timerLabel.setText("Timer:  " + gameTime);
            }
        };

        gameTimer = new Timer(1000, seconds);

        labelView = new JPanel();
        labelView.setLayout(new GridLayout(1, 3, 0, 0));

        labelView.add(bombsLeftLabel);
        labelView.add(restart);
        labelView.add(timerLabel);

        boardView = new JPanel();  // used to hold game board
        boardView.setLayout(new GridLayout(height, width, 0, 0));

        gameBoard = new Board(height, width, numBombs, this, bombsLeftLabel);
        gameBoard.bombsLeft = numBombs;

        gameBoard.fillBoardView(boardView);

        Container c = getContentPane();
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(50*width, 50*height+100);
        setVisible(true);
    }

    public void createMenus(){
    JMenu gameMenu = new JMenu( "Game" ); // create game menu
    gameMenu.setMnemonic( 'G' ); // set mnemonic to G

    // create New menu item
    JMenuItem newItem = new JMenuItem( "New" );
    newItem.setMnemonic( 'N' ); // set mnemonic to N
    gameMenu.add( newItem ); // add New item to game menu
    newItem.addActionListener(this);

    String difficulty[] = { "Beginner", "Intermediate", "Expert", "Custom" };

    JMenu setupMenu = new JMenu( "Setup" ); // create setup menu
    setupMenu.setMnemonic( 'S' ); // set mnemonic to C

    // create radiobutton menu items for difficulty
    setupItems = new JRadioButtonMenuItem[ difficulty.length ];
    setupButtonGroup = new ButtonGroup(); // manages difficulty


    // create radio button menu items
    for ( int count = 0; count < difficulty.length; count++ )
    {
        setupItems[ count ] = new JRadioButtonMenuItem( difficulty[ count ] ); // create item
        setupMenu.add( setupItems[ count ] ); // add item to menu
        setupButtonGroup.add( setupItems[ count ] ); // add to group
        setupItems[ count ].addActionListener( this );
    } // end for

    gameMenu.add( setupMenu );
    gameMenu.addSeparator(); // add separator in menu

    JMenuItem exitItem = new JMenuItem( "Exit" ); // create exit item
    exitItem.setMnemonic( 'x' ); // set mnemonic to x
    gameMenu.add( exitItem ); // add exit item to file menu
    exitItem.addActionListener(this);


    JMenuItem helpItem = new JMenuItem( "Help" ); // create help menu
    helpItem.setMnemonic( 'H' ); // set mnemonic to H
    helpItem.addActionListener(this);

    JMenuBar bar = new JMenuBar(); // create menu bar
    setJMenuBar( bar ); // add menu bar to application
    bar.add( gameMenu );
    bar.add( helpItem );
}

    public void customMenu(){
        JFrame popup = new JFrame();
        popup.setSize( 250, 200 ); // set frame size
        popup.setVisible( true ); // display frame

        JTabbedPane tabbedPane = new JTabbedPane(); // create JTabbedPane
        JButton enterButton = new JButton("Enter");

        //________________set up pane11 and add it to JTabbedPane_______________________________________________________
        JLabel label1 = new JLabel( "Height (5-10)", SwingConstants.CENTER);

        SpinnerNumberModel heightModel = new SpinnerNumberModel(5, 5,10,1);
        JSpinner heightSpinner = new JSpinner(heightModel);
        Font bigFont = heightSpinner.getFont().deriveFont(Font.PLAIN, 25f);
        heightSpinner.setFont(bigFont);
        label1.setFont(bigFont);

        JPanel panel1 = new JPanel(); // create first panel
        panel1.add( label1 ); // add label to panel
        panel1.add( heightSpinner );
        tabbedPane.addTab( "Height", null, panel1, "First Panel" );

        //________________set up panel2 and add it to JTabbedPane_______________________________________________________
        JLabel label2 = new JLabel( "Width (5-10)", SwingConstants.CENTER );

        SpinnerNumberModel widthModel = new SpinnerNumberModel(5, 5,10,1);
        JSpinner widthSpinner = new JSpinner(widthModel);
        widthSpinner.setFont(bigFont);
        label2.setFont(bigFont);

        JPanel panel2 = new JPanel(); // create second panel
        panel2.add( label2 ); // add label to panel
        panel2.add( widthSpinner );
        tabbedPane.addTab( "Width", null, panel2, "Second Panel" );

        //________________set up panel3 and add it to JTabbedPane_______________________________________________________
        JLabel label3 = new JLabel( "Bomb#", SwingConstants.CENTER);

        SpinnerNumberModel bombModel = new SpinnerNumberModel(5, 5,10,1);
        JSpinner bombSpinner = new JSpinner(bombModel);
        bombSpinner.setFont(bigFont);
        label3.setFont(bigFont);

        JPanel panel3 = new JPanel(); // create third panel
        panel3.add( label3, BorderLayout.CENTER ); // add label to panel
        panel3.add( bombSpinner );
        panel3.add( enterButton );
        tabbedPane.addTab( "Bomb#", null, panel3, "Third Panel");

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int h = (Integer) heightSpinner.getValue();
                int w = (Integer) widthSpinner.getValue();
                if (tabbedPane.getSelectedIndex() == 2){
                    SpinnerNumberModel bombModel = new SpinnerNumberModel(5, 1,(h*w)/2,1);
                    JSpinner bombSpinner = new JSpinner(bombModel);
                    bombSpinner.setFont(bigFont);
                    panel3.removeAll();
                    panel3.add( label3, BorderLayout.CENTER ); // add label to panel
                    panel3.add( bombSpinner );
                    panel3.add( enterButton );
                }
            }
        });

        enterButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int h = (Integer) heightSpinner.getValue();
                int w = (Integer) widthSpinner.getValue();
                int b = (Integer) bombSpinner.getValue();
                reset( h, w, b);
                popup.dispose();
            }
        });

        popup.add( tabbedPane ); // add JTabbedPane to frame
    }

    public void helpMenu(){
        JOptionPane.showMessageDialog( Bombs.this,
            "Left-click an empty square to reveal it.\n" +
                    "Right-click an empty square to flag it.\n" +
                    "Open all squares without bombs to win.",
            "Help", JOptionPane.PLAIN_MESSAGE );
    }


    /* Handle anything that gets clicked and that uses Bombs as an ActionListener */

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("Exit")){ System.exit(0); }

        else if (e.getActionCommand().equals("New") || (e.getSource().equals(restart))){ reset(height, width, numBombs);}

        else if (e.getActionCommand().equals("Beginner")){ reset(5,5,5); }
        else if (e.getActionCommand().equals("Intermediate")){ reset(8, 8, 15);}
        else if (e.getActionCommand().equals("Expert")){ reset(10, 10, 30);}
        else if (e.getActionCommand().equals("Custom")){ customMenu(); }

        else if (e.getActionCommand().equals("Help")){ helpMenu(); }

        else{
            // Get the currently clicked card from a click event
            FlippableButton currCard = (FlippableButton)e.getSource();
            int x = currCard.getI();
            int y = currCard.getJ();
            gameTimer.start();

            if (currCard.id() == -1 && gameWon == false){ lose(); }

            else if (currCard.id() > 0 && currCard.id() < 9){
                currCard.showFront();
                currCard.removeActionListener(this);
            }

            else if (currCard.id() == 0){ gameBoard.reveal(x, y, this); }


            if (toWin == currCard.revealCount && gameTimer.isRunning() && gameLost == false) { win(); }
        }

    }

    public void reset(int h, int w, int n){
        dispose();
        new Bombs(h, w, n);
    }

    public void win(){
        gameWon = true;
        gameTimer.stop();
        JOptionPane.showMessageDialog( Bombs.this,"YOU WIN!","Game Complete", JOptionPane.PLAIN_MESSAGE );
    }

    public void lose(){
        gameLost = true;
        gameBoard.revealBombs(this);
        gameTimer.stop();
        JOptionPane.showMessageDialog( Bombs.this,"YOU LOSE!","Game Over", JOptionPane.PLAIN_MESSAGE );
    }


    public static void main(String args[])
    {
        Bombs M = new Bombs();
    }
}

