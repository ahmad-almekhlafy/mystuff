import javax.swing.*;
import java.io.*;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

class minesweeper extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    int cellSize = 20;
    int frameHeight = 390;
    int frameWidth = 300;
    int difficulty = 1;

    int playFieldHeight = ((frameHeight - 70) / cellSize) * cellSize;
    int PlayFieldWidth = ((frameWidth - 20) / cellSize) * cellSize;
    int cellsPerRow = PlayFieldWidth / cellSize;
    int numOfRows = playFieldHeight / cellSize;

    Random r = new Random();
    Timer timer = new Timer(20, this);
    int numOfMines;
    int secondsCounter;

    JFrame frame = new JFrame("Minesweeper");
    JButton newGame = new JButton();
    ArrayList<cell> cells = new ArrayList<>();

    public static void main(String[] args) {
        new minesweeper();
    }

    minesweeper() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        newGame.setSize(30, 30);
        newGame.setLocation(frameWidth / 2 - 15, 20);

        frame.add(newGame);
        newGame.addActionListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // creating cells and randomly add mines to some cells. Increasing the value of
        // difficulty means more mines will be added
        for (int h = 0; h < numOfRows; h++) {
            for (int w = 0; w < cellsPerRow; w++) {
                if ((r.nextInt(100) < (10 + 3 * difficulty))) {
                    cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, true));
                    numOfMines++;
                } else {
                    cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, false));
                }
            }
        }

        cells.forEach(n -> {
            n.findNeighbors();
        });

        timer.start();
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clear all fields and start a new game
        if (e.getSource() == newGame) {

            numOfMines = 0;
            secondsCounter = 0;

            cells = new ArrayList<>();
            for (int h = 0; h < numOfRows; h++) {
                for (int w = 0; w < cellsPerRow; w++) {
                    if ((r.nextInt(100) < (10 + 3 * difficulty))) {
                        cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, true));
                        numOfMines++;
                    } else {
                        cells.add(new cell(cells.size(), (w * cellSize) + 10, (h * cellSize) + 60, cellSize, false));
                    }
                }
            }

            cells.forEach(n -> {
                n.findNeighbors();
            });

            timer.start();

        } else {
            secondsCounter += 20;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // outline the playing field
        g.drawRect(9, 59, PlayFieldWidth + 1, playFieldHeight + 1);

        // draw the cells
        cells.forEach(n -> {
            n.draw(g);
        });

        // draw two black boxes, one for time and one for mines' number
        g.setColor(Color.BLACK);
        g.fillRect(9, 20, 40, 30);
        g.fillRect(PlayFieldWidth - 29, 20, 40, 30);

        // draw time and mines' number
        g.setColor(Color.red);
        if (System.getProperty("os.name").equals("Linux")) {
            g.setFont(new Font("Ubuntu", Font.BOLD, 20));
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 20));
        }
        g.drawString(Integer.toString(numOfMines), 19, 42);

        if ((secondsCounter / 1000) < 10) {
            g.drawString(Integer.toString(secondsCounter / 1000), PlayFieldWidth - 15, 42);
        } else if ((secondsCounter / 1000) < 100) {
            g.drawString(Integer.toString(secondsCounter / 1000), PlayFieldWidth - 19, 42);
        } else {
            g.drawString(Integer.toString(secondsCounter / 1000), PlayFieldWidth - 25, 42);
        }
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
                    numOfMines++;
                } else {
                    numOfMines--;
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

        if ((mouseLocation.x > 10) && (mouseLocation.x < PlayFieldWidth + 10) && (mouseLocation.y > 60)
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