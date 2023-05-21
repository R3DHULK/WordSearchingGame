import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WordSearchGame extends JFrame {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 400;
    private static final int CELL_SIZE = 40;
    private static final int GRID_SIZE = 10;
    private static final char EMPTY_CELL = '-';
    private static final char SELECTED_CELL = '#';

    private JLabel[][] gridLabels;
    private char[][] grid;
    private Random random;
    private String[] words;
    private int wordsFound;

    public WordSearchGame() {
        setTitle("Word Search Game");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        gridLabels = new JLabel[GRID_SIZE][GRID_SIZE];
        grid = new char[GRID_SIZE][GRID_SIZE];
        random = new Random();
        words = new String[] { "JAVA", "PYTHON", "C#", "HTML", "CSS", "RUBY" };
        wordsFound = 0;

        initializeGrid();
        generateWordSearch();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JLabel label = new JLabel(Character.toString(grid[row][col]), SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 16));
                label.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.addMouseListener(new CellMouseListener(row, col));
                gridLabels[row][col] = label;
                add(label);
            }
        }
    }

    private void initializeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = EMPTY_CELL;
            }
        }
    }

    private void generateWordSearch() {
        for (String word : words) {
            boolean placed = false;
            while (!placed) {
                int startRow = random.nextInt(GRID_SIZE);
                int startCol = random.nextInt(GRID_SIZE);
                int direction = random.nextInt(8);
                placed = placeWord(word, startRow, startCol, direction);
            }
        }
    }

    private boolean placeWord(String word, int startRow, int startCol, int direction) {
        int length = word.length();
        int endRow = startRow;
        int endCol = startCol;

        for (int i = 0; i < length; i++) {
            if (endRow < 0 || endRow >= GRID_SIZE || endCol < 0 || endCol >= GRID_SIZE ||
                    (grid[endRow][endCol] != EMPTY_CELL && grid[endRow][endCol] != word.charAt(i))) {
                return false; // Cannot place word
            }

            switch (direction) {
                case 0: // Up
                    endRow--;
                    break;
                case 1: // Up-right
                    endRow--;
                    endCol++;
                    break;
                case 2: // Right
                    endCol++;
                    break;
                case 3: // Down-right
                    endRow++;
                    endCol++;
                    break;
                case 4: // Down
                    endRow++;
                    break;
                case 5: // Down-left
                    endRow++;
                    endCol--;
                    break;
                case 6: // Left
                    endCol--;
                    break;
                case 7: // Up-left
                    endRow--;
                    endCol--;
                    break;
            }
        }

        endRow = startRow;
        endCol = startCol;

        for (int i = 0; i < length; i++) {
            grid[endRow][endCol] = word.charAt(i);

            switch (direction) {
                case 0: // Up
                    endRow--;
                    break;
                case 1: // Up-right
                    endRow--;
                    endCol++;
                    break;
                case 2: // Right
                    endCol++;
                    break;
                case 3: // Down-right
                    endRow++;
                    endCol++;
                    break;
                case 4: // Down
                    endRow++;
                    break;
                case 5: // Down-left
                    endRow++;
                    endCol--;
                    break;
                case 6: // Left
                    endCol--;
                    break;
                case 7: // Up-left
                    endRow--;
                    endCol--;
                    break;
            }
        }

        return true; // Word placed successfully
    }

    private class CellMouseListener extends MouseAdapter {
        private int row;
        private int col;

        public CellMouseListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JLabel label = gridLabels[row][col];
            char cellValue = grid[row][col];

            if (cellValue != EMPTY_CELL && cellValue != SELECTED_CELL) {
                label.setBackground(Color.YELLOW);
                grid[row][col] = SELECTED_CELL;
                checkWordFound();
            }
        }

        private void checkWordFound() {
            StringBuilder wordBuilder = new StringBuilder();
            boolean found;

            // Check horizontal word
            wordBuilder.setLength(0);
            found = true;
            for (int c = 0; c < GRID_SIZE; c++) {
                wordBuilder.append(grid[row][c]);
            }
            String word = wordBuilder.toString();
            if (word.contains("-")) {
                found = false;
            } else {
                for (String w : words) {
                    if (word.equals(w)) {
                        wordsFound++;
                        break;
                    }
                }
            }
            if (found) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    gridLabels[row][c].setBackground(Color.GREEN);
                }
            }

            // Check vertical word
            wordBuilder.setLength(0);
            found = true;
            for (int r = 0; r < GRID_SIZE; r++) {
                wordBuilder.append(grid[r][col]);
            }
            word = wordBuilder.toString();
            if (word.contains("-")) {
                found = false;
            } else {
                for (String w : words) {
                    if (word.equals(w)) {
                        wordsFound++;
                        break;
                    }
                }
            }
            if (found) {
                for (int r = 0; r < GRID_SIZE; r++) {
                    gridLabels[r][col].setBackground(Color.GREEN);
                }
            }

            // Check diagonal words
            wordBuilder.setLength(0);
            found = true;
            int r = row;
            int c = col;
            while (r >= 0 && c < GRID_SIZE) {
                wordBuilder.append(grid[r][c]);
                r--;
                c++;
            }
            r = row + 1;
            c = col - 1;
            while (r < GRID_SIZE && c >= 0) {
                wordBuilder.append(grid[r][c]);
                r++;
                c--;
            }
            word = wordBuilder.toString();
            if (word.contains("-")) {
                found = false;
            } else {
                for (String w : words) {
                    if (word.equals(w)) {
                        wordsFound++;
                        break;
                    }
                }
            }
            if (found) {
                r = row;
                c = col;
                while (r >= 0 && c < GRID_SIZE) {
                    gridLabels[r][c].setBackground(Color.GREEN);
                    r--;
                    c++;
                }
                r = row + 1;
                c = col - 1;
                while (r < GRID_SIZE && c >= 0) {
                    gridLabels[r][c].setBackground(Color.GREEN);
                    r++;
                    c--;
                }
            }

            // Check reverse diagonal words
            wordBuilder.setLength(0);
            found = true;
            r = row;
            c = col;
            while (r >= 0 && c >= 0) {
                wordBuilder.append(grid[r][c]);
                r--;
                c--;
            }
            r = row + 1;
            c = col + 1;
            while (r < GRID_SIZE && c < GRID_SIZE) {
                wordBuilder.append(grid[r][c]);
                r++;
                c++;
            }
            word = wordBuilder.toString();
            if (word.contains("-")) {
                found = false;
            } else {
                for (String w : words) {
                    if (word.equals(w)) {
                        wordsFound++;
                        break;
                    }
                }
            }
            if (found) {
                r = row;
                c = col;
                while (r >= 0 && c >= 0) {
                    gridLabels[r][c].setBackground(Color.GREEN);
                    r--;
                    c--;
                }
                r = row + 1;
                c = col + 1;
                while (r < GRID_SIZE && c < GRID_SIZE) {
                    gridLabels[r][c].setBackground(Color.GREEN);
                    r++;
                    c++;
                }
            }

            if (wordsFound == words.length) {
                JOptionPane.showMessageDialog(null, "Congratulations! You found all the words!");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordSearchGame().setVisible(true);
            }
        });
    }
}
