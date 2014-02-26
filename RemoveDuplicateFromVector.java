/*

 * RemoveDuplicateFromVector.java
 * 
 * Copyright (c) 2012 Software Engineering Research Lab <http://serl.cs.wichita.edu/>. 
 * 

 * This file is part of Automatic Developer Recommendation Tool.
 * 
 * Automatic Developer Recommendation Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Automatic Developer Recommendation Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Automatic Developer Recommendation Tool.  If not, see <http ://www.gnu.org/licenses/>.

 */
package dev.recommendation.src;

import java.util.Vector;

/**
 *
 * @author kamal <mxhossen@wichita.edu>
 */
public class RemoveDuplicateFromVector {
    
    public static Vector removeDuplicates(Vector s) {
        int i = 0;
        int j = 0;
        boolean duplicates = false;

        Vector v = new Vector();

        for (i = 0; i < s.size(); i++) {
            duplicates = false;
            for (j = (i + 1); j < s.size(); j++) {
                if (s.elementAt(i).toString().equalsIgnoreCase(
                        s.elementAt(j).toString())) {
                    duplicates = true;
                }

            }
            if (duplicates == false) {
                v.addElement(s.elementAt(i).toString().trim());
            }

        }

        return v;
    }
    
}
