/*
 * 
 */
package assignment.packGrid;

import assignment.ConstantsInterface;

/**
 *
 * @author el_rus0_kpo_2011@hotmail.com pongo webcam
 */
public class clsPackGrid implements ConstantsInterface {

    public static boolean DigRepetInCol(int[][] grid) {
        int[] counters = new int[N_OF_COLS];

        for (int k = 0; k < N_OF_COLS; ++k) {
            for (int b = 0; b < N_OF_COLS; ++b) {
                counters[b] = 0;
            }

            for (int i = 0; i < N_OF_COLS; ++i) {
                switch (grid[i][k]) {
                    case 1:
                        counters[0] += 1;
                        break;
                    case 2:
                        counters[1] += 1;
                        break;
                    case 3:
                        counters[2] += 1;
                        break;
                    case 4:
                        counters[3] += 1;
                        break;
                    case 5:
                        counters[4] += 1;
                        break;
                    case 6:
                        counters[5] += 1;
                        break;
                    case 7:
                        counters[6] += 1;
                        break;
                    case 8:
                        counters[7] += 1;
                        break;
                    case 9:
                        counters[8] += 1;
                        break;
                    default:
                        break;
                }
            }

            for (int b = 0; b < N_OF_COLS; ++b) {
                if (counters[b] > 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean DigRepetInRow(int[][] grid) {
        int[] counters = new int[N_OF_ROWS];

        for (int k = 0; k < N_OF_ROWS; ++k) {
            for (int b = 0; b < N_OF_ROWS; ++b) {
                counters[b] = 0;
            }

            for (int i = 0; i < N_OF_ROWS; ++i) {
                switch (grid[i][k]) {
                    case 1:
                        counters[0] += 1;
                        break;
                    case 2:
                        counters[1] += 1;
                        break;
                    case 3:
                        counters[2] += 1;
                        break;
                    case 4:
                        counters[3] += 1;
                        break;
                    case 5:
                        counters[4] += 1;
                        break;
                    case 6:
                        counters[5] += 1;
                        break;
                    case 7:
                        counters[6] += 1;
                        break;
                    case 8:
                        counters[7] += 1;
                        break;
                    case 9:
                        counters[8] += 1;
                        break;
                    default:
                        break;
                }
            }

            for (int b = 0; b < N_OF_ROWS; ++b) {
                if (counters[b] > 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isComplete(int[][] grid) {
        int sum = 0;
        for (int a = 0; a < N_OF_ROWS; ++a) {
            for (int b = 0; b < N_OF_COLS; ++b) {
                sum += grid[a][b];
            }
        }

        return !(sum == 405);

    }

}
