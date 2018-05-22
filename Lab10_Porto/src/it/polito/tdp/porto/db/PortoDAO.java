package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				st.close();
				rs.close();
				conn.close();
				return paper;
			}
			st.close();
			rs.close();
			conn.close();

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	/*
	 * OTTENGO TUTTI GLI AUTORI
	 */
	public List<Author> getAutori(AuthorIdMap aMap) {

		final String sql = "SELECT * FROM author";
		List<Author> result = new ArrayList<Author>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(aMap.get(autore));
			}
			
			st.close();
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		return result;
	}
	
	
	/*
	 * Dato l'id ottengo Tutti i coautori.
	 */
	public List<Author> getCoAutoriFromId(int id,AuthorIdMap aMap) {  //SFRUTTA SQL PER L'ELABORAZIONE

		final String sql = "SELECT DISTINCT authorid FROM CREATOR WHERE eprintid IN(SELECT eprintid FROM CREATOR WHERE authorid=?) AND authorid <>?";
		List<Author> result= new ArrayList<Author>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);
			st.setInt(2, id);

			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				int aId=rs.getInt("authorid");
				Author autore = new Author(aId, aMap.getAuthor(aId).getFirstname(), aMap.getAuthor(aId).getLastname());
				result.add(autore);
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		return result;
	}

	public boolean checkConn(Author a, Author autore) {
		final String sql = "SELECT DISTINCT authorid FROM CREATOR WHERE eprintid IN(SELECT eprintid FROM CREATOR WHERE authorid=?) AND authorid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			st.setInt(2, autore.getId());

			ResultSet rs = st.executeQuery();

			if(rs.next()&&autore.getId()==rs.getInt("authorid")) {
				st.close();
				rs.close();
				conn.close();
				return true;
			}
			st.close();
			rs.close();
			conn.close();	
			return false;
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper getArtic(Author a,Author b) {
		final String sql="SELECT C1.eprintid FROM creator as C1, creator as C2 WHERE C1.eprintid=C2.eprintid AND C1.authorid=? AND C2.authorid=? LIMIT 1";
		Paper articolo=null;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a.getId());
			st.setInt(2, b.getId());

			ResultSet rs = st.executeQuery();

			if(rs.next())
				articolo=this.getArticolo(rs.getInt("eprintid"));
				
			st.close();
			rs.close();
			conn.close();	
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
		return articolo;
	}

}