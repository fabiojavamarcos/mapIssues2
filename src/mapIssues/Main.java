package mapIssues;

import control.IssueMapper;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String user		= args[0];
		String pswd		= args[1];
		String db		= args[2];
		String file     = args[3];
		String projName = args[4]; 
		String cases 	= args[5];
		IssueMapper c = new IssueMapper(user, pswd, db, file, projName, cases);
	}

}
