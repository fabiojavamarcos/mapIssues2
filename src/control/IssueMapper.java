package control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import DAO.FileDAO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IssueMapper {
	private String pr = "";
	private String prAuthor = "";
	private String issue = "";
	private String issueTitle = "";
	private String issueAuthor = "";
	private String issueBody = "";
	private String issueComments = ""; 
	private String issueTitleLink = "";
	private String issueBodyLink = "";
	private String issueCommentsLink = ""; 
	private String projName = "";
	private int isPR = 0; 
	private int isTrain = 0; 
	private String commitMessage = ""; 
	private String prComments = "";
	private String cases = "LOWER"; // "LOWER" - every text is lower case (good for TF-IDF). "AS IS" cases as is. Templates are not removed. Any other value: removes templates comparing the characters without transform in lower cases.
	
	public IssueMapper ( String user, String pswd, String db, String file, String projName, String cases) {
		readData( user, pswd, db, file, projName, cases);
		
	}
	
	public void Convert1(String file) {
		// TODO Auto-generated constructor stub
	}

	private void readData( String user, String pswd, String db, String file, String projName, String cases) {
		// TODO Auto-generated method stub
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    String s = null;;
		try {
			s = br.readLine();//header!
			s = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// primeira linha do arquivo
		//String source = s;
		//System.out.println("Read: "+s);
		
		while (s != null) {
			System.out.println("linha:"+s);
			//boolean isOk = splitLine(s);
			boolean isOk = splitnew(s);
			
			if (isOk) {
				
				 issueTitle = filter_text(issueTitle, cases);
				 issueBody = filter_text(issueBody, cases);
				 issueComments = filter_text(issueComments, cases);
				 issueTitleLink = filter_text(issueTitleLink, cases);
				 issueBodyLink = filter_text(issueBodyLink, cases);
				 issueCommentsLink = filter_text(issueCommentsLink, cases); 
				  
				 commitMessage = filter_text(commitMessage, cases);
				 prComments = filter_text(prComments, cases);
				 
				System.out.println("issue "+issue);
				System.out.println("IssueTitle "+issueTitle);
				System.out.println("IssueBody "+issueBody);
				System.out.println("IssueComments "+issueComments); 
				System.out.println("IssueTitleLink "+issueTitleLink);
				System.out.println("IssueBodyLink "+issueBodyLink);
				System.out.println("IssueCommentsLink "+issueCommentsLink); 
				System.out.println("isPR "+isPR); 
				System.out.println("isTrain "+isTrain); 
				System.out.println("CommitMessage"+commitMessage);
				
				if (issue.equals("")) {
					issue = "0";
				}
				
				// TODO: may need to change this to reflect info additions in splitnew
				boolean ok = insertPrIssue( user, pswd, db, pr, issue, issueTitle, issueBody, issueComments, issueTitleLink, issueBodyLink, issueCommentsLink, isPR, isTrain, commitMessage, prComments, projName );
				if (!ok)
					System.out.println("error inserting into database : "+pr+"  - "+issue + "  -  "+projName );
				else {
					System.out.println("inserterd into pr_issue : "+pr+"  issue- "+issue + " for project: " + projName );
				}
			}
			try {
				s = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//generateFile();
		try {
			br.close();
			isr.close();
			is.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean splitnew(String s) {
		boolean isOk = false;
		String[] values = s.split("\\s*,\\s*");
		System.out.println( "\nvalues: " + Arrays.toString(values) );

		
		//String[] values = s.split(",");
		try {
		pr = values[0];
		System.out.println( "pr: " + pr );
		
		if (pr.equals("3050")) {
			System.out.println("debug");
		}
		//String issueClosedDate = values[1];
		//String issueAuthor = values[2];
		issueTitle = values[1];
		System.out.println( "issueTitle: " + issueTitle );
		
		issueAuthor = values[2];

		issueBody = values[3];
		System.out.println( "issueBody: " + issueBody );

		issueComments = values[4];
		System.out.println( "issueComments: " + issueComments );

		//String prClosedDate = values [6];
		//String prAuthor = values[7];
		//String prTitle = values[8];
		//String prBody = values[9];
		prAuthor = values[4];
		
		prComments = values[6];
		System.out.println( "prComments: " + prComments );

		//String commitAuthor = values[11];
		commitMessage = values[7];
		System.out.println( "commitMessage: " + commitMessage );

		//String commidDate = values[13];
		
		isPR = Integer.parseInt(values[8]);  
		System.out.println( "isPR: " + isPR );
		
		String prIssue = values[9];

		issue = values[10];
		issueTitleLink = values[11];
		issueBodyLink = values[12]; 
		issueCommentsLink = values[13];
		isTrain = (int) Float.parseFloat(values[14]); 
		isOk = true;
		}
		catch(Exception e) {
			System.out.println(" line with problems:  separator missing...");
		    e.printStackTrace();
		}
		return isOk;  
		   

	}
	private boolean splitLine(String s) {
		// TODO Auto-generated method stub
		boolean isOk = false;
		String separator = ",";
		int ini = 0;
		int fim = 0;	
				
		fim = s.indexOf(separator);
		if (fim == -1) {
			System.out.println(" line with problems:   separator missing..."+fim);
			return isOk;
		}
		/*	pr issue issueTitle issueBody issueComments  issueTitleLink issueBodyLink issueCommentsLink  isPR   isTrain   commitMessage  
		 pr 
		 issue 
		 issueTitle 
		 issueBody 
		 issueComments  
		 issueTitleLink 
		 issueBodyLink 
		 issueCommentsLink  
		 isPR   
		 isTrain   
		 commitMessage  
		*/
		pr = s.substring(0, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:   separator missing..."+fim);
			return isOk;
		}
		
		String issueClosedDate = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String issueAuthor = s.substring(ini+1, fim);
		ini = fim;
			 
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueTitle = s.substring(ini+1, fim);
		ini = fim;
		
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueBody = s.substring(ini+1, fim);
		ini = fim;	
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueComments = s.substring(ini+1, fim);
		ini = fim;	
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prClosedDate = s.substring(ini+1, fim);
		ini = fim;	
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prAuthor = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prTitle = s.substring(ini+1, fim);
		ini = fim;
			
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prBody = s.substring(ini+1, fim);
		ini = fim;	
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prComments = s.substring(ini+1, fim);
		ini = fim;	
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String commitAuthor = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String commitMessage = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		isPR = Integer.parseInt(s.substring(ini+1, fim));
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		String prIssue = s.substring(ini+1, fim);
		ini = fim;	 
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issue = s.substring(ini+1, fim);
		ini = fim;	 
			
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueTitleLink = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueBodyLink = s.substring(ini+1, fim);
		ini = fim;
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		issueCommentsLink = s.substring(ini+1, fim);
		ini = fim;
		
		
		fim = s.indexOf(separator, ini+1);
		if (fim == -1) {
			System.out.println(" line with problems:  second separator missing..."+fim);
			return isOk;
		}
		
		isTrain = Integer.parseInt(s.substring(ini+1, fim));
		ini = fim;

		 //old = s.substring(comma1+1, s.length());
		
		//System.out.println("API: "+api+" General: "+general + " Old "+ old);
		
		isOk = true;
		
		return isOk;
		
	}

	private boolean insertPrIssue(String user, String pswd, String db, String pr, String issue, String issueTitle2, String issueBody2, String issueComments2, String issueTitleLink2, String issueBodyLink2, String issueCommentsLink2, int isPR2, int isTrain2, String commitMessage2, String prComments2, String projname2) {
		// TODO Auto-generated method stub
		FileDAO fd = FileDAO.getInstancia(db,user,pswd);
		boolean done = fd.insertPrIssue(pr, issue, issueTitle2, issueBody2, issueComments2,  issueTitleLink2, issueBodyLink2, issueCommentsLink2,  isPR2,   isTrain2,   commitMessage2, prComments2, projname2  );
		if (!done) {
			System.out.println("api: "+pr+" - "+issue+"not inserted in database!!!");
		}
		return done;
	}
	
	private String filter_text(String str, String cases) {
		// TODO Auto-generated method stub
		String newstr = str;
		if (cases.equals("LOWER")) {
			newstr = str.toLowerCase();
		}
		newstr = newstr.trim();
		
		if (!cases.equals("AS IS")) { //skip templates removal
			
			newstr = newstr.replaceAll( "http.*?\\s", " " );
			
			// added by Jacob
			//	 	template vvvv
			//	 	newstr = newstr.replaceAll( "", " " );
			
			
			// guava
			newstr = newstr.replaceAll( "\\w+\\*+\\son.*_-+", " " );
			newstr = newstr.replaceAll( "\\s*\\*+\\w+:\\*+\\s*`*.*\\|+\\s*\"*", " " );
			newstr = newstr.replaceAll( "\"*thanks.*google\\s*open\\s*source.*git\\s*commits\\].*need_sender_cla\\s*-->\\s*\\|+", " " );
			newstr = newstr.replaceAll( "\\s+\\(\\[login\\s*here.*author_cla\\s*-->\\s*\\|+", " " );
			newstr = newstr.replaceAll( "\\s*\\*+\\s*<!--\\sneed_author_consent.*cla_yes\\s*-->\\s*\\|*", " " );
			newstr = newstr.replaceAll( "\\s*\\*+\\s*<!--\\sneed_author_consent\\s*-->\\s*\\|+", " " );
			newstr = newstr.replaceAll( "thanks\\s*for\\s*your\\s*pull\\s*request.*google\\s*open\\ssource\\s*project.*contributor.*\\(cla\\)", " " );
			newstr = newstr.replaceAll( "once\\s*youve\\s*signed.*verify\\s* it>", " " );
			newstr = newstr.replaceAll( "####\\s*what\\s*to\\s*do.*\\]\\(", " " );
			newstr = newstr.replaceAll( "@googlebot", " " );
			
			
			// mockito
			newstr = newstr.replaceAll( "by\\s`\\s*\"?", " " );
			newstr = newstr.replaceAll( "\\s*\\d+%.*diff.*>.*merging", " " );
			newstr = newstr.replaceAll( "\\s*\\*+\\d+%\\*+.*\\].\\s\\|+", " " );
			newstr = newstr.replaceAll( "`.*merging.*stmts.*hit.*```.*uncovered\\s+suggestions.*", " " );
			newstr = newstr.replaceAll( "\\*+.*on.*into\\s+\\*+.*on\\s+mockito:master\\*+\\s+\\|+", " " );
			newstr = newstr.replaceAll( "\\s*\\d+%.*diff.*update.*\\|+", " " );
			newstr = newstr.replaceAll( "\"?\\s*\\d+.*methods.*messages.*hits.*misses.*partials.*```.*update\\s*\\[\\w+\\]\\(\\s+\\|+", " " );
			newstr = newstr.replaceAll( "\\s*merging.*coverage.*complexity.*partials.*\\]\\(\\s*\\|+", " " );
			newstr = newstr.replaceAll( "`\\d+%`>", " " );
			newstr = newstr.replaceAll( "\\]\\(\\s*(\\(\\w+\\)\\s*)?into\\s*\\[.*\\]\\(\\s*(\\(\\w+\\))?\\s*", " " );
			newstr = newstr.replaceAll( "@dependabot.*\\|+", " " );
			newstr = newstr.replaceAll( "\\(`release.*\\`\\)\\s*.*\\[comment\\s*docs\\]\\(\\s*\\|+\\s*", " " );
			newstr = newstr.replaceAll( "\"?:\\w+:", " " );
			newstr = newstr.replaceAll( "\\(`release\\/.*`\\).*what\\s*that\\s*means\\]\\(\\s*`\\s*", " " );
			newstr = newstr.replaceAll( "\\s*merging\\s\\d{4}\\s", " " );
			
			
			// presto
			newstr = newstr.replaceAll( "\\s*\\*+\\d+%\\*+>\\s*merging.*into.*lines.*methods.*messages.*hits.*misses.*partials.*by\\s*\\[\\w+\\]", " " );
			newstr = newstr.replaceAll( "\\s*merging.*coverage.*complexity.*partials.*complexity.*", " " );
			newstr = newstr.replaceAll( "\\s*\\d+%.*diff.*merging.*into.*lines.*hits.*partials.*update\\s*\\[\\w+\\]\\(\\s*\\|*", " " );
			
			
			
			ArrayList <String> yourList = new ArrayList();
			yourList.add("[WIP]");
			yourList.add("[wip]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			yourList.add("[ ]");
			yourList.add("[ x ]");
			yourList.add("[x]");
			yourList.add("[x ]");
			yourList.add("[ x]");
			yourList.add("[]");
			
			yourList.add("Tests created for changes (if applicable)");
			yourList.add("Tests created for changes");
			
			yourList.add("Screenshots added in PR description");
			yourList.add("screenshots added");
			
			yourList.add("Ensured that [the git commit message is a good one]");
			
			yourList.add("Check documentation status");	
			yourList.add("Checked documentation");
			
			yourList.add("tests green");
	
			yourList.add("changes in pull request outlined? (what  why  ...)"); 
			yourList.add("what why");
	
			yourList.add("Changes in pull request outlined");
	
			yourList.add("Commits squashed");	
			
			yourList.add("<!--  Describe the changes you have made here: what  why  ...  Link issues by using the following pattern: #");
			yourList.add("<!-- describe the changes you have made here: what  why");
			yourList.add("<!-- describe the changes you have made here:");
			yourList.add("what  why");
			yourList.add("...");       
	
	
			yourList.add("Link issues by using the following pattern: [#333]");
			yourList.add("link issues by using the following pattern:");
			yourList.add("[koppor#47](https://github.com/koppor/jabref/issues/47");
			
			yourList.add("or [koppor#49]");
			yourList.add("or [koppor#");
			yourList.add("[koppor#");
			yourList.add("https://github.com/JabRef/jabref/issues/333");
			yourList.add("https://github.com/koppor/jabref/issues/47");      
			yourList.add("https://github.com/jabref/jabref/issues/");
			yourList.add("https://github.com/koppor/jabref/issues/"); 
			yourList.add("https://github.com/jabref/jabref/pull/");
			yourList.add("https://github.com/jabref/jabref/pull/");
	
			yourList.add("[#");
	
			
			yourList.add("The title of the PR must not reference an issue  because GitHub does not support autolinking there. -->");
			yourList.add("The title of the PR must not reference an issue");  
			yourList.add("because GitHub does not support autolinking there. -->");
	
			
			yourList.add("If you fixed a koppor issue  link it with following pattern");
			yourList.add("If you fixed a koppor issue");
			yourList.add("link it with following pattern");
	
			yourList.add("fixes https://github.com/jabref/jabref/issues/");
			yourList.add("fixes https://github.com/koppor/jabref/issues/");
			yourList.add("Fixes #");
			yourList.add("Fixes  #");
			yourList.add("Fix #");
			yourList.add("fix issue");
			yourList.add("resolve #");
			yourList.add("resolves #");
			yourList.add("followup from #");
			yourList.add("localizationupd");
			yourList.add("githubusercont");
	
			yourList.add("![image](https://user-images.githubusercontent.com/");
			yourList.add("![image](https://user-images.githubusercontent.com/");
			yourList.add("![modification](https://user-images.githubusercontent.com/");
			yourList.add("![modification](https://user-images.githubusercontent.com/");
			yourList.add("![grafik](https://user-images.githubusercontent.com/");
			yourList.add("![grafik](https://user-images.githubusercontent.com/");
			yourList.add("![littlebefore](https://user-images.githubusercontent.com/");
			yourList.add("![preferences](https://user-images.githubusercontent.com/");
			yourList.add("![preferences](https://user-images.githubusercontent.com/");
			yourList.add("![image]");
			yourList.add("![modification]");
			yourList.add("![grafik]");
			yourList.add("![preferences](https://user-images.githubusercontent.com/");
			yourList.add("![preferences]");
			
			yourList.add("https://user-images.githubusercontent.com/");
					
			yourList.add("<!--  - All items with  [ ]  are still a TODO. - All items checked with  [x]  are done. - Remove items not applicable -->");
			yourList.add("<!--  - All items with  [ ]  are still a TODO.");
			yourList.add("<!--  - All items with");
			yourList.add("are still a TODO.");
			yourList.add("All items checked with");
			yourList.add("are done");
			yourList.add("Remove items not applicable -->");
			
			yourList.add("Change in CHANGELOG.md described (if applicable)");
			yourList.add("Change in CHANGELOG.md described");
			yourList.add("for bigger UI changes");
	
	
			
			yourList.add("Manually tested changed features in running JabRef (always required)");
			yourList.add("Manually tested changed features in running JabRef ");
			
			
			yourList.add("Is the information available and up to date?"); 
			
			yourList.add("If not: Issue created at"); 
			yourList.add("Issue created for outdated help page at");
			yourList.add("Internal SQ");
			yourList.add("If you changed the localization: Did you run  gradle localizationUpdate");
			yourList.add("Internal quality assurance");
			
			
			yourList.add("Replace copy and rename");
			
			yourList.add("expandFileName "); 
			yourList.add("shortenFileName"); 
			
			yourList.add("Aux File listener? - [ ] introduce new paper folder?"); 
			yourList.add("Look for all aux files in paper folder"); 
			yourList.add("create icon inside groups menu/groups sidepane or under tools");
			yourList.add("Introduce an EventBus object being passed around. This enables better testing");
			yourList.add("Make  DatabaseChangeEvent  abstract and add subclasses according to  DatbaseChangeEvent");  
			yourList.add("Rewrite the currently existing code to use that event bus instead of");  
			yourList.add("net.sf.jabref.model.database.BibDatabase.addDatabaseChangeListener(DatabaseChangeListener)");  
			yourList.add("net.sf.jabref.model.database.BibDatabase.removeDatabaseChangeListener(DatabaseChangeListener)");
			
			yourList.add("Mostly GUI changes  testing makes not that much sense here");
			yourList.add("Mostly GUI changes testing makes not that much sense here");
			
			
	
			 
			yourList.add("[x] Change in CHANGELOG.md described");
			yourList.add("[x] Tests created for changes");
			yourList.add("[x] Manually tested changed features in running JabRef ");
			yourList.add("[x] Screenshots added in PR description");		
			yourList.add("[x] Ensured that [the git commit message is a good one]");
			yourList.add("[x] Check documentation status");
			yourList.add("[x] tests green?");
			yourList.add("[x] commits squashed?");
			yourList.add("[x] changes in pull request outlined? (what  why  ...)"); 
			
			yourList.add("[ x ] Change in CHANGELOG.md described");
			yourList.add("[ x ] Tests created for changes");
			yourList.add("[ x ] Manually tested changed features in running JabRef ");
			yourList.add("[ x ] Screenshots added in PR description");		
			yourList.add("[ x ] Ensured that [the git commit message is a good one]");
			yourList.add("[ x ] Check documentation status");
			yourList.add("[ x ] tests green?");
			yourList.add("[ x ] commits squashed?");
			yourList.add("[ x ] changes in pull request outlined? (what  why  ...)"); 
			
			yourList.add("https://docs.jabref.org/");		
			yourList.add("https://github.com/joelparkerhenderson/git_commit_message");
			yourList.add("help.jabref.org");
			yourList.add("https://github.com/JabRef/help.jabref.org/issues");
			yourList.add("https://github.com/joelparkerhenderson/git_commit_message");
			yourList.add("https://github.com/JabRef/help.jabref.org/issues");
			yourList.add("<https://github.com/JabRef/user-documentation/issues>"); 
			yourList.add("https://github.com/JabRef/user-documentation/issues"); 		
	
			//- [ ] Change in CHANGELOG.md described
			//- [ ] Tests created for changes
			//- [ ] Manually tested changed features in running JabRef
			//- [ ] Screenshots added in PR description (for bigger UI changes)
			//- [ ] Ensured that [the git commit message is a good one](https://github.com/joelparkerhenderson/git_commit_message)
			//- [ ] Check documentation status (Issue created for outdated help page at [help.jabref.org](https://github.com/JabRef/help.jabref.org/issues)?)
			//"[ ]", "[ x ]", "[x]","change changelogmd described", "tests created changes", "manually tested changed features running jabref", "screenshots added pr description bigger ui changes", "ensured [ git commit message good one ]", "httpsgithubcomjoelparkerhendersongitcommitmessage", "check documentation status issue created outdated help page", "httpsgithubcomjabrefhelpjabreforgissues"]
			//<!-- describe the changes you have made here: what  why  ...       Link issues by using the following pattern: [#333](https://github.com/JabRef/jabref/issues/333) or [koppor#49](https://github.com/koppor/jabref/issues/47).      The title of the PR must not reference an issue  because GitHub does not support autolinking there. -->  Fixes the delete action in the maintable branch and the  do you really want to delete the entry -dialog is converted to JavaFX. Moreover  a few lines of JavaFX-Swing-interaction code in  FXDialog  are deleted since it is no longer needed.  ----  - 
				//[ ] Change in CHANGELOG.md described - [ ] Tests created for changes - [x] Manually tested changed features in running JabRef - [ ] Screenshots added in PR description (for bigger UI changes) - 
				//[ ] Ensured that [the git commit message is a good one](https://github.com/joelparkerhenderson/git_commit_message) - [ ] Check documentation status (Issue created for outdated help page at [help.jabref.org](https://github.com/JabRef/help.jabref.org/issues)?)	    
	
			
			// Jacob Penney additions
			// Guava
			yourList.add( "This code has been reviewed and submitted internally. Feel free to discuss on the PR and we can submit follow-up changes as necessary." );
			yourList.add( "this code has been reviewed and submitted internally feel free to discuss onthe pr and we can submit follow-up changes as necessarycommits");		yourList.add( "filler for non-existent google code issue ");
			yourList.add( "this code has been reviewed and submitted internally feel free to discuss on the pr and we can submit follow-up changes as necessarycommits");
			yourList.add( "this code has been reviewed and submitted internally. feel free to discuss on the pr  and we can submit follow-up changes as necessary.  commits:");
			yourList.add( "moe sync");
			yourList.add( "commits: ===== <p>");
			yourList.add( "filler for non-existent google code issue ");
			yourList.add( "filler for non-existent issue ");
			yourList.add( "_this issue only exists to ensure that github issues have the same ids they had on google code please ignore it_" );
			yourList.add( "\"_[original comment]( posted by **");
			yourList.add( "_[original comment]( posted by **");
			yourList.add( "\"_[original issue]( created by **");
			yourList.add( "_[original issue]( created by **");
			
			// Mockito
			yourList.add( "### problem" );
			yourList.add( "### bugfix" );
			yourList.add( "##### reconnect" );
			yourList.add("> hey > > thanks for the contribution this is awesome> as you may have read project members have somehow an opinionated view on what and how should be> mockito eg we dont want mockito to be a feature bloat> there may be a thorough review with feedback -> code change loop> > which branch : > - on mockito 2x make your pull request target `release/2x`> - on next mockito version make your pull request target `master`>> _this block can be removed_> _something wrong in the template fix it here `github/pull_request_templatemd`check list -  read the [contributing guide]( -  pr should be motivated ie what does it fix why and if relevant how -  if possible / relevant include an example in the description that could help all readers       including project members to get a better picture of the change -  avoid other runtime dependencies -  meaningful commit history  intention is important please rebase your commit history so that each       commit is meaningful and help the people that will explore a change in 2 years -  the pull request follows coding style -  mention `<issue number>` in the description _if relevant_ -  at least one commit should mention `fixes #<issue number>` _if relevant_");
			yourList.add("> hey> > first thanks for reporting in order to help us to classify issue can you make sure the following check boxes are checked ?> > if this is about mockito usage the better way is to reach out to> >  - stackoverflow :   - the mailing-list  :  / mockito@googlegroupscom>    (note mailing-list is moderated to avoid spam)>> _this block can be removed_> _something wrong in the template fix it here `github/issue_templatemd`check that -  the mockito message in the stacktrace have useful information but it didnt help -  the problematic code (if thats possible) is copied here       note that some configuration are impossible to mock via mockito -  provide versions (mockito / jdk / os / any other relevant information) -  provide a [short self contained correct (compilable) example]( of the issue       (same as any question on stackoverflowcom) -  read the [contributing guide](https://githubcom/mockito/mockito/blob/master/github/contributingmd)");	
			yourList.add( "> hey> > " );
			yourList.add( "> hey > > " );
			yourList.add( "\"> hey> > " );
			yourList.add( "\"```" );
			yourList.add( "## [current coverage]( is" );
			yourList.add( "powered by [codecov]" );
			yourList.add( "# [codecov]( report>" );
			yourList.add( "]( into [release/2x](" );
			yourList.add( "]( into [master](" );
			yourList.add( "into **master** will change coverage by" );
			yourList.add( "[![coverage status]( unknown when pulling" );
			yourList.add( "[![coverage status]( remained the same when pulling" );
			yourList.add( "[![sunburst]( no coverage report found for" );
			yourList.add( "** into **master** will increase coverage by" );
			yourList.add( "** into **master** will not affect coverage as of [`" );
			yourList.add( "** into **master** will not effect coverage as of [`" );
			yourList.add( "** into **master** will decrease coverage by" );
			yourList.add( "will not change coverage``` diff@@" );
			yourList.add( "will increase coverage by" );
			yourList.add( "will decrease coverage by" );
			yourList.add( "`]( coverage diff``` diff@@" );
			yourList.add( "> > ( last updated by [" );
			yourList.add( " diff@@            master    " );
			yourList.add( "            master   " );
			yourList.add( "         branches      " );
			yourList.add( " diff @@  files          " );
			yourList.add( "            master    " );
			yourList.add( "```[![sunburst]( ( last updated by [" );
			yourList.add( " via [" );
			yourList.add( "**master** at" );
			yourList.add( "( updated on successful ci builds ||" );
			yourList.add( "[![coverage status]( remained the same when pulling " );
			yourList.add( "![coverage  remained the same when pulling" );
			yourList.add( "[![coverage  unknown when pulling" );
			yourList.add( "[![ no coverage report found for" );
			yourList.add( "## [current  is" );
			yourList.add( "the diff coverage is " );
			yourList.add( "will **not change** coverage> " );
			yourList.add( "will **decrease** coverage " );
			yourList.add( "will **increase** coverage " );
			yourList.add( "no coverage report found for **release/2x** at " );
			yourList.add( "|| one of your ci runs failed on this pull request so dependabot wont merge itdependabot will still automatically merge this pull request if you amend it and your tests pass ||" );
			yourList.add( "               ( updated on successful ci builds ||" );
			yourList.add( "diff@@" );
			yourList.add( "           lines          " );
			yourList.add( "![sunburst]( no coverage report found for" );
			
	
			// Presto
			yourList.add( "looks good ||" );
			yourList.add( " looks good ||" );
			yourList.add( "looks good to me||" );
			yourList.add( "looks good to me ||" );
			yourList.add( "look good ||" );
			yourList.add( "fixed" );
			yourList.add( "merged thanks! ||" );
			yourList.add( "good stuff ||" );
			yourList.add( "merged ||" );
			yourList.add( "do not work on this pull request until  was applied!" );
			yourList.add( "|| this issue has been automatically marked as stale because it has not had recent activity it will be closed if no further activity occurs ||" );
			yourList.add( "this issue has been automatically marked as stale because it has not had any activity in the last 2 years if you feel that this issue is important just comment and the stale tag will be removed otherwise it will be closed in 7 days this is an attempt to ensure that our open issues remain valuable and relevant so that we can keep track of what needs to be done and prioritize the right things ||" );
	
	
			//RxJava
			yourList.add( "based on votes" );
			yourList.add( "based on unanimous votes" );
			yourList.add( "based on the majority of votes" );
			yourList.add( "implemented ||" );
			yourList.add( "[debug] [testeventlogger]" );
			yourList.add( "thanks ||" );
			yourList.add( "thanks! ||" );
			yourList.add( "lgtm ||" );
			yourList.add( "]( into [1x]( by" );
			yourList.add( "]( into [2x]( by" );
			yourList.add( "coverage diff" );
			yourList.add( "@@##" );
			yourList.add( "[![impacted file tree graph]" );
			
			// powertoys templates (fabio)
			yourList.add("Summary of the Pull Request");
			yourList.add("What is this about:");
			yourList.add("This will help us diagnose current and future issues like the one thats linked");
			yourList.add("What is include in the PR");
			yourList.add("###");		yourList.add("##");
			yourList.add("**");
			yourList.add("Microsoft PowerToys version");
			yourList.add("Running as admin");
			yourList.add("Area(s) with issue");
			yourList.add("Steps to reproduce");
			yourList.add("Actual Behavior");
			yourList.add("Description of the new feature / enhancement");
			yourList.add("Scenario when this would be used");
			yourList.add("Supporting information");
			yourList.add("Expected Behavior");
			yourList.add("Utility with translation issue");
			yourList.add("Language affected");
			yourList.add("Actual phrase(s)");
			yourList.add("Other Software");
			yourList.add("How does someone test / validate");
			yourList.add("Testing export functionality");
			yourList.add("Contributor License Agreement (CLA)");
			yourList.add("A CLA must be signed. If not, go over here and sign the CLA");
			yourList.add("Linked issue:");
			yourList.add("Communication:");
			yourList.add("Tests:");
			yourList.add("Installer:");
			yourList.add("Localization:");
			yourList.add("Docs:");
			yourList.add("Binaries:");
			yourList.add("No new binaries");
			yourList.add("YML for signing for new binaries");
			yourList.add("WXS for installer for new binaries");
			yourList.add("Quality Checklist");
			yourList.add("<!-- Enter a brief description/summary of your PR here What does it fix/what does it change/how was it tested (even manually if necessary)? -->");
			yourList.add("<!-- enter a brief description/summary of your pr here. what does it fix/what does it change/how was it tested (even manually  if necessary)? -->");
			yourList.add("<!-- Enter a brief description/summary of your PR here. What does it fix/what does it change/how was it tested (even manually  if necessary)? -->");
			
			yourList.add("<!-- Other than the issue solved is this relevant to any other issues/existing PRs? -->");
			yourList.add("<!-- other than the issue solved  is this relevant to any other issues/existing prs? -->");
			yourList.add("<!-- Other than the issue solved  is this relevant to any other issues/existing PRs? -->");
	
			yourList.add("<!-- please review the items on the pr checklist before submitting-->");
			yourList.add("<!-- please review the items on the pr checklist before submitting-->");
			yourList.add("<!-- Please review the items on the PR checklist before submitting-->");
	
			yourList.add("<!-- provide a more detailed description of the pr other things   or any additional comments/features here -->");
			yourList.add("<!-- Provide a more detailed description of the PR  other things fixed or any additional comments/features here -->");
			yourList.add("<!-- provide a more detailed description of the pr  other things  or any additional comments/features here -->");
			yourList.add("<!-- provide a more detailed description of the pr other things  or any additional comments/features here -->");
			yourList.add("<!-- provide a more detailed description of the pr other things or any additional comments/features here -->");		yourList.add("<!-- Provide a more detailed description of the PR other things fixed or any additional comments/features here -->");
			yourList.add("<!-- Provide a more detailed description of the PR  other things fixed or any additional comments/features here -->");
			yourList.add("<!-- provide a more detailed description of the pr other things   or any additional comments/features here -->");
			yourList.add("detailed description of the pull request / additional comments");
			yourList.add("validation steps performed");		
			
			yourList.add("<!-- Describe how you validated the behavior Add automated tests wherever possible but list manual validation steps taken as well -->");
			yourList.add("<!-- describe how you validated the behavior. add automated tests wherever possible  but list manual validation steps taken as well -->");
			yourList.add("<!-- Describe how you validated the behavior. Add automated tests wherever possible  but list manual validation steps taken as well -->");
			
			yourList.add("summary of the new feature/enhancement");
			yourList.add("summary of the pull request");
			yourList.add("<!--**important: when reporting bsods or security issues do not attach memory dumps logs or traces to github issues");
			yourList.add("instead send dumps/traces to secure@microsoftcom referencing this github issue-->");
			yourList.add("# environment");
			yourList.add("powertoy module for which you are reporting the bug (if applicable)");		
			yourList.add("<!-- whats actually happening? -->");
			yourList.add("screenshots!"); 
			yourList.add("## references");
			yourList.add("## references");
			yourList.add("## pr checklist *");
			yourList.add("## pr checklist*");
			yourList.add("applies to #");
			yourList.add("xxx *  cla signed");
			yourList.add("*  cla signed");
			yourList.add("*  tests added/passed");
			yourList.add("*  requires documentation to be updated");
			yourList.add("*  i ve discussed this with core contributors already");
			yourList.add("xxx");
			yourList.add("##");
			yourList.add("##");
			yourList.add("##");
			yourList.add("**image was set to remote loading**");
			yourList.add("**");
			yourList.add("**");
			yourList.add("**");
			yourList.add("**");
			yourList.add("**");
			yourList.add("**");
			yourList.add("ive discussed this with core contributors in the issue");
			yourList.add("added/updated and all pass");
			yourList.add("added/updated and all pass");
			yourList.add("all end user facing strings can be localized");
			yourList.add("added/ updated");
			yourList.add("any new files are added to wxs / yml");
			yourList.add("[yml for signing]");
			yourList.add("for new binaries");
			yourList.add("[wxs for installer]");
			yourList.add("for new binaries");
			yourList.add("a cla must be signed if not go over [here]");
			yourList.add("and sign the cla");			
			yourList.add("<!-- important: when reporting bsods or security issues do not attach memory dumps logs or traces to github issues");
			yourList.add("instead send dumps/traces to secure@microsoftcom referencing this github issue-->");
			yourList.add("cla signed"); 
			yourList.add("if not go over [here]");
			yourList.add("tests added/passed"); 
			yourList.add("requires documentation to be updated");
			yourList.add("ive discussed this with core contributors already");
			yourList.add("issue number where discussion took place");
			yourList.add("info on pull request_what does this include");
			yourList.add("how does someone test & validate");
			yourList.add("what is this about");
			yourList.add("how does someone test & validate");
			yourList.add("what is this about?");
			yourList.add("cla signed if not go over [here]");
			yourList.add("what does this include?");
			
			// repeat to eliminate again
			yourList.add( "[rxjava-pull-requests " );
			yourList.add( "[rxjava-pull-requests " );
			yourList.add( "[rxjava-pull-requests " );
			
			yourList.add( "]( failurelooks like theres a problem with this pull request ||" );
			yourList.add( "]( failurelooks like theres a problem with this pull request ||" );
			yourList.add( "]( failurelooks like theres a problem with this pull request ||" );
	
			yourList.add( "]( successthis pull request" );
			yourList.add( "]( successthis pull request" );
			yourList.add( "]( successthis pull request" );
			
			yourList.add( "( aborted ||" );
			yourList.add( "( aborted ||" );
			yourList.add( "( aborted ||" );
			
			yourList.add( ":+1: ||" );
			yourList.add( ":+1: ||" );
			yourList.add( ":+1: ||" );
			yourList.add( ":+1: ||" );
			yourList.add( ":+1: ||" );
	
			yourList.add( ":+1:  ||" );
			yourList.add( ":+1:  ||" );
			yourList.add( ":+1:  ||" );
			yourList.add( ":+1:  ||" );
			
	
			yourList.add( "👍 ||" );
			yourList.add( "👍 ||" );
			
			yourList.add( "👍  ||" );
			yourList.add( "👍  ||" );
			yourList.add( "👍  ||" );
			
			
			//guava
			yourList.add( "<!-- ok -->" );
			yourList.add( "kevinb@" );
			yourList.add( "boppenheim@" );
			yourList.add( "cgdecker@" );
			yourList.add( "lowasser@" );
			yourList.add( "@lowasser" );
			yourList.add( "fry@" );
			yourList.add( "jlevy@" );
			yourList.add( "cpovirk@" );
			yourList.add( "@cpovirk" );
			yourList.add( "em@" );
			yourList.add( "ek@" );
			yourList.add( "kak@" );
			yourList.add( "we found a contributor license agreement for you (the sender of this pull request) but were unable to find agreements for the commit author(s)  if you authored these maybe you used a different email address in the git commits than was used to sign the cla" );
			yourList.add( "you are receiving this because you were mentioned> > reply to this email directly view it on github>  or mute the> thread>" );
			yourList.add( "bulk closing all pull requests that are listed as needing cla signing if youd like us to look at your pull request youll need to sign the cla and report back hereif this is a false positive i apologize please reopen the pull request and well have a look" );
			yourList.add( "all (the pull request submitter and all commit authors) clas are signed **but** one or more commits were authored or co-authored by someone other than the pull request submitter" );
			yourList.add( "we need to confirm that all authors are ok with their commits being contributed to this project  please have them confirm that by leaving a comment that contains only `@googlebot i consent` in this pull request" );
			yourList.add( "note to project maintainer:* there may be cases where the author cannot leave a comment or the comment is not properly detected as consent  in those cases you can manually confirm consent of the commit author(s) and set the `cla` label to `yes` (if enabled on your project)" );
			yourList.add( "a googler has manually verified that the clas look good(googler please make sure the reason for overriding the cla status is clearly documented in these comments)" );
			yourList.add( "**googlers: [go here]( for more info**" );
			yourList.add( "ℹ️" );
			yourList.add( "**please visit  to sign**> >" );
			yourList.add( "i signed it! ||" );
			yourList.add( "clas look good thanks! ||" );
			yourList.add( "clas look good thanks!ℹ️  ||" );
			yourList.add( "so theres good news and bad news  the good news is that everyone that needs to sign a cla (the pull request submitter and all commit authors) have done so  everything is all good there  the bad news is that it appears that one or more commits were authored or co-authored by someone other than the pull request submitter  we need to confirm that all authors are ok with their commits being contributed to this project  please have them confirm that here in the pull request*note to project maintainer: this is a terminal state meaning the `cla/google` commit status will not change from this state its up to you to confirm consent of all the commit author(s) set the `cla` label to `yes` (if enabled on your project) and then merge this pull request when appropriate* <!-- need_author_consent --> ||" );
			yourList.add( "* **googlers: [go here]( for more info " );
			yourList.add( "if you authored these maybe you used a different email address in the git commits than was used to sign the cla" );
			yourList.add( "we found a contributor license agreement for you (the sender of this pull request) but were unable to find agreements for all the commit author(s) or co-authors" );
			yourList.add( "ok i wont notify you again about this release but will get in touch when a new version is available if youd rather skip all updates until the next major or minor version let me know by commenting `" );
			yourList.add( "so theres good news and bad news  the good news is that everyone that needs to sign a cla (the pull request submitter and all commit authors) have done so  everything is all good there  the bad news is that it appears that one or more commits were authored or co-authored by someone other than the pull request submitter  we need to confirm that all authors are ok with their commits being contributed to this project  please have them confirm that here in the pull request*note to project maintainer: this is a terminal state meaning the `cla/google` commit status will not change from this state its up to you to confirm consent of all the commit author(s) set the `cla` label to `yes` (if enabled on your project) and then merge this pull request when appropriate" );
			yourList.add( "we need to confirm that all authors are ok with their commits being contributed to this project  please have them confirm that by leaving a comment that contains only `  i consent` in this pull request" );
			
			System.out.println( "Filtering Strings..." );
			
			for (int i =0; i<yourList.size(); i++) {
				String text = "";
				if (cases.equals("LOWER"))
					text = yourList.get(i).toLowerCase();
				else
					text = yourList.get(i);
				int pos = newstr.indexOf(text) ;
				
				if (newstr.indexOf("this code has been reviewed")!= -1) {
					//System.out.println("Debug");
					//System.out.println("comparing: "+text+" - "+newstr);
	
				}			
				
				if (pos != -1) {
					String temp1 = "";
					int tam = text.length();
					//if (isFirst) {
						//temp1 = str.substring(0, pos+tam-1);
						//isFirst = false;
					//} else {
						temp1 = newstr.substring(0, pos);
					//}
					
					newstr = temp1 + " " + newstr.substring(pos+tam, newstr.length());
				}
			}
		}
		return newstr;
	}

}
