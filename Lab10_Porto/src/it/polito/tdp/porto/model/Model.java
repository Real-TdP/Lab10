package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	private PortoDAO pDAO = new PortoDAO();
	private List<Author> autori;
	private AuthorIdMap aMap;
	private Graph<Author,DefaultEdge> graph;
	
	public Model() {
		aMap= new AuthorIdMap();
		autori= new ArrayList<Author>(pDAO.getAutori(aMap));
		this.creaGraph(autori);
	}
	
	public List<Author> getAutori(){
		return autori;
	}
	public List<Author> getNoCoAutori(int id) {
		Author autore= this.aMap.getAuthor(id);
		List<Author> noCo = new ArrayList<Author>(autori);
		noCo.remove(autore);
		for(Author a: autori) 
			if(!a.equals(autore)&&this.isCoautore(a, autore))    //if(!a.equals(autore)&&!graph.getAllEdges(a, autore).isEmpty()) 
				noCo.remove(a);
				

				
		return noCo;
	}
	
	/*  SFRUTTA STRINGA SQL
	public String getCoAutori(int id) {  
		StringBuilder sb = new StringBuilder("I Coautori sono: \n\n");
		for(Author a: pDAO.getCoAutoriFromId(id,aMap))
			sb.append(a.toString()+"\n");
		return sb.toString();
	}
	
	*/
	
	public String getCoAutori(int id) {
		Author autore= this.aMap.getAuthor(id);
		StringBuilder sb = new StringBuilder("I Coautori sono: \n\n");
		for(Author a: autori) 
			if(!a.equals(autore)&&this.isCoautore(a, autore))   //if(!a.equals(autore)&&!graph.getAllEdges(a, autore).isEmpty()) 
				sb.append(a.toString()+"\n");
		return sb.toString();
	}

	private void creaGraph(List<Author> autori) {
		graph= new SimpleGraph<Author,DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(graph, autori);
		for(Author a: autori)
			for(Author b:autori)
				if(this.checkConnection(a,b))
					graph.addEdge(a,b);

				
		
		
	}
	
	private boolean isCoautore(Author a,Author b) {
		return graph.getEdge(a,b)!=null;
	}
	public boolean isCoautore(int c,int d) {
		Author a=aMap.getAuthor(c);
		Author b=aMap.getAuthor(d);
		return graph.getEdge(a,b)!=null;
	}

	private boolean checkConnection(Author a, Author autore) {
		if(a.equals(autore))
			return false;
		if(pDAO.checkConn(a,autore))
			return true;
		return false;
	}
	

}
