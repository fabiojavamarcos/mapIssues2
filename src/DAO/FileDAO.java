package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import util.DBUtil;

public class FileDAO {
	private static FileDAO instancia;
	private String dbcon;
	private String user;
	private String pswd;
	
	private FileDAO(String dbcon, String user, String pswd){
		this.dbcon = dbcon;
		this.user = user;
		this.pswd = pswd;
	}
	
	public static FileDAO getInstancia( String dbcon, String user, String pswd ){
		if (instancia == null){
			instancia = new FileDAO(dbcon, user, pswd);
		}
		return instancia;
	}
	

	public boolean insertPrIssue(String pr, String issue, String issueTitle2, String issueBody2, String issueComments2, String issueTitleLink2, String issueBodyLink2, String issueCommentsLink2, int isPR2, int isTrain2, String commitMessage2, String prComments2, String projName ) {
		// TODO Auto-generated method stub
		Connection con = DBUtil.getConnection(dbcon, user, pswd);
		int count = 0;
		
		try {
			Statement comandoSql = con.createStatement();
			
			String sql = "insert into pr_issue values ('" + pr + "','" + issue + "','" + issueTitle2 + "','" + issueBody2 + "','" + issueComments2 + "','" + issueTitleLink2 + "','" + issueBodyLink2 + "','" + issueCommentsLink2 +"','"+  isTrain2 + "','" + commitMessage2 +"','"+  isPR2 + "','"+ prComments2+"','"+projName+"' )" ;  
			

			System.out.println(sql);
			
			count = comandoSql.executeUpdate(sql);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if (count >0)
			return true;
		else
			return false;
	}
	
}

