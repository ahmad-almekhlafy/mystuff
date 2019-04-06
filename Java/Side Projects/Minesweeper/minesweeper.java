import javax.swing.*;
import java.io.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

class minesweeper {

    public static void main(String[] args) {
        optionDialog optionDialog = new optionDialog();
        mainDialog mainDialog = new mainDialog(12, 11, 10, optionDialog);
        optionDialog.mainDialog = mainDialog;

    }
}

class mainDialog extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    int cellSize = 20;
    int frameHeight;
    int frameWidth;

    int playFieldHeight;
    int playFieldWidth;
    int cellsPerRow;
    int numOfRows;

    Random r = new Random();
    Timer timer = new Timer(20, this);
    int numOfMines;
    int secondsCounter;

    JFrame frame = new JFrame("Minesweeper");
    JButton newGameButton = new JButton();

    JPanel timePanel = new JPanel();
    JPanel numOfMinesPanel = new JPanel();

    JMenuBar menuBar = new JMenuBar();
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGameMenuItem = new JMenuItem("New Game");
    JMenuItem statsMenuItem = new JMenuItem("Statistics");
    JMenuItem optionsMenuItem = new JMenuItem("Options");

    JLabel timeLabel;
    JLabel minesLabel;
    ArrayList<cell> cells = new ArrayList<>();
    optionDialog optionDialog;

    mainDialog(int numOfRows, int cellsPerRow, int numOfMines, optionDialog optionDialog) {

        frameHeight = numOfRows * cellSize + 70;
        frameWidth = cellsPerRow * cellSize + 20;
        playFieldHeight = numOfRows * cellSize;
        playFieldWidth = cellsPerRow * cellSize;
        this.cellsPerRow = cellsPerRow;
        this.numOfRows = numOfRows;
        this.numOfMines = numOfMines;
        this.optionDialog = optionDialog;

        setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        newGameButton.setSize(30, 30);
        newGameButton.setLocation(frameWidth / 2 - 15, 20);

        timePanel.setOpaque(true);
        timePanel.setBackground(Color.BLACK);

        numOfMinesPanel.setOpaque(true);
        numOfMinesPanel.setBackground(Color.BLACK);

        timePanel.setSize(40, 30);
        numOfMinesPanel.setSize(40, 30);

        timePanel.setLocation(playFieldWidth - 29, 20);
        numOfMinesPanel.setLocation(9, 20);

        gameMenu.add(newGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(statsMenuItem);
        gameMenu.add(optionsMenuItem);
        menuBar.add(gameMenu);

        frame.setJMenuBar(menuBar);
        frame.add(timePanel);
        frame.add(numOfMinesPanel);
        frame.add(newGameButton);

        newGameButton.addActionListener(this);
        newGameMenuItem.addActionListener(this);
        optionsMenuItem.addActionListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // creating cells and randomly add mines to some cells. Increasing the value of
        // difficulty means more mines will be added
        for (int h = 0; h < numOfRows; h++) {
            for (int w = 0; w < cellsPerRow; w++) {
                cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, false));
            }
        }

        for (int i = 0; i < this.numOfMines;) {
            int cellId = r.nextInt(cells.size());
            if (!cells.get(cellId).isMine) {
                cells.get(cellId).isMine = true;
                i++;
            } else {
                cellId = r.nextInt(cells.size());
            }
        }

        cells.forEach(n -> {
            n.findNeighbors();
        });

        timeLabel = new JLabel("0", JLabel.CENTER);
        minesLabel = new JLabel(Integer.toString(numOfMines), JLabel.CENTER);

        timeLabel.setForeground(Color.red);
        minesLabel.setForeground(Color.red);

        if (System.getProperty("os.name").equals("Linux")) {
            timeLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));
            minesLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));
        } else {
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            minesLabel.setFont(new Font("Arial", Font.BOLD, 15));
        }

        timePanel.add(timeLabel);
        numOfMinesPanel.add(minesLabel);

        timer.start();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clear all fields and start a new game
        if ((e.getSource() == newGameButton) || (e.getSource() == newGameMenuItem)) {

            secondsCounter = 0;
            cells = new ArrayList<>();

            for (int h = 0; h < numOfRows; h++) {
                for (int w = 0; w < cellsPerRow; w++) {
                    cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, false));
                }
            }

            for (int i = 0; i < this.numOfMines;) {
                int cellId = r.nextInt(cells.size());
                if (!cells.get(cellId).isMine) {
                    cells.get(cellId).isMine = true;
                    i++;
                } else {
                    cellId = r.nextInt(cells.size());
                }
            }
            cells.forEach(n -> {
                n.findNeighbors();
            });

            minesLabel.setText(Integer.toString(numOfMines));

            timer.start();

        } else if (e.getSource() == optionsMenuItem) {
            optionDialog.setVisible(true);
        } else {
            secondsCounter += 20;
            timeLabel.setText(Integer.toString(secondsCounter / 1000));
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // outline the playing field
        g.drawRect(9, 59, playFieldWidth + 1, playFieldHeight + 1);

        // draw the cells
        cells.forEach(n -> {
            n.draw(g);
        });

    }

    @Override
    // When the mouse cursor is hovering over a cell, make it darker
    public void mouseMoved(MouseEvent e) {
        Point mouseLocation = e.getPoint();
        int cellId = getCellId(mouseLocation);

        // if cursor is hovering over a cell, change the value of it's "mouseHovering"
        // boolean to true, so it gets drawn darker
        if (cellId != -1) {
            cells.forEach(n -> {
                n.mouseHovering = false;
            });
            cells.get(cellId).mouseHovering = true;
        } else { // if the cursor isn't hovering over a cell, remove "darkness" from all cells :3
            cells.forEach(n -> {
                n.mouseHovering = false;
            });
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (timer.isRunning()) {
            Point mouseLocation = e.getPoint();
            int cellId = getCellId(mouseLocation);

            // if right mouse button is clicked, mark the clicked cell (yellow color)
            if ((cellId != -1) && (e.getButton() == 3)) {

                if (cells.get(cellId).isMarked) {
                    cells.get(cellId).isMarked = false;
                    minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText()) + 1));

                } else {
                    minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText()) - 1));
                    cells.get(cellId).isMarked = true;
                }

                // if left mouse button is clicked, reveal the clicked cell
            } else if ((cellId != -1) && (e.getButton() == 1)) {
                if (cells.get(cellId).isMine) {
                    cells.forEach(n -> {
                        if (n.isMine)
                            n.isRevealed = true;
                    });
                    repaint();

                    timer.stop();
                } else {
                    reveal(cellId);
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    // a function the returns using only the location of the mouse cursor
    public int getCellId(Point mouseLocation) {

        if ((mouseLocation.x > 10) && (mouseLocation.x < playFieldWidth + 10) && (mouseLocation.y > 60)
                && (mouseLocation.y < 60 + playFieldHeight)) {
            int cellX = (mouseLocation.x - 10) / cellSize;
            int cellY = (mouseLocation.y - 60) / cellSize;
            int id = cellX + (cellY * (cellsPerRow));
            return id;
        } else {
            return -1;
        }
    }

    /*
     * in the original game, if a cell is clicked that's not a mine cell and that
     * doesn't have any adjacent mine cells, all surrounding cells that are not a
     * mine cell and that doesn't have any adjacent mine cells themselves are
     * revealed too. This recursive function does exactly that. Isn't that a great
     * solution?! :-)
     */
    public void reveal(int cellId) {
        if (!cells.get(cellId).isRevealed) {
            cells.get(cellId).isRevealed = true;
            if (!cells.get(cellId).isMine && (cells.get(cellId).adjacentMines == 0)) {
                cells.get(cellId).neighbors.forEach(n -> {
                    reveal(n.id);
                });
            }
        }

    }

    public boolean cellExists(int cellId) {
        if ((cellId >= 0) && (cellId < numOfRows * cellsPerRow)) {
            return true;
        }
        return false;
    }

    class cell {
        int id;
        int xCoor;
        int yCoor;
        int size;
        int adjacentMines;
        ArrayList<cell> neighbors = new ArrayList<>();
        boolean isMine;
        boolean isMarked;
        boolean isRevealed;
        boolean mouseHovering;

        cell(int id, int xCoor, int yCoor, int size, boolean isMine) {
            this.id = id;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.size = size;
            this.isMine = isMine;
        }

        void draw(Graphics g) {

            // set the color of the cell according to its status
            if (isRevealed && isMine) {
                g.setColor(Color.red);
            } else if (isRevealed && !isMine) {
                g.setColor(Color.lightGray);
            } else if (!isRevealed && isMarked) {
                g.setColor(Color.yellow);
            } else if (!isRevealed && !isMarked && !mouseHovering) {
                g.setColor(new Color(230, 230, 230)); // very very light gray

            } else if (!isRevealed && !isMarked && mouseHovering) {
                g.setColor(new Color(220, 220, 220)); // very light gray

            }

            // draw the cell
            g.fillRect(xCoor, yCoor, size, size);

            // draw the number of adjacent mines whenever needed
            if ((adjacentMines > 0) && (!isMine) && isRevealed) {
                if (adjacentMines == 1) {
                    g.setColor(Color.blue);
                } else if (adjacentMines == 2) {
                    g.setColor(new Color(0, 153, 0));
                } else {
                    g.setColor(Color.red);
                }
                if (System.getProperty("os.name").equals("Linux")) {
                    g.setFont(new Font("Ubuntu", Font.BOLD, 15));
                } else {
                    g.setFont(new Font("Arial", Font.PLAIN, 15));
                }
                g.drawString(Integer.toString(adjacentMines), xCoor + 6, yCoor + 14);
            }

        }

        public boolean atLeftEdge() {
            if ((id) % cellsPerRow == 0) {
                return true;
            } else {
                return false;
            }
        }

        public boolean atRightEdge() {
            if ((id + 1) % cellsPerRow == 0) {
                return true;
            } else {
                return false;
            }
        }

        public void findNeighbors() {
            if (cellExists(id - cellsPerRow))
                neighbors.add(cells.get(id - cellsPerRow));

            if (cellExists(id + cellsPerRow))
                neighbors.add(cells.get(id + cellsPerRow));

            if (cellExists(id - 1) && !atLeftEdge())
                neighbors.add(cells.get(id - 1));

            if (cellExists(id - cellsPerRow - 1) && !atLeftEdge())
                neighbors.add(cells.get(id - cellsPerRow - 1));

            if (cellExists(id - cellsPerRow + 1) && !atRightEdge())
                neighbors.add(cells.get(id - cellsPerRow + 1));

            if (cellExists(id + 1) && !atRightEdge())
                neighbors.add(cells.get(id + 1));

            if (cellExists(id + cellsPerRow + 1) && !atRightEdge())
                neighbors.add(cells.get(id + cellsPerRow + 1));

            if (cellExists(id + cellsPerRow - 1) && !atLeftEdge())
                neighbors.add(cells.get(id + cellsPerRow - 1));

            neighbors.forEach(n -> {
                if (n.isMine)
                    adjacentMines++;
            });
        }

    }
}

class optionDialog extends JDialog implements ActionListener, WindowListener {

    JRadioButton beginnerButton = new JRadioButton("Beginner (10 mines, 12 x 11 tile grid)", true);
    JRadioButton intermediateButton = new JRadioButton("Intermediate (40 mines, 16 x 16 tile grid)");
    JRadioButton advancedButton = new JRadioButton("Advanced (99 mines, 16 x 30 tile grid)");
    JRadioButton customButton = new JRadioButton("Custom:");
    ButtonGroup buttonGroup = new ButtonGroup();
    JPanel radioPanel = new JPanel();
    JPanel customPanel = new JPanel();
    JLabel height = new JLabel("Height:");
    JTextField heightField = new JTextField(3);

    JLabel width = new JLabel("Width:");
    JTextField widthField = new JTextField(3);

    JLabel mines = new JLabel("Mines:");
    JTextField minesField = new JTextField(3);
    mainDialog mainDialog;

    optionDialog() {

        beginnerButton.addActionListener(this);
        intermediateButton.addActionListener(this);
        advancedButton.addActionListener(this);
        customButton.addActionListener(this);

        buttonGroup.add(beginnerButton);
        buttonGroup.add(intermediateButton);
        buttonGroup.add(advancedButton);
        buttonGroup.add(customButton);

        customPanel.add(height);
        customPanel.add(heightField);
        customPanel.add(width);
        customPanel.add(widthField);
        customPanel.add(mines);
        customPanel.add(minesField);

        for (Component component : customPanel.getComponents()) {
            component.setEnabled(false);
        }

        radioPanel.setLayout(new GridLayout(5, 1));
        radioPanel.add(beginnerButton);
        radioPanel.add(intermediateButton);
        radioPanel.add(advancedButton);
        radioPanel.add(customButton);
        radioPanel.add(customPanel);

        radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Difficulty"));

        setContentPane(radioPanel);

        setTitle("Options");
        addWindowListener(this);
        setLocationRelativeTo(null);
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Component component : customPanel.getComponents()) {
            component.setEnabled(false);
        }

        if (e.getSource() == customButton) {
            for (Component component : customPanel.getComponents()) {
                component.setEnabled(true);
            }
        }

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (beginnerButton.isSelected()) {
            mainDialog.frame.dispose();
            this.mainDialog = new mainDialog(12, 11, 10, this);
        } else if (intermediateButton.isSelected()) {
            mainDialog.frame.dispose();
            this.mainDialog = new mainDialog(16, 16, 40, this);
        } else if (advancedButton.isSelected()) {
            mainDialog.frame.dispose();
            this.mainDialog = new mainDialog(16, 30, 99, this);
        } else {
            mainDialog.frame.dispose();
            this.mainDialog = new mainDialog(Integer.parseInt(heightField.getText()),
                    Integer.parseInt(widthField.getText()), Integer.parseInt(minesField.getText()), this);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }
}