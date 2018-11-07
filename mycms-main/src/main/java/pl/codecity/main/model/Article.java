package pl.codecity.main.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@NamedEntityGraphs({@NamedEntityGraph(name = Article.SHALLOW_GRAPH_NAME, attributeNodes = {
						@NamedAttributeNode("cover"),
						@NamedAttributeNode("author"),
						@NamedAttributeNode("drafted"),
						@NamedAttributeNode("categories"),
						@NamedAttributeNode("tags")}),
		@NamedEntityGraph(name = Article.DEEP_GRAPH_NAME, attributeNodes = {
						@NamedAttributeNode("cover"),
						@NamedAttributeNode("author"),
						@NamedAttributeNode("drafted"),
						@NamedAttributeNode("categories"),
						@NamedAttributeNode("tags"),
						@NamedAttributeNode("relatedToPosts"),
						@NamedAttributeNode(value = "customFieldValues", subgraph = "customFieldValue")}, subgraphs =  {
						@NamedSubgraph(name = "customFieldValue", attributeNodes = { @NamedAttributeNode("customField")})})})
@Table(name = "article")
@DynamicInsert
@DynamicUpdate
@Analyzer(definition = "synonyms")
@Indexed
@SuppressWarnings("serial")
public class Article extends Post implements Comparable<Article> {

	public static final String SHALLOW_GRAPH_NAME = "ARTICLE_SHALLOW_GRAPH";
	public static final String DEEP_GRAPH_NAME = "ARTICLE_DEEP_GRAPH";

	@Override
	public int compareTo(Article article) {
		if (getDate() != null && article.getDate() == null) return 1;
		if (getDate() == null && article.getDate() != null) return -1;
		if (getDate() != null && article.getDate() != null) {
			int r = getDate().compareTo(article.getDate());
			if (r != 0) return r * -1;
		}
		return (int) (article.getId() - getId());
	}
}
