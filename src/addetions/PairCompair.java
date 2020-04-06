/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addetions;

import java.util.Comparator;

/**
 *
 * @author DELL
 */
public class PairCompair implements Comparator<pair> {

    @Override
    public int compare(pair p1, pair p2) {
        if (p1.first < p2.first) {
            return -1;
        } else if (p1.first > p2.first) {
            return 1;
        }
        return 0;
    }
}
