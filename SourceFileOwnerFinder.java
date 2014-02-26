/*

 * SourceFileOwnerFinder.java
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

import static dev.recommendation.src.SourceFileOwnerFinder.addPath;
import static dev.recommendation.src.SourceFileOwnerFinder.checkIdentitiyInUniqueNameMap;
import static dev.recommendation.src.SourceFileOwnerFinder.developerCommitCount;

import static dev.recommendation.src.SourceFileOwnerFinder.developerModifiedMapPerBug;
import static dev.recommendation.src.SourceFileOwnerFinder.developerTotalCountMap;
import static dev.recommendation.src.SourceFileOwnerFinder.dumpArray;
import static dev.recommendation.src.SourceFileOwnerFinder.endDate;
import static dev.recommendation.src.SourceFileOwnerFinder.escapePath;
import static dev.recommendation.src.SourceFileOwnerFinder.failedToParse;
import static dev.recommendation.src.SourceFileOwnerFinder.filePathList;
import static dev.recommendation.src.SourceFileOwnerFinder.fileTypeValidator;
import static dev.recommendation.src.SourceFileOwnerFinder.filterdType;
import static dev.recommendation.src.SourceFileOwnerFinder.genericFileName;
import static dev.recommendation.src.SourceFileOwnerFinder.getInputString;
import static dev.recommendation.src.SourceFileOwnerFinder.issueCommitMap;
import static dev.recommendation.src.SourceFileOwnerFinder.issueCommitPath;
import static dev.recommendation.src.SourceFileOwnerFinder.issueDeveloperMap;
import static dev.recommendation.src.SourceFileOwnerFinder.issueDeveloperPath;
import static dev.recommendation.src.SourceFileOwnerFinder.keywordList;
import static dev.recommendation.src.SourceFileOwnerFinder.keywordListPath;
import static dev.recommendation.src.SourceFileOwnerFinder.logPath;
import static dev.recommendation.src.SourceFileOwnerFinder.multipleRelease;
import static dev.recommendation.src.SourceFileOwnerFinder.outVector;
import static dev.recommendation.src.SourceFileOwnerFinder.outputDirPath;
import static dev.recommendation.src.SourceFileOwnerFinder.projectName;
import static dev.recommendation.src.SourceFileOwnerFinder.recListDirPath;
import static dev.recommendation.src.SourceFileOwnerFinder.sourceCodeDirPath;
import static dev.recommendation.src.SourceFileOwnerFinder.sourceNotFound;
import static dev.recommendation.src.SourceFileOwnerFinder.srcDirVector;
import static dev.recommendation.src.SourceFileOwnerFinder.startDate;
import static dev.recommendation.src.SourceFileOwnerFinder.svnLogPath;
import static dev.recommendation.src.SourceFileOwnerFinder.uniqeNameMap;
import static dev.recommendation.src.SourceFileOwnerFinder.uniqueNamePath;
import static dev.recommendation.src.SourceFileOwnerFinder.wd;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kamal <mxhossen@wichita.edu>
 */
public class SourceFileOwnerFinder
{

    /**
     * @param args the command line arguments
     *
     *
     */
    public static File wd;
    public static String recListDirPath;
    public static String recListIrDirPath;
    public static String sourceCodeDirPath;
    public static String outputDirPath;
    public static String uniqueNamePath;
    public static String logPath;
    public static String svnLogPath;
    public static String addPath;
    public static String escapePath;
    public static String startDate;
    public static String endDate;
    public static FileTypeValidator fileTypeValidator = new FileTypeValidator();
    public static List<ItemObject> filePathList;
    public static Vector sourceNotFound;
    public static Vector failedToParse;
    public static LinkedHashMap uniqeNameMap;
    public static LinkedHashMap<String, Double> developerTotalCountMap;
    public static LinkedHashMap rankList1;
    public static LinkedHashMap rankList2;
    public static boolean multipleRelease;
    public static String filterdType;
    public static String projectName;
    public static Vector srcDirVector;
    public static String genericFileName;
    public static ArrayList<LinkedHashMap<String, Double>> dumpArray;
    public static int m;
    public static int n;
    public static ArrayList<String> relevantsFiles = new ArrayList<String>();
    public static ArrayList<String> relevantsFilesTemp = new ArrayList<String>();
    public static HashMap issueCommitMap;
    public static HashMap issueDeveloperMap;
    public static String issueCommitPath;
    public static String issueDeveloperPath;
    public static String keywordListPath;
    //public static String issueOpenDatePath;
    public static Vector keywordList;
    public static LinkedHashMap developerCommitCount;
    public static LinkedHashMap<ItemObject, Double> developerModifiedMapPerBug;
    public static LinkedHashMap<String, Double> developerMapPerBug;
    public static Vector outVector;
    public static LinkedHashMap<String, String> issueOpenDateMap;
    public static Vector logVector;
    public static String removePattern;
    public static String releaseDate;
     public static LinkedHashMap<String, Integer> uniqueDevCount;

    public static void main(String[] args)
    {
        wd = new File(PropertyReader.getValue("WD"));
        

        while (true)
        {
            multipleRelease = false;
            System.err.println("");
            //System.err.println("------------------------------------------");
            System.err.println("1: Mucommander");
            System.err.println("2: Argouml");
            System.err.println("3: JabRef");
            System.err.println("4: Jedit");

            System.err.println("");

            System.err.println("25: Enough Now Lets Go and Sleep !!");

            System.err.println("");
            System.err.println("Select Option:");
            String choice = getInputString();
            int key;
            switch (Integer.parseInt(choice))
            {
                //Mucommander 
                case 1:
                    projectName = "MuCommander";
                    getPathConfiguration("MuCommander");
                    filterdType = "NonFiltered";
                    runRecommender();
                    break;

                //ArgoUml 
                case 2:
                    projectName = "ArgoUml";
                    getPathConfiguration("ArgoUml");
                    filterdType = "NonFiltered";
                    removePattern = "ArgoUML0.22/src/argouml/src_new/";
                    releaseDate = "2006-08-08";
                    runRecommender();
                    break;

                //JabRef    
                case 3:
                    projectName = "JabRef";
                    getPathConfiguration("JabRef");
                    filterdType = "NonFiltered";
                    removePattern = "JabRef1.8/src/";
                    runRecommender();
                    break;

                //Jedit    
                case 4:
                    projectName = "Jedit";
                    getPathConfiguration("Jedit");
                    filterdType = "NonFiltered";
                    removePattern = "jEdit4.3/org/gjt/sp/";
                    runRecommender();
                    break;

                case 25:
                    System.exit(0);
                    break;

                default:
                    System.err.println("Woo!! Read Option ");
                    break;
            }
        }

    }

    private static void runRecommender()
    {
        try
        {

            outVector = new Vector();
            uniqeNameMap = new LinkedHashMap();
            createuniqeNameMap(uniqueNamePath);

            //create issueCommitMap
            issueCommitMap = new HashMap();
            createIssueCommitMap(issueCommitPath);
            issueDeveloperMap = new HashMap();
            createIssueDeveloperMap(issueDeveloperPath);


            //create keyword lsit
            keywordList = new Vector();
            createKeywordList();

            //create issue open date map
            //issueOpenDateMap=new LinkedHashMap<String, String>();
            //createIssueOpenDateMap();

            // get recList file one at a time and go through
            File[] recFileList = new File(recListDirPath).listFiles();

            sourceNotFound = new Vector();
            failedToParse = new Vector();
            developerTotalCountMap = new LinkedHashMap<String, Double>();

            //create sourcefilePath List
            filePathList = new ArrayList<ItemObject>();
            walkSourceDirectory(sourceCodeDirPath);

             uniqueDevCount = new LinkedHashMap<String, Integer>();
            logVector = new Vector();
            for (int i = 0; i < recFileList.length; i++)
            {

                String bugName = "";
                String absPath = "";
                String fileFullName = "";
                String fileName = "";
                String fileSourceCodePath = "";
                String xmlFilePath = "";
                int commitNo = 0;
                String issueNo;
                Double involvedInBugSolve = 0.0;

                bugName = recFileList[i].getName();
                absPath = recFileList[i].getAbsolutePath();

                if (!bugName.equals(".svn") && !bugName.equals(".DS_Store"))
                {
                    try
                    {
                        //get the bug number and commit number where it was fixed....
                        commitNo = Integer.parseInt(issueCommitMap.get(bugName.replaceAll("\\..*", "")).toString());
                        issueNo = bugName.replaceAll("\\..*", "");
                        System.err.println("Issue#" + issueNo + "  Solved Commit#" + commitNo + " Actual Developer: " + issueDeveloperMap.get(issueNo).toString());
                        logVector.add("Issue#" + issueNo + "  Solved Commit#" + commitNo + " Actual Developer: " + issueDeveloperMap.get(issueNo).toString());
                        //update source revesio
                        //updateSouceCode(commitNo - 1);
                       // if (issueDeveloperMap.get(issueNo).toString().equals("mvw"))
                        if(issueDeveloperMap.get(issueNo).toString().equals("mvw") && i%2==0 )
                        {
                            continue;
                        }

                        FileInputStream fstream = new FileInputStream(absPath);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;

                        developerModifiedMapPerBug = new LinkedHashMap<ItemObject, Double>();

                        //Read File Line By Line
                        Integer filePos = 0;
                        Vector authorList = new Vector();
                        //while ((strLine = br.readLine()) != null )
                        while ((strLine = br.readLine()) != null && filePos < 5)
                        {
                            filePos++;
                            developerMapPerBug = new LinkedHashMap<String, Double>();
                            fileFullName = strLine;

                            System.err.println("\t" + filePos + ": " + fileFullName);
                            logVector.add("\t" + filePos + ": " + fileFullName);
                            String commitFileName = fileFullName.replaceAll(removePattern, "").trim();
                            logVector.add("\t" + commitFileName);
                            System.err.println("\t" + commitFileName);

                            developerCommitCount = new LinkedHashMap();

                            SourceFileCommitInfo sourceFileCommitInfo = new SourceFileCommitInfo(commitFileName, svnLogPath, commitNo, keywordList, releaseDate);
                            involvedInBugSolve = sourceFileCommitInfo.isFileInvolvedInBugSolved();

                            System.err.println("\t#" + fileFullName + "\t" + involvedInBugSolve);
                            logVector.add("\t#" + fileFullName + "\t" + involvedInBugSolve);
                            System.err.println("\t" + developerCommitCount);
                            logVector.add("\t" + developerCommitCount);

                            createDeveloperModifiedMapPerBug(developerCommitCount);


                            System.err.println("\t**Committers>> " + developerMapPerBug);
                            logVector.add("\t**Committers>> " + developerMapPerBug);
                            //********************************************************
                            Pattern p = Pattern.compile("/");
                            Matcher matcher = p.matcher(fileFullName);
                            int end = 0;
                            while (matcher.find())
                            {
                                end = matcher.end();
                                fileName = fileFullName.substring(end);
                            }
                            if (!fileName.equals(""))
                            {
                                fileSourceCodePath = getFileSourceCodePath(fileName);
                                //go one level back
                                if (fileSourceCodePath.equals(""))
                                {
                                    int key = end - 2;
                                    String name = "";
                                    while (fileFullName.charAt(key) != '/')
                                    {
                                        name += fileFullName.charAt(key);
                                        key--;
                                    }
                                    name = new StringBuffer(name).reverse().toString().trim() + ".java";
                                    fileSourceCodePath = getFileSourceCodePath(name);
                                }

                                if (!fileSourceCodePath.equals(""))
                                {
                                    System.err.println("\t" + fileSourceCodePath);
                                    xmlFilePath = convertSrc2SrcXml(fileSourceCodePath);
                                    if (!xmlFilePath.equals(""))
                                    {
                                        //System.err.println(xmlFilePath);
                                        File xmlFile = new File(xmlFilePath);
                                        if (xmlFile.isFile())
                                        {
                                            //Get AuthorList
                                            SrcXMlParser srcXMlParser = new SrcXMlParser(xmlFile, projectName);
                                            authorList = srcXMlParser.parseXmlFile();

                                            if (!srcXMlParser.getFailedLogVector().isEmpty())
                                            {
                                                failedToParse.add(srcXMlParser.getFailedLogVector().get(0));
                                            }

                                            String output = "";
                                            Vector authorTemp = new Vector();
                                            Vector uniquAuthor = new Vector();
                                            //System.err.println(authorList);

                                            for (int k = 0; k < authorList.size(); k++)
                                            {
                                                if (!authorList.get(k).equals(""))
                                                {
                                                    String author = authorList.get(k).toString();
                                                    author = checkIdentitiyInUniqueNameMap(author);
                                                    if (!uniquAuthor.contains(author))
                                                    {
                                                        uniquAuthor.add(author);
                                                    }
                                                }
                                            }
                                            authorList.clear();
                                            authorList = uniquAuthor;

                                            System.err.println("\t**authors " + authorList);
                                            logVector.add("\t**authors " + authorList);
                                            System.err.println("");

                                        }
                                        else
                                        {
                                            System.err.println("Xml File Not Created");
                                            logVector.add("Xml File Not Created");
                                            continue;
                                        }
                                    }
                                    else
                                    {
                                        System.err.println("Xml File Not Created");
                                        logVector.add("Xml File Not Created");
                                        continue;
                                    }
                                }
                                else
                                {
                                    System.err.println("Source File Not Found");
                                    logVector.add("Source File Not Found");
                                }
                            }
                            else
                            {
                                System.err.println("Source File Not Found");
                                logVector.add("Source File Not Found");
                                sourceNotFound.add(fileFullName);
                                continue;
                            }

                            ///combined both list fairly
                            Object[] keyArray = developerMapPerBug.keySet().toArray();
                            developerMapPerBug.clear();
                            int key = 0;
                            int a = 0;
                            int c = 0;
                            while (key < 10)
                            {

//                                 //committer first
//                                if (c < keyArray.length)
//                                {
//                                    if (!developerMapPerBug.containsKey(keyArray[c].toString()))
//                                    {
//                                        developerMapPerBug.put(keyArray[c].toString(), new Double("1"));
//                                        //c++;
//                                    }
//                                    c++;
//                                }
//                                
                              //author first
                                if (a < authorList.size())
                                {
                                    if (!developerMapPerBug.containsKey(authorList.get(a).toString()))
                                    {
                                        developerMapPerBug.put(authorList.get(a).toString(), new Double("1"));
                                        //a++;
                                    }
                                    a++;
                                }
                               
                                

                                key++;
                            }
                            System.err.println("\tDList>>" + developerMapPerBug);
//                            logVector.add("\tDList>>" + developerMapPerBug);
//                            System.err.println("");
//
//
//                            //System.err.println("\t" + developerMapPerBug);
                            String fileAuthorList = getFileAuthorList(developerMapPerBug);
                            if (!fileAuthorList.equals(""))
                            {
                                //LSI+BugProne ranking
                                //developerModifiedMapPerBug.put(fileAuthorList, (involvedInBugSolve+filePos.doubleValue())/2 );

                                //bugprone ranking
                                ItemObject io = new ItemObject(filePos.intValue(), fileAuthorList);
                                developerModifiedMapPerBug.put(io, involvedInBugSolve);
                            }

                        }
                        //Close the input stream
                        in.close();

                        //BugProneness ranking
                        //System.err.println("\t" + developerModifiedMapPerBug);
                        logVector.add("\t" + developerModifiedMapPerBug);
                        System.err.println("size:" + developerModifiedMapPerBug.size());

                        List<Map.Entry<ItemObject, Double>> entries = new ArrayList<Map.Entry<ItemObject, Double>>(developerModifiedMapPerBug.entrySet());
                        Collections.sort(entries, new Comparator<Map.Entry<ItemObject, Double>>()
                        {
                            public int compare(Map.Entry<ItemObject, Double> a, Map.Entry<ItemObject, Double> b)
                            {
                                //return a.getValue().compareTo(b.getValue());
                                Double res = b.getValue() - a.getValue();
                                return res.intValue();
                            }
                        });

                        LinkedHashMap<ItemObject, Double> sortedMap = new LinkedHashMap<ItemObject, Double>();
                        for (Map.Entry<ItemObject, Double> entry : entries)
                        {
                            sortedMap.put(entry.getKey(), entry.getValue());
                        }
                        developerModifiedMapPerBug.clear();
                        developerModifiedMapPerBug = sortedMap;

                        //sorted list
                        printItemObjectMap(developerModifiedMapPerBug);
                        System.err.println("");
                        logVector.add("\t" + developerModifiedMapPerBug);

                        Vector matrix = createColumnMatrix(developerModifiedMapPerBug, 15);
                        //Vector matrix=createRowMatrix(developerModifiedMapPerBug);

                        System.err.println("\t" + matrix);
                        logVector.add("\t" + matrix);
                        System.err.println("");


                        String dev = issueDeveloperMap.get(issueNo).toString();
                        int pos = 0;
                        if (dev.contains(","))
                        {
                            String[] split = dev.split(",");
                            for (int d = 0; d < split.length; d++)
                            {
                                if (matrix.indexOf(split[d]) > -1)
                                {
                                    pos = matrix.indexOf(split[d]) + 1;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            pos = matrix.indexOf(dev) + 1;
                        }

                        if (pos == 0)
                        {
                            pos = 999;
                        }

                        System.err.println(issueNo + "\t" + pos);
                        logVector.add(issueNo + "\t" + pos);
                        outVector.add(issueNo + "\t" + pos);
                        System.err.println("");
                        
                        
                        
                          //for unique in top k
                        if (pos ==1)
                        {
                            if (uniqueDevCount.containsKey(issueDeveloperMap.get(issueNo).toString()))
                            {

                                Integer c = uniqueDevCount.get(issueDeveloperMap.get(issueNo).toString());
                                uniqueDevCount.put(issueDeveloperMap.get(issueNo).toString(), c + 1);
                            }
                            else
                            {
                                uniqueDevCount.put(issueDeveloperMap.get(issueNo).toString(), 1);
                            }

                        }

                    }
                    catch (Exception e)
                    {//Catch exception if any
                        e.printStackTrace();
                        System.err.println("Error: " + e.getMessage());
                    }
                }

            }
            
            System.err.println(uniqueDevCount);
            System.err.println("");
            System.err.println(outVector);
            
            System.err.println(outVector);

            createOutPutFile(outVector, "issueDevOutput");
            createOutPutFile(logVector, "log/" + projectName + new Date());


            if (!sourceNotFound.isEmpty())
            {
                RemoveDuplicateFromVector rdfv = new RemoveDuplicateFromVector();
                sourceNotFound = rdfv.removeDuplicates(sourceNotFound);
                createLog(sourceNotFound, "noSource");

            }
            if (!failedToParse.isEmpty())
            {
                RemoveDuplicateFromVector rdfv = new RemoveDuplicateFromVector();
                failedToParse = rdfv.removeDuplicates(failedToParse);
                createLog(failedToParse, "failed");
            }
            // System.err.println("*****************Create RankList 1********************");
            //  createRankList(rankList1, "single");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }


    }

    // run the srcml tool
    private static String convertSrc2SrcXml(String srcPath)
    {
        Process proc = null;
        String outPutXmlFilePath = "";
        try
        {
            proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (proc != null)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(proc.getOutputStream())), true);
            File srcFile = new File(srcPath);
            outPutXmlFilePath = outputDirPath + "/" + "xml/" + srcFile.getName();

            if (fileTypeValidator.validate(srcFile.getName()))
            {
                String cmd = "src2srcml" + " " + srcPath + " " + "-o" + outPutXmlFilePath;
                out.println(cmd);
            }
            out.println("exit");
            try
            {
                String line;
                while ((line = in.readLine()) != null)
                {
                    System.out.println(line);
                }
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();

                //System.err.println(outPutXmlFilePath);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return outPutXmlFilePath;
    }

    private static String getFileSourceCodePath(String name)
    {

        String srcPath = "";
        name = name.trim();
        for (ItemObject io : filePathList)
        {
            if (io.getFileName().trim().equals(name))
            {
                return io.getFileAbsPath();
            }
        }

        return srcPath;

    }

    private static void walkSourceDirectory(String srcDirRoot)
    {

        // System.err.println(srcDirRoot);
        File root = new File(srcDirRoot);
        File[] list = root.listFiles();

        for (File f : list)
        {
            if (f.isDirectory())
            {
                walkSourceDirectory(f.getAbsolutePath());
                //System.err.println("Dir:" + f.getAbsoluteFile());
            }
            else
            {
                ItemObject io = new ItemObject(f.getName(), f.getAbsolutePath());
                filePathList.add(io);
            }
        }
    }

    private static void createOutPutFile(Vector outputVector, String fileName)
    {
        FileWriter outPutFile = null;
//        File txtFile = new File(outputDirPath + "/" + "formatedData/" + filterdType + "/" + rankList + "/" + bugName);
        File txtFile = new File(outputDirPath + "/" + fileName);
        try
        {
            outPutFile = new FileWriter(txtFile);
            PrintWriter out = new PrintWriter(outPutFile);
            for (int i = 0; i < outputVector.size(); i++)
            {
                out.println(outputVector.get(i));
                //12
                //out.println();
            }
            out.close();
            outPutFile.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }


    }

    private static void createLog(Vector v, String type)
    {
        FileWriter outPutFile = null;
        //Date dt = new Date();
        File txtFile = new File(logPath + "/" + type);
        try
        {
            outPutFile = new FileWriter(txtFile);
            PrintWriter out = new PrintWriter(outPutFile);

            for (int i = 0; i < v.size(); i++)
            {
                out.println(v.get(i).toString());
            }
            out.close();
            outPutFile.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private static void createuniqeNameMap(String uniqueNamePath) throws IOException
    {
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(uniqueNamePath);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                String line = strLine;
                String[] split = line.split("\\t");
                if (split.length > 0)
                {
                    uniqeNameMap.put(split[0], split[1]);
                }
            }


        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SourceFileOwnerFinder.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(SourceFileOwnerFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void createIssueCommitMap(String uniqueNamePath) throws IOException
    {
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(uniqueNamePath);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                String line = strLine;
                String[] split = line.split("\\t");
                if (split.length > 0)
                {
                    issueCommitMap.put(split[0], split[1]);
                }
            }


        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    private static void createKeywordList() throws IOException
    {
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(keywordListPath);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                String line = strLine;
                keywordList.add(line);
            }


        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    public static String checkIdentitiyInUniqueNameMap(String author)
    {
        String s = author;
        Set set = uniqeNameMap.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext())
        {
            Map.Entry m = (Map.Entry) i.next();
            if (s.toUpperCase().contains(m.getKey().toString().toUpperCase()))
            {
                s = m.getValue().toString();
                // System.err.println(author + "************* > " + s);
                break;
            }
        }

        return s;
    }

    public static String checkAuthorInCommitMessage(String msg)
    {
        String s = "";
        Set set = uniqeNameMap.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext())
        {
            Map.Entry m = (Map.Entry) i.next();
            if (msg.toUpperCase().trim().contains(m.getKey().toString().toUpperCase()))
            {
                s = m.getValue().toString();
                break;
            }
        }

        return s;
    }

    public static LinkedHashMap sortLinkedHashMapByValuesDouble(LinkedHashMap passedMap)
    {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        //reverse order
        Collections.reverse(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap =
                new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext())
        {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext())
            {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2))
                {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Double) val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    // for getting input from user
    public static String getInputString()
    {
        String choice = "";
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            choice = bufferedReader.readLine();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return choice;
    }

    private static void createIssueDeveloperMap(String uniqueNamePath) throws IOException
    {
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(uniqueNamePath);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                String line = strLine;
                String[] split = line.split("\\t");
                if (split.length > 0)
                {
                    issueDeveloperMap.put(split[0], split[1]);
                }
            }

        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }

    // getting file path 
    private static void getPathConfiguration(String projectName)
    {

        sourceCodeDirPath = PropertyReader.getValue(projectName + ".sourceCodeDirPath");

        recListDirPath = PropertyReader.getValue(projectName + ".recListPath");
        outputDirPath = PropertyReader.getValue(projectName + ".outputDirPath");
        uniqueNamePath = PropertyReader.getValue(projectName + ".uniqueNamePath");
        logPath = PropertyReader.getValue(projectName + ".logPath");
        genericFileName = PropertyReader.getValue(projectName + ".genericFileName");
        svnLogPath = PropertyReader.getValue(projectName + ".svnLogPath");
        addPath = PropertyReader.getValue(projectName + ".addPath");
        escapePath = PropertyReader.getValue(projectName + ".escapePath");

        startDate = PropertyReader.getValue(projectName + ".startDate");
        endDate = PropertyReader.getValue(projectName + ".endDate");

        issueCommitPath = PropertyReader.getValue(projectName + ".issueCommitPath");
        issueDeveloperPath = PropertyReader.getValue(projectName + ".issueDeveloperPath");
        keywordListPath = PropertyReader.getValue(projectName + ".keywordListPath");
        // issueOpenDatePath=PropertyReader.getValue(projectName + ".issueOpenDatePath");

//        recListIrDirPath = PropertyReader.getValue(projectName + ".recListIrRelatedPath");

        // for multiple release
        if (multipleRelease)
        {
            srcDirVector = new Vector();
            srcDirVector.add(PropertyReader.getValue(projectName + ".sourceCodeDirPath"));
            int prev = Integer.parseInt(PropertyReader.getValue(projectName + ".totalPrev"));

            for (int i = 1; i <= prev; i++)
            {
                srcDirVector.add(PropertyReader.getValue(projectName + ".sourceCodeDirPathPrev" + i));
            }


        }
    }

    private static void createRankList(LinkedHashMap rankList1, String type)
    {
        Set set = rankList1.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext())
        {
            Map.Entry m = (Map.Entry) i.next();
            String bugName = m.getKey().toString();
            LinkedHashMap<String, Integer> temp = (LinkedHashMap<String, Integer>) m.getValue();
            String out = genericFileName;

            Set s = temp.entrySet();
            Iterator k = s.iterator();
            String rank1 = "";

            while (k.hasNext())
            {
                Map.Entry kmap = (Map.Entry) k.next();
                rank1 += "\t" + kmap.getKey() + "\t" + kmap.getValue();

            }
            ///rank1
            Vector o = new Vector();
            o.add(out + rank1);
            // createOutPutFile(o, bugName, type);

        }
    }

    private static void addToArrayByPosition(LinkedHashMap<String, Double> dumpMap)
    {

        Set set = dumpMap.entrySet();
        Iterator i = set.iterator();
        int k = 0;
        LinkedHashMap<String, Double> tempMap;

        while (i.hasNext())
        {
            Map.Entry map = (Map.Entry) i.next();
            if (dumpArray != null && dumpArray.size() > k)
            {
                tempMap = new LinkedHashMap<String, Double>();
                tempMap = (LinkedHashMap<String, Double>) dumpArray.get(k);
                //tempMap.(map.getKey().toString(), (Double) map.getValue());

                if (tempMap.containsKey(map.getKey().toString()))
                {
                    Double val = tempMap.get(map.getKey().toString());
                    tempMap.put(map.getKey().toString(), val + 1);
                }
                else
                {
                    tempMap.put(map.getKey().toString(), 0.0);
                }

                dumpArray.set(k, tempMap);
            }
            else
            {
                tempMap = new LinkedHashMap<String, Double>();
                tempMap.put(map.getKey().toString(), 0.0);
                dumpArray.add(k, tempMap);

            }


            k++;

        }



    }

    private static void createNewRankList()
    {
        LinkedHashMap<String, Double> tempMap;
        String rank = "";
        Double maxSize = new Double(dumpArray.size());

        for (int i = 0; i < dumpArray.size(); i++)
        {
            tempMap = (LinkedHashMap<String, Double>) dumpArray.get(i);
            Set set = tempMap.entrySet();
            Iterator k = set.iterator();
            Double maxvalue = (Collections.max(tempMap.values()));
            while (k.hasNext())
            {
                Map.Entry kmap = (Map.Entry) k.next();
                if (kmap.getValue() == maxvalue)
                {
                    rank += "\t" + kmap.getKey() + "\t" + maxSize;
                    maxSize--;
                    break;
                }

            }

        }

        System.err.println(rank);

    }

    private static void createDeveloperModifiedMapPerBug(HashMap developerCommitCount)
    {

        Set set = developerCommitCount.entrySet();
        Iterator k = set.iterator();
        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            if (developerMapPerBug.containsKey(kmap.getValue()))
            {
                Double val = developerMapPerBug.get(kmap.getValue());
                developerMapPerBug.put(kmap.getValue().toString(), val + 1);

            }
            else
            {
                developerMapPerBug.put(kmap.getValue().toString(), new Double("1"));
            }

        }

    }

    private static int findDeveloperPosition(LinkedHashMap<String, Double> developerMapPerBug, String issueNo)
    {
        int position = 0;
        String actualDeveloper = issueDeveloperMap.get(issueNo).toString();

        Set set = developerMapPerBug.entrySet();
        Iterator k = set.iterator();
        int i = 1;
        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            if (actualDeveloper.toUpperCase().contains(kmap.getKey().toString().toUpperCase()))
            {
                position = i;
                break;
            }
            i++;
        }


        return position;
    }

    private static String getFileAuthorList(LinkedHashMap<String, Double> developerMapPerBug)
    {
        String authorList = "";
        Set set = developerMapPerBug.entrySet();
        Iterator k = set.iterator();
        int i = 1;
        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            authorList += "\t" + kmap.getKey().toString();
        }

        return authorList;
    }

    private static Vector createColumnMatrix(LinkedHashMap<ItemObject, Double> developerModifiedMapPerBug, Integer maxPosition)
    {
        Vector bugDeveloperVector = new Vector();

        for (int pos = 0; pos <= maxPosition; pos++)
        {
            LinkedHashMap<String, Double> temp = new LinkedHashMap();

            Set set = developerModifiedMapPerBug.entrySet();
            Iterator k = set.iterator();
            while (k.hasNext())
            {
                Map.Entry kmap = (Map.Entry) k.next();
                ItemObject io = (ItemObject) kmap.getKey();
                String[] authors = io.getDevs().toString().trim().split("\t");

                if (pos <= (authors.length - 1))
                {

                    if (temp.containsKey(authors[pos]))
                    {
                        Double val = temp.get(authors[pos]);
                        temp.put(authors[pos], val + 1);
                    }
                    else
                    {
                        temp.put(authors[pos], new Double("1"));
                    }
                }
            }

            System.err.println("\t#" + (pos + 1) + "\t" + temp);

            set = temp.entrySet();
            k = set.iterator();
            while (k.hasNext())
            {
                Map.Entry kmap = (Map.Entry) k.next();
                if (!bugDeveloperVector.contains(kmap.getKey()))
                {
                    bugDeveloperVector.add(kmap.getKey());
                }

            }
        }

        System.err.println("");

        return bugDeveloperVector;

    }

    //row wise count
    private static Vector createRowMatrix(LinkedHashMap<String, Double> developerModifiedMapPerBug)
    {
        //System.err.println("");
        Vector bugDeveloperVector = new Vector();
        LinkedHashMap<Integer, String> temp = new LinkedHashMap();

        Set set = developerModifiedMapPerBug.entrySet();
        Iterator k = set.iterator();


        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            String[] authors = kmap.getKey().toString().trim().split("\t");
            for (int i = 0; i < authors.length; i++)
            {
//                if (temp.containsKey(i))
//                {
//                    String tempAuthors[] = temp.get(i).toString().trim().split("\t");
//                    List<String> aList = Arrays.asList(tempAuthors);
//
//                    if (!aList.contains(authors[i]))
//                    {
//                        temp.put(i, temp.get(i) + "\t" + authors[i]);
//                    }
//
//                }
//                else
//                {
//                    temp.put(i, authors[i]);
//                }

                if (!bugDeveloperVector.contains(authors[i]))
                {
                    bugDeveloperVector.add(authors[i]);
                }


            }

        }

//        System.err.println("\t" + temp);
//
//        set = temp.entrySet();
//        k = set.iterator();
//        while (k.hasNext())
//        {
//            Map.Entry kmap = (Map.Entry) k.next();
//            String[] authors = kmap.getValue().toString().trim().split("\t");
//            for (int i = 0; i < authors.length; i++)
//            {
//                bugDeveloperVector.add(authors[i]);
//            }
//        }
//
//        System.err.println("");


        return bugDeveloperVector;

    }

    //ICSM matrix
    private static Vector createICSMMatrix(LinkedHashMap<String, Double> developerModifiedMapPerBug)
    {
        //System.err.println("");
        Vector bugDeveloperVector = new Vector();
        LinkedHashMap<String, Double> temp = new LinkedHashMap();

        Set set = developerModifiedMapPerBug.entrySet();
        Iterator k = set.iterator();

        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            String[] authors = kmap.getKey().toString().trim().split("\t");
            for (int i = 0; i < authors.length; i++)
            {
                if (temp.containsKey(authors[i].toString()))
                {
                    Double prev = temp.get(authors[i].toString());
                    temp.put(authors[i].toString(), prev + 1);

                }
                else
                {
                    temp.put(authors[i].toString(), new Double("1"));
                }


            }

        }

        List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(temp.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b)
            {
                //return a.getValue().compareTo(b.getValue());
                Double res = b.getValue() - a.getValue();
                return res.intValue();

            }
        });

        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : entries)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        temp.clear();
        temp = sortedMap;


        System.err.println("\t" + temp);

        set = temp.entrySet();
        k = set.iterator();
        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            bugDeveloperVector.add(kmap.getKey());
        }

        // System.err.println("");


        return bugDeveloperVector;

    }

    private static void updateSouceCode(int revNo)
    {
        /* build up command and launch */
        Process proc = null;
        try
        {
            proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (proc != null)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(proc.getOutputStream())), true);

            String cmd = "cd " + sourceCodeDirPath;
            out.println(cmd);

            cmd = "svn update  -r " + revNo;
            out.println(cmd);

            out.println("exit");
            try
            {
                String line;
                String wLine = "";
                while ((line = in.readLine()) != null)
                {
                    //wLine=line;
                    //System.out.println(line);
                }
                System.err.println(wLine);
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();
                System.err.println("\t>>Souce code udpate to rev no: " + revNo);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private static void printItemObjectMap(LinkedHashMap<ItemObject, Double> developerModifiedMapPerBug)
    {
        Set set = developerModifiedMapPerBug.entrySet();
        Iterator k = set.iterator();
        while (k.hasNext())
        {
            Map.Entry kmap = (Map.Entry) k.next();
            ItemObject io = (ItemObject) kmap.getKey();
            System.err.println("\t" + io.getPos() + ":" + io.getDevs() + " >> " + kmap.getValue());
        }
    }
}
