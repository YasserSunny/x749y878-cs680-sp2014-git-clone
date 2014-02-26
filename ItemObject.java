/*

 * ItemObject.java
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

/**
 *
 * @author kamal <mxhossen@wichita.edu>
 */
public class ItemObject {

    private String fileName;
    private String fileAbsPath;
    
    private int pos;
    private String devs;

    public ItemObject(String fName, String fPath) {

        this.fileName = fName;
        this.fileAbsPath = fPath;
    }
    
    public ItemObject(int pos,String devs){
        this.pos=pos;
        this.devs=devs;
    }
    

    public String getFileAbsPath() {
        return fileAbsPath;
    }

    public void setFileAbsPath(String fileAbsPath) {
        this.fileAbsPath = fileAbsPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    public String getDevs()
    {
        return devs;
    }

    public void setDevs(String devs)
    {
        this.devs = devs;
    }

    
    
    
}

