/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.packSubGrid;

import assignment.ConstantsInterface;

/**
 *
 * @author cstuser
 */
public class clsPackSubGrid implements ConstantsInterface {

    public static boolean DigRepetInSubGrid(int[][] grid) {

        int[] counters = new int[N_OF_ROWS];

        for (int b = 0; b < N_OF_ROWS; ++b) {
            counters[b] = 0;
        }

        for (int d = 0; d < 2; ++d) {
            for (int c = 0; c < 2; ++c) {

                for (int b = 0; b < 2; ++b) {
                    for (int a = 0; b < 2; ++b) {
                        switch (grid[b + SUBGRID_ROWS * c][a + d * SUBGRID_COLS]) {
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

                    for (int e = 0; e < N_OF_ROWS; ++e) {
                        if (counters[e] > 1) {
                            return true;
                        } else {
                            counters[e] = 0;
                        }
                    }

                }
            }
        }

        return false;
    }

}
