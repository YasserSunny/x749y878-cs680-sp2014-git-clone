/*

 * SrcXMlParser.java
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

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author kamal <mxhossen@wichita.edu>
 */
public class SrcXMlParser extends DefaultHandler
{

    private File inputXmlFile;
    private String projectName;
    private Matcher matcher;
    private String text;
    private Pattern pattern1;
    private Pattern pattern2;
    private Pattern pattern3;
    private Pattern pattern4;
    private Pattern pattern5;
    private Pattern pattern5_modifier;
    private Pattern innerPattern1;
    private Pattern innerPattern2;
    private Pattern innerPattern3;
    private Pattern innerPattern4;
    private Pattern innerPattern5;
    private Pattern innerPattern6;
    private Vector failedLogVector;
    private Vector nameList;
    private Vector authorList;
    
    
    private String author;
    private String copyright;
    private String contributor;
    private String id;
    
    private Vector authorT=new Vector();
    private Vector copyrightT=new Vector();
    private Vector contributorT=new Vector();
    private Vector idT=new Vector();
    
    
    

    // private static Document dom;
    public SrcXMlParser(File file, String projectName)
    {
        this.inputXmlFile = file;
        this.projectName = projectName;

        nameList = new Vector();
        authorList = new Vector();
        failedLogVector = new Vector();
        author = "";
        copyright = "";
        contributor = "";
        id = "";
    }

    public Vector parseXmlFile()
    {


        try
        {
            //get the factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            Document dom = (Document) db.parse(getInputXmlFile());
            pattern1 = Pattern.compile("@author|Created by");
            pattern2 = Pattern.compile("Copyright\\s*\\(C\\)|\\s*\\(C\\)");
            pattern3 = Pattern.compile("Contributors:(\\s|\\n|.)*(\\*\\*)");
            pattern4 = Pattern.compile("\\*\\s*.*\\(.*\\)\\s*\\*\\s*\\*");
            pattern5 = Pattern.compile("\\$Id\\s*.* \\$");
            pattern5_modifier = Pattern.compile("[0-9]+\\s*.*\\$");


            innerPattern1 = Pattern.compile("([0-9-,]+)");
            innerPattern2 = Pattern.compile("see below|database|files|@version|Revision:|All Rights Reserved|<a href=\"mailto:\"|$Author:"
                    + "|not attributable|Copyright|based on CustomExportList by|\\?\\?|based on ExportCustomizationDialog by"
                    + "|\\(|by|may be|C|\\|:|\\)|Regents|alifornia|University|based|on|code|http|www|//|johann@jschmitz|Apache|Stware|Foundati|mailto|Id:|::Z|Politechnic of Bandung|@see javabeansPropertyhangeListener#propertyhange|Exp|::"
                    + ":forum|jrollercom|All|rights|reserved|France Telecom|INRIA|<a href=\"|Id|::");
            innerPattern3 = Pattern.compile("(The|the|Free|of|\\*|\\$|\\.)");
            innerPattern4 = Pattern.compile("\\(.*\\)");
            innerPattern5 = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
            innerPattern6 = Pattern.compile("\\s*.*\\.java,v");

            //Get all the comments
            NodeList nl = dom.getElementsByTagName("comment");
            int size = nl.getLength();

            for (int i = 0; i < size; i++)
            {
                Node n = nl.item(i);
                text = n.getTextContent();

                             
                //**for finding Ids
                buildIdList(text); //off for jedit
                //**for finding contributor
                buildContributorList(text);
                 //**build copyright list
                buildCopyrightList(text);
                 //**build authorlist
                buildAutorList(text);

            }

            System.err.println("\t>> " + "Author:" + author + " " + " Contributor:" + contributor + " " + " Copyright:" + copyright + " Id:" + id);
            
            authorList.clear();
            if(!idT.isEmpty())
                authorList.addAll(idT);
            if(!contributorT.isEmpty())
                authorList.addAll(contributorT);
            if(!copyrightT.isEmpty())
                authorList.addAll(copyrightT);
            if(!authorT.isEmpty())
                authorList.addAll(authorT);

            //System.err.println(">> " + "Author:" + author + " " + " Contributor:" + contributor);
            if (author.equals("") && contributor.equals("") && copyright.equals("") && id.equals(""))
            {
                failedLogVector.add(inputXmlFile);

            }


        }
        catch (SAXException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace();
        }


        return authorList;

    }

    public File getInputXmlFile()
    {
        return inputXmlFile;
    }

    private Vector findAuthor()
    {
        Vector names = new Vector();
        String s = text;

        while (matcher.find())
        {

            //System.err.println("Pattern Matched: " + matcher.pattern());
            int start = matcher.start();
            int end = matcher.end();

            int key = end + 1;
            String name = "";

            while (text.charAt(key) != '\n')
            {

                name += text.charAt(key);
                key++;

                if (key <= text.length() - 1)
                {

                    if (projectName.equals("ArgoUml"))
                    {



                        if (text.charAt(key) == '<' || text.charAt(key) == '>' || text.charAt(key) == '.' || text.charAt(key) == '(' || text.charAt(key) == ':') //. ( for aurgouml only
                        {

                            break;
                        }



                    }
                    else
                    {
                        if (text.charAt(key) == '<' || text.charAt(key) == '>' || text.charAt(key) == '(' || text.charAt(key) == ':') //. ( for no aurgouml 
                        {

                            break;
                        }
                    }
                    if (text.charAt(key) == ',')
                    {
                        names.add(name);
                        name = "";
                    }
                }else{
                    break;
                }


            }
            names.add(name);

        }
        return names;
    }

    private String extraRemoval(String s)
    {

        String str = s;

        //file name removal
        Matcher m6 = innerPattern6.matcher(str);
        if (m6.find())
        {
            str = m6.replaceAll("");
        }
        //email adress remover
        Matcher m5 = innerPattern5.matcher(str);
        if (m5.find())
        {
            str = m5.replaceAll("");
        }

        Matcher m4 = innerPattern4.matcher(str);
        if (m4.find())
        {
            str = m4.replaceAll("");
        }
        Matcher m1 = innerPattern1.matcher(str);
        if (m1.find())
        {
            str = m1.replaceAll("");
        }
        Matcher m2 = innerPattern2.matcher(str);
        if (m2.find())
        {
            str = m2.replaceAll("");
        }

        Matcher m3 = innerPattern3.matcher(str);
        if (m3.find())
        {
            str = m3.replaceAll("");
        }


        str = str.trim();

        return str;

    }

    public Vector getFailedLogVector()
    {
        return failedLogVector;
    }

    private void buildAutorList(String text)
    {
        if (matcher != null)
        {
            matcher.reset();
        }
        matcher = pattern1.matcher(text);
        nameList.removeAllElements();
        nameList = findAuthor();
        for (int j = 0; j < nameList.size(); j++)
        {
            if (!nameList.get(j).equals(""))
            {
                String str = extraRemoval(nameList.get(j).toString());

                if (!str.equals(""))
                {
                    String[] split = str.split("and|&");
                    if (split.length > 0)
                    {
                        for (int k = 0; k < split.length; k++)
                        {
                            authorList.add(split[k]);
                            author += split[k].trim() + ";";
                            authorT.add(split[k]);
                        }
                    }
                    else
                    {
                        authorList.add(str);
                        author += str + ";";
                        authorT.add(str);
                    }
                }
            }
        }
    }

    private void buildCopyrightList(String text)
    {
        if (matcher != null)
        {
            matcher.reset();
        }
        matcher.reset();
        matcher = pattern2.matcher(text);
        nameList.removeAllElements();
        nameList = findAuthor();
        for (int j = 0; j < nameList.size(); j++)
        {
            String str = extraRemoval(nameList.get(j).toString());
            if (!str.equals(""))
            {
                String[] split = str.split("and|&");
                if (split.length > 0)
                {
                    for (int k = 0; k < split.length; k++)
                    {
                        authorList.add(split[k]);
                        copyright += split[k].trim() + ";";
                        copyrightT.add(split[k]);
                    }

                }
                else
                {
                    authorList.add(str);
                    copyright += str.trim() + ";";
                    copyrightT.add(str);
                }
            }

        }
    }

    private void buildContributorList(String text)
    {
        if (matcher != null)
        {
            matcher.reset();
        }
        matcher = pattern3.matcher(text);
        if (matcher.find())
        {
            //String contributor = "";
            String temp = "";
            String rawText = matcher.group().toString().trim();
            int indexOf = rawText.indexOf('*');
            for (int t = indexOf; t < rawText.length(); t++)
            {
                if (rawText.charAt(t) != '*')
                {
                    temp += rawText.charAt(t);
                }
                else
                {
                    if (!temp.trim().equals(""))
                    {
                        temp = extraRemoval(temp);
                        temp = temp.trim();
                        contributorT.add(temp);
                        authorList.add(temp);
                        contributor += temp.toString() + ";";
                    }
                    else
                    {
                    }
                    temp = "";
                }
            }

        }
    }

    private void buildIdList(String text)
    {
        if (matcher != null)
        {
            matcher.reset();
        }
        matcher = pattern5.matcher(text);
        if (matcher.find())
        {

            String temp = "";
            String rawText = matcher.group().toString().trim();
            //System.err.println(rawText);

            matcher.reset();
            matcher = pattern5_modifier.matcher(rawText);

            if (matcher.find())
            {
                rawText = matcher.group().toString().trim();
                rawText = extraRemoval(rawText);
                if (!rawText.trim().toString().equals(""))
                {

                    String s = rawText.trim().toString();

                    //this matching only for Jedit
                    if (projectName == "Jedit")
                    {
                        Pattern p = Pattern.compile(".*.java");
                        Matcher m3 = p.matcher(s);
                        if (m3.find())
                        {
                            s = m3.replaceAll("").trim();
                        }
                    }

                    authorList.add(s);
                    idT.add(s);
                    id += s;


                }
            }


        }
    }
}
