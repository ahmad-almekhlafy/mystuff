import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class Maze extends JPanel implements ActionListener, MouseMotionListener {
    // Default values
    int cellSize = 20;
    int wallSize = 15;
    int speed = 0;
    Color mainColor = Color.BLACK;
    Color foundedPathColor = new Color(0, 153, 0); // green
    Color topCellColor = new Color(255, 51, 51); // red

    int height = (500 / (cellSize + wallSize)) * (cellSize + wallSize);
    int width = (900 / (cellSize + wallSize)) * (cellSize + wallSize);
    int cellsPerRow = width / (cellSize + wallSize);
    int numOfRows = height / (cellSize + wallSize);
    Timer timer = new Timer(speed, this);
    Random r = new Random();
    JOptionPane errorMessage = new JOptionPane();

    JFrame frame = new JFrame("Maze");
    JMenuBar menuBar = new JMenuBar();
    JMenu doMenu = new JMenu("Do...");

    JMenuItem solveMaze = new JMenuItem("Solve");
    JMenuItem findPath = new JMenuItem("Find a path from cell x to cell y");
    JMenuItem newMaze = new JMenuItem("Gen. New Maze");
    JMenuItem saveMaze = new JMenuItem("Save Maze");
    JMenuItem loadMaze = new JMenuItem("Load Maze");
    JMenuItem customizeStuff = new JMenuItem("Customize Colors and Stuff");

    ArrayList<wall> walls = new ArrayList<>();
    ArrayList<cell> cells = new ArrayList<>();
    Stack<cell> generatingStack = new Stack<cell>(); // used for generating mazes
    Stack<cell> solvingStack = new Stack<cell>(); // used for solving mazes

    int startId;
    JTextField cell1 = new JTextField();
    JTextField cell2 = new JTextField();

    public static void main(String[] args) {
        // makes UI look cool
        /*
         * try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
         * catch (Exception x) { ex.printStackTrace(); }
         */

        new Maze();
    }

    Maze() {
        // Configuring main frame
        setPreferredSize(new Dimension(width - wallSize, height - wallSize));
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuring the menu and the tooltip
        doMenu.add(solveMaze);
        doMenu.add(findPath);
        doMenu.add(newMaze);
        doMenu.add(saveMaze);
        doMenu.add(loadMaze);
        doMenu.add(customizeStuff);
        menuBar.add(doMenu);
        frame.setJMenuBar(menuBar);
        JToolTip cellId = new JToolTip();
        frame.add(cellId);

        // Adding this panel as a listener to different components
        solveMaze.addActionListener(this);
        findPath.addActionListener(this);
        newMaze.addActionListener(this);
        saveMaze.addActionListener(this);
        loadMaze.addActionListener(this);
        customizeStuff.addActionListener(this);
        this.addMouseMotionListener(this);

        // Showing frame
        frame.pack();
        frame.setVisible(true);

        for (int h = 0; h < numOfRows; h++) {
            for (int w = 0; w < cellsPerRow; w++) {
                cells.add(new cell(w * (cellSize + wallSize), h * (cellSize + wallSize), cellSize, cells.size()));

                /*
                 * Adding null-Walls makes it possible to access a wall through its idCell1 und
                 * idCell2. All vertical walls have an even index and all horizontal walls have
                 * an odd index. Example (1): The vertical wall separating cell 4 and cell 5 has
                 * the index 4*2=8. Example (2): The horizontal wall separating cell 4 from the
                 * cell underneath it (cellId = 4+cellsPerRow) has index 4*2+1 = 9.
                 */
                int idCell1 = cells.size() - 1;
                if (w < cellsPerRow - 1) {
                    walls.add(new wall(w * (cellSize + wallSize) + cellSize, h * (cellSize + wallSize), wallSize, true,
                            idCell1, idCell1 + 1));
                } else if (w == cellsPerRow - 1) {
                    walls.add(null);

                }

                if (h < numOfRows - 1) {
                    walls.add(new wall(w * (cellSize + wallSize), h * (cellSize + wallSize) + cellSize, wallSize, false,
                            idCell1, idCell1 + cellsPerRow));
                } else if (h == numOfRows - 1) {
                    walls.add(null);
                }
            }
        }

        // after creating the cells, find the neighbors of each cell
        for (int i = 0; i < cellsPerRow * numOfRows; i++)
            cells.get(i).lookForNeighbors();

        // choose a random cell and start the depth first search with it being the cell
        // at the top of the generatingStack
        startId = r.nextInt(cells.size());
        cells.get(startId).firstVisit = true;
        generatingStack.push(cells.get(startId));
        doMenu.setEnabled(false);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == (("Customize Colors and Stuff"))) {

            JTextField speed = new JTextField();
            JColorChooser mainColorChooser = new JColorChooser();
            JColorChooser topCellColorChooser = new JColorChooser();
            JColorChooser foundedPathColorChooser = new JColorChooser();

            mainColorChooser.setPreviewPanel(new JPanel());
            topCellColorChooser.setPreviewPanel(new JPanel());
            foundedPathColorChooser.setPreviewPanel(new JPanel());

            Object[] message = { "Speed:", speed, "Main Color:", mainColorChooser, "Top Cell's Color:",
                    topCellColorChooser, "Founded Path Color:", foundedPathColorChooser };
            JOptionPane customizationWindow = new JOptionPane(message);
            customizationWindow.showConfirmDialog(this, message, "Customize ...", customizationWindow.OK_CANCEL_OPTION);

            try {
                if (mainColorChooser.getColor() != Color.white)
                    mainColor = mainColorChooser.getColor();
                if (topCellColorChooser.getColor() != Color.white)
                    topCellColor = topCellColorChooser.getColor();
                if (foundedPathColorChooser.getColor() != Color.white)
                    foundedPathColor = foundedPathColorChooser.getColor();
                timer.setDelay(Integer.parseInt(speed.getText()));

            } catch (NumberFormatException c) {
                if (!speed.getText().equals(""))
                    errorMessage.showMessageDialog(frame, "The speed entered isn't valid! It won't be changed.",
                            "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getActionCommand() == (("Gen. New Maze"))) {
            solvingStack = new Stack<cell>();

            cells.forEach((n) -> {
                n.firstVisit = false;
                n.secondVisit = false;
            });

            walls.forEach((n) -> {
                if (n != null) {
                    n.isBroken = false;
                    n.isVisited = false;
                }
            });

            startId = r.nextInt(cells.size());
            generatingStack.push(cells.get(startId));
            doMenu.setEnabled(false);
            timer.start();

        } else if (e.getActionCommand() == (("Save Maze"))) {
            try {

                FileDialog saveFileDialog = new FileDialog(frame, "Save Maze as .mze File ...", FileDialog.SAVE);
                saveFileDialog.setVisible(true);

                if (((saveFileDialog.getDirectory() != null) && (saveFileDialog.getFile() != null))) {
                    File savedFile = new File(saveFileDialog.getDirectory() + saveFileDialog.getFile() + ".mze");
                    FileWriter writer = new FileWriter(savedFile);
                    writer.write(height * width * cellSize * wallSize + "\n");

                    for (int i = 0; i < cells.size(); i++) {

                        writer.write("c" + i + ": ");
                        if (cells.get(i).firstVisit) {
                            writer.write("1");
                        } else {
                            writer.write("0");
                        }

                        if (cells.get(i).secondVisit) {
                            writer.write("1");
                        } else {
                            writer.write("0");
                        }
                        writer.write("\n");
                    }

                    for (int i = 0; i < walls.size(); i++) {

                        if (walls.get(i) != null) {
                            writer.write("w" + i + ": ");

                            if (walls.get(i).isVisited) {
                                writer.write("1");
                            } else {
                                writer.write("0");
                            }

                            if (walls.get(i).isBroken) {
                                writer.write("1");
                            } else {
                                writer.write("0");
                            }

                            writer.write("\n");
                        }
                    }

                    writer.close();
                }
            }

            catch (Exception x) {

            }

        } else if (e.getActionCommand() == (("Load Maze"))) {
            try {

                // One could use swing like in the following line, but it doesn't look pretty :3
                // JFileChooser fileChooser = new JFileChooser();

                FileDialog openFileDialog = new FileDialog(frame, "Choose a .mze File ...", FileDialog.LOAD);

                openFileDialog.setFilenameFilter(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (name.toLowerCase().endsWith(".mze")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                openFileDialog.setVisible(true);
                File loadedFile = new File(openFileDialog.getDirectory() + openFileDialog.getFile());

                FileReader fileReader = new FileReader(loadedFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String nextLine = bufferedReader.readLine();
                if (nextLine.equals(Integer.toString(height * width * cellSize * wallSize))) {
                    while ((nextLine = bufferedReader.readLine()) != null) {

                        if (nextLine.charAt(0) == 'c') {
                            int index = Integer.parseInt((nextLine.substring(1, nextLine.indexOf(':'))));
                            int begin = nextLine.indexOf(' ');
                            if (nextLine.charAt(begin + 1) == '1') {
                                cells.get(index).firstVisit = true;
                            } else {
                                cells.get(index).firstVisit = false;
                            }

                            if (nextLine.charAt(begin + 2) == '1') {
                                cells.get(index).secondVisit = true;
                            } else {
                                cells.get(index).secondVisit = false;
                            }
                        } else if (nextLine.charAt(0) == 'w') {
                            int index = Integer.parseInt((nextLine.substring(1, nextLine.indexOf(':'))));
                            int begin = nextLine.indexOf(' ');
                            if (nextLine.charAt(begin + 1) == '1') {
                                walls.get(index).isVisited = true;
                            } else {
                                walls.get(index).isVisited = false;
                            }

                            if (nextLine.charAt(begin + 2) == '1') {
                                walls.get(index).isBroken = true;
                            } else {
                                walls.get(index).isBroken = false;
                            }
                        }

                    }

                    bufferedReader.close();

                    repaint();

                } else {

                    errorMessage.showMessageDialog(frame,
                            "The selected maze file doesn't have the appropriate measurements", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            catch (Exception x) {

            }

            // find a path... and solve are to summed together in one condition because they
            // do basically the same thing
        } else if ((e.getActionCommand() == (("Find a path from cell x to cell y"))
                || (e.getActionCommand() == ("Solve")))) {

            Object[] message = { "Start Cell:", cell1, "Destination Cell:", cell2 };
            JOptionPane input = new JOptionPane(message);
            int selectedOption = -1;

            if ((e.getActionCommand() == ("Find a path from cell x to cell y"))) {
                cell1.setText("");
                cell2.setText("");
                selectedOption = input.showConfirmDialog(this, message, "Enter Cells' Numbers:",
                        JOptionPane.OK_CANCEL_OPTION);
            } else {
                selectedOption = 0;
                cell1.setText("0");
                cell2.setText(Integer.toString(numOfRows * cellsPerRow - 1)); // Set cell2 to be the last cell
            }

            // try-catch is used to prevent either having a non-integer input or having an
            // invalid integer input
            try {
                if ((selectedOption == 0) && ((Integer.parseInt(cell1.getText()) > (numOfRows * cellsPerRow - 1))
                        || (Integer.parseInt(cell1.getText()) < 0)
                        || (Integer.parseInt(cell2.getText()) > (numOfRows * cellsPerRow - 1))
                        || (Integer.parseInt(cell2.getText()) < 0))) {
                    throw new Exception();
                } else if ((selectedOption == 0)) {
                    cell startCell = cells.get(Integer.parseInt(cell1.getText()));
                    cells.forEach((n) -> {
                        n.firstVisit = false;
                        n.secondVisit = true;
                    });

                    walls.forEach((n) -> {
                        if (n != null)
                            n.isVisited = false;
                    });

                    startCell.firstVisit = true;
                    solvingStack.push(startCell);
                    doMenu.setEnabled(false);
                    timer.start();
                }

            }

            catch (Exception c) {
                errorMessage.showMessageDialog(frame, "Input isn't valid!", "Error!", JOptionPane.ERROR_MESSAGE);
            }

        } else {

            // if a maze is being generated
            if (!generatingStack.isEmpty()) {
                cell nextCell = generatingStack.peek().visitRandomNeighbor();
                if (nextCell.id == generatingStack.peek().id) {
                    generatingStack.pop();
                } else {
                    generatingStack.push(nextCell);
                }
            }

            // if a maze is being solved
            if (!solvingStack.isEmpty()) {
                cell nextCell = solvingStack.peek().visitConnectedNeighbor();
                if (nextCell.id == solvingStack.peek().id) {
                    solvingStack.peek().secondVisit = false;
                    solvingStack.peek().getWall(solvingStack.get(solvingStack.size() - 2).id).isVisited = false;
                    solvingStack.pop();

                } else if (nextCell.id == cells.get(Integer.parseInt(cell2.getText())).id) {
                    solvingStack.push(nextCell);
                    cell1 = new JTextField();
                    cell2 = new JTextField();
                    doMenu.setEnabled(true);
                    timer.stop();

                } else {
                    solvingStack.push(nextCell);
                }
            }
            repaint();

            // Fixing lagging if OS is linux
            if (System.getProperty("os.name").equals("Linux")) {
                Toolkit.getDefaultToolkit().sync();
            }
        }

    }

    public void paintComponent(Graphics g) {

        g.setColor(mainColor);
        g.fillRect(0, 0, width - wallSize, height - wallSize);

        cells.forEach((n) -> n.draw(g));

        walls.forEach((n) -> {
            if (n != null)
                n.draw(g);

        });

        // color the cell at the top of the generatingStack with a certain color
        // (default: red), so it looks cool
        // and we know what is actually happening
        if (!generatingStack.isEmpty()) {
            g.setColor(topCellColor);
            g.fillRect(generatingStack.peek().xCoor, generatingStack.peek().yCoor, cellSize, cellSize);
        }

        // if both stacks are empty (nothing is being done), stop timer, so that
        // unnecessary actions fired by it are no longer fired
        if ((generatingStack.isEmpty()) && (solvingStack.isEmpty()) && timer.isRunning()) {
            doMenu.setEnabled(true);
            timer.stop();
        }

    }

    public void mouseMoved(MouseEvent e) {
        Point mouseLocation = e.getPoint();
        int cellX = mouseLocation.x / (cellSize + wallSize);
        int cellY = mouseLocation.y / (cellSize + wallSize);
        this.setToolTipText(Integer.toString(cellX + (cellY * cellsPerRow)));

    }

    public void mouseDragged(MouseEvent e) {

    }

    class cell {
        int size;
        int xCoor;
        int yCoor;
        int id;

        ArrayList<cell> neighbors = new ArrayList<cell>();
        boolean firstVisit;
        boolean secondVisit;

        cell(int xCoor, int yCoor, int size, int id) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.id = id;
        }

        public cell visitRandomNeighbor() {

            ArrayList<cell> unvisitedNeighbors = new ArrayList<cell>();
            for (int i = 0; i < neighbors.size(); i++) {
                if (!neighbors.get(i).firstVisit)
                    unvisitedNeighbors.add(neighbors.get(i));
            }
            if (unvisitedNeighbors.isEmpty())
                return this;

            int randomNeighborId = r.nextInt(unvisitedNeighbors.size());
            cell randomNeighbor = unvisitedNeighbors.get(randomNeighborId);
            randomNeighbor.firstVisit = true;

            getWall(randomNeighbor.id).isBroken = true;

            return randomNeighbor;
        }

        // a neighbor is connected when the wall separating it from this cell is broken
        public cell visitConnectedNeighbor() {
            ArrayList<cell> connectedNeighbors = new ArrayList<cell>();

            for (int i = 0; i < neighbors.size(); i++) {
                if (getWall(neighbors.get(i).id).isBroken)
                    connectedNeighbors.add(neighbors.get(i));
            }

            ArrayList<cell> unvisitedNeighbors = new ArrayList<cell>();
            for (int i = 0; i < connectedNeighbors.size(); i++) {
                if (!connectedNeighbors.get(i).firstVisit)
                    unvisitedNeighbors.add(connectedNeighbors.get(i));
            }

            if (unvisitedNeighbors.isEmpty())
                return this;

            int connectedNeighborId = r.nextInt(unvisitedNeighbors.size());
            cell connectedNeighbor = unvisitedNeighbors.get(connectedNeighborId);
            connectedNeighbor.firstVisit = true;
            getWall(connectedNeighbor.id).isVisited = true;

            return connectedNeighbor;

        }

        public wall getWall(int otherCellId) {
            if (Math.abs(id - otherCellId) == 1) {
                if (walls.get(Math.min(id, otherCellId) * 2) != null)
                    return walls.get(Math.min(id, otherCellId) * 2);
            } else {
                if (walls.get(Math.min(id, otherCellId) * 2 + 1) != null)
                    return walls.get(Math.min(id, otherCellId) * 2 + 1);
            }
            return null;
        }

        public void draw(Graphics g) {

            if ((firstVisit) && (!secondVisit)) {
                g.setColor(Color.WHITE);

            } else if ((firstVisit) && (secondVisit)) {
                g.setColor(foundedPathColor);

            } else if ((!firstVisit) && (!secondVisit)) {
                g.setColor(mainColor);

            } else if ((!firstVisit) && (secondVisit)) {
                g.setColor(Color.WHITE);

            }
            g.fillRect(xCoor, yCoor, size, size);

        }

        public void lookForNeighbors() {

            if (xCoor != 0)
                neighbors.add(cells.get(id - 1));
            if (yCoor != 0)
                neighbors.add(cells.get(id - cellsPerRow));
            if (xCoor != width - (size + wallSize))
                neighbors.add(cells.get(id + 1));
            if (yCoor != height - (size + wallSize))
                neighbors.add(cells.get(id + cellsPerRow));

        }
    }

    class wall {
        int size;
        int xCoor;
        int yCoor;
        int idCell1;
        int idCell2;
        boolean isBroken;
        boolean isVertical;
        boolean isVisited;

        wall(int xCoor, int yCoor, int size, boolean isVertical, int idCell1, int idCell2) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.isVertical = isVertical;
            this.idCell1 = idCell1;
            this.idCell2 = idCell2;
        }

        public void draw(Graphics g) {

            if ((isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, cellSize, size);
            } else if ((isVertical) && (isBroken) && (isVisited)) {
                g.setColor(foundedPathColor);
                g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (isBroken) && (isVisited)) {
                g.setColor(foundedPathColor);
                g.fillRect(xCoor, yCoor, cellSize, size);
            }
        }
    }

}