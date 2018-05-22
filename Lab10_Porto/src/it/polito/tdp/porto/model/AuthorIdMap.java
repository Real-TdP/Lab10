package it.polito.tdp.porto.model;

import java.util.HashMap;
import java.util.Map;


public class AuthorIdMap {
	private Map<Integer,Author> map;

	public AuthorIdMap() {
		map = new HashMap<Integer,Author>();
	}
	
	public Author getAuthor(int authorId) {
		return map.get(authorId);
	}
	
	public Author get(Author author) {
		Author old = map.get(author.getId());
		if (old == null) {
			map.put(author.getId(), author);
			return author;
		}
		return old;
	}
	
	public void put(Author author, int authorId) {
		map.put(authorId, author);
	}

}
