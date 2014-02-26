/*

 * SpreadheetdOutput.java
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
public class SpreadheetdOutput {

    public Vector columNames;
    public Vector rowData;

    public SpreadheetdOutput() {
        columNames = new Vector(3);
        columNames.addElement("File Name");
        columNames.addElement("Author");
        columNames.addElement("Copyright");
        rowData = new Vector();
    }

    public Vector getRowData() {
        return rowData;
    }

    public void setRowData(Vector rowData) {
        this.rowData.add(rowData);
    }

    public Vector getColumNames() {
        return columNames;

    }

    public void ouputInFile() {

        String[] columns = new String[]{"FileName", "Authors", "Contributors", "Copuright"};
        //System.err.println(rowData.get(10));
        final Object[][] outputData = new Object[rowData.size()][4];

        for (int i = 0; i < rowData.size(); i++) {

            Vector data = (Vector) rowData.get(i);
            String firstColumn = "";
            String secondColumn = "";
            String thirdColumn = "";
            String fourthColumn = "";

            if (!data.equals("")) {
                firstColumn = data.get(0).toString();
                secondColumn = data.get(1).toString();
                thirdColumn = data.get(2).toString();
                fourthColumn = data.get(3).toString();

            }

            outputData[i] = new Object[]{firstColumn, secondColumn, thirdColumn, fourthColumn};
        }





//        TableModel model = new DefaultTableModel(outputData, columns);
//        final File file = new File("/home/kml/srcML/output.ods");
//        try {
//            SpreadSheet.createEmpty(model).saveAs(file);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(SourceFileOwnerFinder.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(SourceFileOwnerFinder.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            OOUtils.open(file);
//        } catch (IOException ex) {
//            Logger.getLogger(SourceFileOwnerFinder.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}

