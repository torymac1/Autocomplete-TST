package servlet;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;  
import net.sf.json.JSONObject;  
import tst.TST;

/**
 * Servlet implementation class CheckMailServlet
 */
@WebServlet("/CheckMailServlet")
public class SearchEngines extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static TST tst_allifiliations = null;
	private static TST tst_authors = null;
    private static TST tst_conferences = null;  
    private static TST tst_fields= null;
    private static TST tst_journals= null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    long size=0;
	public SearchEngines() throws  IOException{
    	super();
    	String path=this.getClass().getClassLoader().getResource("/").getPath().replace("WEB-INF/classes/", "");
    	if (tst_allifiliations == null) {
    		tst_allifiliations = new TST();
    		File allifiliations=new File(path.concat("affiliations.csv"));
			InputStreamReader read_allifiliations = new InputStreamReader (new FileInputStream(allifiliations),"UTF-8");
			BufferedReader reader_allifiliations=new BufferedReader(read_allifiliations);
    		String s_allifiliations=reader_allifiliations.readLine();
		    int k=0;
			while (k<=10000000 && s_allifiliations!=null) {

					String[] n=read_affiliations(s_allifiliations);
					if (n!=null){
						tst_allifiliations.insert(n[0], Long.parseLong(n[1]));
			            //tst_allifiliations.insert(n[2]+":"+n[0],Long.parseLong(n[1]));
					}
		            s_allifiliations=reader_allifiliations.readLine();
		            ++k;
		    }
			System.out.println("Allifiliation Finished!!!!!!!!!!!!!!!!!!!");
		    reader_allifiliations.close();
    	}
    	
    	if (tst_authors==null){
    		tst_authors= new TST();
    		File authors=new File(path.concat("authors50.csv"));
    		System.out.println(path.concat("authors50.csv"));
			InputStreamReader read_authors = new InputStreamReader (new FileInputStream(authors),"UTF-8");
			BufferedReader reader_authors=new BufferedReader(read_authors);
    		String s_authors=reader_authors.readLine();
    		int k=0;
    		while (k<5000000 && s_authors!=null){
    			String[] n=read_authors(s_authors);
    			if (n!=null){
    				tst_authors.insert(n[0]+":"+n[1], Long.parseLong(n[2]));
    			}
    			s_authors=reader_authors.readLine();
    			++k;
    		}
    		System.out.println("Authors Finished!!!!!!!!!!!!!!!!!!!");
    		reader_authors.close();
    		
    	}
    	
    	if (tst_conferences == null){
    		tst_conferences= new TST();
    		File conferences=new File(path.concat("ConferenceSeries.csv"));
			InputStreamReader read_conferences = new InputStreamReader (new FileInputStream(conferences),"UTF-8");
			BufferedReader reader_conferences=new BufferedReader(read_conferences);
			String s_conferences=reader_conferences.readLine();
			int k=0;
			while (k<500000 && s_conferences!=null){
				String[] n=read_others(s_conferences);
				if (n!=null){
					tst_conferences.insert(n[0].toLowerCase()+":"+n[1],100-n[0].length());
				}
				s_conferences=reader_conferences.readLine();
				++k;
			}
			System.out.println("Conferences Finished!!!!!!!!!!!!!!!!!!!");
			reader_conferences.close();
    	}
    	
    	if (tst_fields == null){
    		tst_fields= new TST();
    		File fields=new File(path.concat("FieldsOfStudy.csv"));
			InputStreamReader read_fields = new InputStreamReader (new FileInputStream(fields),"UTF-8");
			BufferedReader reader_fields=new BufferedReader(read_fields);
			String s_fields=reader_fields.readLine();
			int k=0;
			while (k<500000 && s_fields!=null){
				String[] n=read_others(s_fields);
				if (n!=null){
					tst_fields.insert(n[0].toLowerCase()+":"+n[1],100-n[0].length());
				}
				s_fields=reader_fields.readLine();
				++k;
			}
			System.out.println("Fields Finished!!!!!!!!!!!!!!!!!!!");
			reader_fields.close();
    	}
    	
    	if (tst_journals == null){
    		tst_journals= new TST();
    		File journals=new File(path.concat("Journals.csv"));
			InputStreamReader read_journals = new InputStreamReader (new FileInputStream(journals),"UTF-8");
			BufferedReader reader_journals=new BufferedReader(read_journals);
			String s_journals=reader_journals.readLine();
			int k=0;
			while (k<500000 && s_journals!=null){
				String[] n=read_others(s_journals);
				if (n!=null){
					tst_journals.insert(n[0].toLowerCase()+":"+n[1],100-n[0].length());
				}
				s_journals=reader_journals.readLine();
				++k;
			}
			System.out.println("Journals Finished!!!!!!!!!!!!!!!!!!!");
			reader_journals.close();
    	}
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession asession=request.getSession();
		String ckeckKey;
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		if(asession!=null){
			JSONObject result_json=new JSONObject();
			ckeckKey=request.getParameter("checkKey");
			if(ckeckKey!=null){
				List<String> match_allifiliations = new ArrayList<String>(); //Allifiliations
				List<String> match_authors=new ArrayList<String>();//Author
				List<String> match_conferences=new ArrayList<String>();
				List<String> match_fields=new ArrayList<String>();
				List<String> match_journals=new ArrayList<String>();
				
				match_allifiliations=tst_allifiliations.find(ckeckKey,8);
				match_authors=tst_authors.find_authors(ckeckKey,8);
				match_conferences=tst_conferences.find(ckeckKey, 8);
				match_fields=tst_fields.find(ckeckKey, 8);
				match_journals=tst_journals.find(ckeckKey,8);
				
				int total_match_allifiliations;
				if (match_allifiliations==null){
					total_match_allifiliations=0;
				}
				else{
					total_match_allifiliations=match_allifiliations.size();
				}
				if (total_match_allifiliations>8){total_match_allifiliations=8;}
				int total_match_authors;
				if (match_authors==null){
					total_match_authors=0;
				}
				else{
					total_match_authors=match_authors.size()/2;
				}
				if (total_match_authors>8){total_match_authors=8;}
				int total_match_conferences;
				if (match_conferences==null){
					total_match_conferences=0;
				}
				else{
					total_match_conferences=match_conferences.size();
				}
				if (total_match_conferences>8){total_match_conferences=8;}
				
				int total_match_fields;
				if (match_fields==null){
					total_match_fields=0;
				}
				else{
					total_match_fields=match_fields.size();
				}
				if (total_match_fields>8){total_match_fields=8;}
				
				int total_match_journals;
				if (match_journals==null){
					total_match_journals=0;
				}
				else{
					total_match_journals=match_journals.size();
				}
				if (total_match_journals>8){total_match_journals=8;}
				
				HttpSession sessions=request.getSession();
				String[] result=new String[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+total_match_journals];
				JSONArray t=new JSONArray();
				for (int i=0;i<total_match_allifiliations;++i){
					JSONObject temp=new JSONObject();
					result[i]=match_allifiliations.get(i);
					int j;
					for (j=0;j<result[i].length();++j){
						if (result[i].charAt(j)==':'){
							break;
						}
					}
					if (j==result[i].length()){
						temp.put("AffiliationName", result[i]);
					}
					else{
						temp.put("AffiliationName", result[i].substring(j+1));
					}
					t.add(temp);
				}
				result_json.accumulate("affiliations",t);
				t.clear();
				
				String[] author_weight=new String[total_match_authors];
				for (int i=0;i<total_match_authors;++i){
					JSONObject temp=new JSONObject();
					result[total_match_allifiliations+i]=match_authors.get(i);
					author_weight[i]=match_authors.get(total_match_authors+i);
					int j;
					for(j=0;j<result[total_match_allifiliations+i].length();++j){
						if (result[total_match_allifiliations+i].charAt(j)==':'){
							break;
						}
					}
					temp.put("AuthorID", result[total_match_allifiliations+i].substring(j+1));
					temp.put("AuthorName", result[total_match_allifiliations+i].substring(0, j));
					temp.put("PaperCounts", author_weight[i]);
					t.add(temp);
				}
				result_json.accumulate("authors",t);
				t.clear();
				
				for (int i=0;i<total_match_conferences;++i){
					JSONObject temp=new JSONObject();
					result[total_match_allifiliations+total_match_authors+i]=match_conferences.get(i);
					int j;
					for (j=0;j<result[total_match_allifiliations+total_match_authors+i].length();++j){
						if (result[total_match_allifiliations+total_match_authors+i].charAt(j)==':'){
							break;
						}
					}
					temp.put("ShortName",result[total_match_allifiliations+total_match_authors+i].substring(0,j));
					temp.put("ConferenceSeriesID",result[total_match_allifiliations+total_match_authors+i].substring(j+1));	
					t.add(temp);
				}
				result_json.accumulate("conf",t);
				t.clear();
				
				
				for (int i=0;i<total_match_fields;++i){
					JSONObject temp=new JSONObject();
					result[total_match_allifiliations+total_match_authors+total_match_conferences+i]=match_fields.get(i);
					int j;
					for (j=0;j<result[total_match_allifiliations+total_match_authors+total_match_conferences+i].length();++j){
						if (result[total_match_allifiliations+total_match_authors+total_match_conferences+i].charAt(j)==':'){
							break;
						}
					}
					temp.put("FieldsOfStudyName",result[total_match_allifiliations+total_match_authors+total_match_conferences+i].substring(0,j));
					temp.put("FieldsOfStudyID",result[total_match_allifiliations+total_match_authors+total_match_conferences+i].substring(j+1));
					t.add(temp);	
				}
				result_json.accumulate("field",t);
				t.clear();
				
				for (int i=0;i<total_match_journals;++i){
					JSONObject temp=new JSONObject();
					result[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+i]=match_journals.get(i);
					int j;
					for (j=0;j<result[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+i].length();++j){
						if (result[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+i].charAt(j)==':'){
							break;
						}
					}
					temp.put("JournalName",result[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+i].substring(0,j));
					temp.put("JournalID",result[total_match_allifiliations+total_match_authors+total_match_conferences+total_match_fields+i].substring(j+1));
					t.add(temp);	
				}
				result_json.accumulate("journal",t);
				t.clear();
				
				sessions.setAttribute("result",result_json.toString());
				//response.sendRedirect("search.jsp");
				Runtime r=Runtime.getRuntime();
				long first=r.totalMemory();
				long second=r.freeMemory();
				String Mem=Long.toString((first-second)/1024/1024);
				sessions.setAttribute("Memory", Mem);
				sessions.setAttribute("totalsize", size);
				response.sendRedirect("search.jsp");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
	}
	
	public static String[] read_affiliations(String str){
        String strr = str.trim();
        String[] abc = new String[3];
        int i;
        for(i=0;i<strr.length();++i){
        	if (strr.charAt(i)==','){
        		break;
        	}
        }
        abc[0]=strr.substring(1,i-1);
		abc[1]=strr.substring(i+1,strr.length());
		String p=abc[0];
		if(p.contains(" ")==false){
			return null;
		}
		String prefix ="";
		p=p.replace(" of ", " ");p=p.replace(" in ", " ");
		p=p.replace(" at ", " ");p=p.replace(" and ", " ");
		p=p.replace(" the "," ");
		int j=0;
		prefix=prefix+String.valueOf(p.charAt(j));
		for (j=1;j<p.length();++j){
			if (p.charAt(j)==' '){
				prefix=prefix+String.valueOf(p.charAt(j+1));
				
			}
		}
		abc[2]=prefix;
        return abc;
    }
	
	public static String[] read_authors(String str){
		String strr=str.trim();
		String[] abc=new String[3];
		abc[1]=strr.substring(0, 8);//id
		String p=strr.substring(9);
		
		int i;
		for (i=1;i<p.length();++i){
			if (p.charAt(i)==','){
				break;
			}
		}
		abc[0]=p.substring(0,i);//word
		abc[2]=p.substring(i+1);//weight
		
		boolean isEnglish=abc[0].matches("[a-zA-Z]+");
		if (isEnglish==true){
			if (abc[0].contains(" ")==false){
				return null;
			}
		}
		return abc;
	}
	
	public static String[] read_others(String str){
		String strr=str.trim();
		String[] abc=new String[2];
		abc[0]=strr.substring(9);
		abc[1]=strr.substring(0, 8);
		return abc;
	}
}
