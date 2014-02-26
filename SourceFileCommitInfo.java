/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.recommendation.src;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author kml
 */
public class SourceFileCommitInfo
{

    private String soruceFileName;
    private String commitLogPath;
    private Date startDate;
    private Date endDate;
    private int commitNo;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Vector keywordList;
    private Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    private boolean isInvolvedInBugSolve;
    private Double count=0.0;
    private String issueOpenDate;
    private Double totalCommit = 0.0;
    private String releaseDate;

    //private String commitDate;
    public SourceFileCommitInfo(String fileName, String logpath, int commitNo, Vector keywordList,String releaseDate)
    {

        this.soruceFileName = fileName.trim();
        this.commitLogPath = logpath.trim();
        this.commitNo = commitNo;
        this.keywordList = keywordList;
        this.issueOpenDate = issueOpenDate;
        this.releaseDate=releaseDate;
    }

    public Double isFileInvolvedInBugSolved()
    {
        this.count = 0.0;
        this.createCommitHistoryByCommit();
        System.err.println("");
        System.err.println("\tTotal Commits: " + totalCommit);
        System.err.println("\tTotal IFC:" + count);
        return this.count;
        //return this.count/this.totalCommit;
    }

    private String createCommitHistoryByCommit()
    {
        String commitMessage = "";
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = (Document) db.parse(new File(this.commitLogPath));

            NodeList nl = dom.getElementsByTagName("logentry");
            int size = nl.getLength();
            int prevCount = 0;
            for (int i = 0; i < size; i++)
            {
                Node n = nl.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) n;
                    int revisionNo = Integer.parseInt(eElement.getAttribute("revision"));
                    
//                     String commitDate = getTagValue("date", eElement);
//                     commitDate = commitDate.replaceAll("T.*", "");
//
//                     Date cDate = df.parse(commitDate);
//                     Date rDate = df.parse(releaseDate);
                     
                    //if (revisionNo < this.commitNo )  
                   if (revisionNo < this.commitNo && prevCount < 20)
                    //if (cDate.before(rDate) && prevCount < 20 )
                    {
                       
                        //get the list of file commited in this revision
                        NodeList pathNodeList = eElement.getElementsByTagName("path");
                        for (int k = 0; k < pathNodeList.getLength(); k++)
                        {
                            String fileName = "";
                            Node path = pathNodeList.item(k);
                            if (!path.getTextContent().equals(""))
                            {
                                FileTypeValidator fileTypeValidator = new FileTypeValidator();
                                //validate java file
                                if (fileTypeValidator.validate(path.getTextContent()))
                                {
                                    fileName = path.getTextContent().trim();
                                    //System.err.println(fileName);

                                    if (fileName.trim().contains(this.soruceFileName.trim()))
                                    {
                                        totalCommit++;
                                        System.err.print("\t"+revisionNo);
                                        commitMessage = getTagValue("msg", eElement);
                                        boolean found = false;
                                        //  System.err.println(commitMessage);
                                        if (commitMessage != null)
                                        {

                                            for (int m = 0; m < this.keywordList.size(); m++)
                                            {
                                                if (commitMessage.toUpperCase().contains(this.keywordList.get(m).toString().toUpperCase())
                                                        && !commitMessage.toUpperCase().contains("STYLE ISSUES"))
                                                {
                                                    if (!found)
                                                    {
                                                        count++;
                                                    }
                                                    //extra    code need to be comment out later
                                                    if (!SourceFileOwnerFinder.developerCommitCount.containsKey(eElement.getAttribute("revision")))
                                                    {
                                                        //here author may need to change later to consider author in commit message
                                                        String author = getTagValue("author", eElement);
                                                        author = SourceFileOwnerFinder.checkIdentitiyInUniqueNameMap(author);
                                                        if (!SourceFileOwnerFinder.checkAuthorInCommitMessage(commitMessage).equals(""))
                                                        {
                                                            author = SourceFileOwnerFinder.checkAuthorInCommitMessage(commitMessage);
                                                        }
                                                        SourceFileOwnerFinder.developerCommitCount.put(eElement.getAttribute("revision"), author);
                                                    }

                                                    found = true;

                                                }
                                            }
                                        }

                                        if (found)
                                        {
                                            prevCount++;
                                        }


                                    }
                                }
                            }
                        }


                    }


                }

            }

        }
        catch (ParserConfigurationException ex)
        {
            ex.printStackTrace();
        }
        catch (SAXException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        

        return commitMessage;

    }

    private static String getTagValue(String sTag, Element eElement)
    {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        if (nlList.getLength() > 0)
        {
            Node nValue = (Node) nlList.item(0);

            return nValue.getNodeValue();
        }
        else
        {
            return null;
        }
    }
}
