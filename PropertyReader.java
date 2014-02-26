/*

 * PropertyReader.java
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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author kamal <mxhossen@wichita.edu>
 */
public class PropertyReader {
      private static final String BUNDLE_NAME = "dev.recommendation.resource.config";
      private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
     
      public static String getValue(String key){
            try{
                  return RESOURCE_BUNDLE.getString(key);
            }catch(MissingResourceException mex){
                mex.printStackTrace();
                  return "";
            }catch(Exception ex){
                ex.printStackTrace();
                  return "NOT FOUND";
            }
      }
}
