package pl.codecity.main.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "popular_post", uniqueConstraints = @UniqueConstraint(columnNames = {"language", "type", "rank"}))
@DynamicInsert
@DynamicUpdate
@Indexed
public class PopularPost extends DomainObject<Long> implements Comparable<PopularPost> {

	public enum Type {
		DAILY,
		WEEKLY,
		MONTHLY,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Field(name = "sortId", analyze = Analyze.NO, index = org.hibernate.search.annotations.Index.NO)
	@SortableField(forField = "sortId")
	private long id;

	@Column(length = 3, nullable = false)
	@Field
	private String language;

	@Enumerated(EnumType.STRING)
	@Column(length = 50, nullable = false)
	@Field
	private Type type;

	@Column(nullable = false)
	private int rank;

	@Column(nullable = false)
	private long views;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@IndexedEmbedded(includeEmbeddedObjectId = true)
	private Post post;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	@Override
	public String print() {
		return getType() + " " + Integer.toString(getRank());
	}

	@Override
	public int compareTo(PopularPost o) {
		return new CompareToBuilder()
				.append(getLanguage(), o.getLanguage())
				.append(getType(), o.getType())
				.append(getRank(), o.getRank())
				.append(getId(), o.getId())
				.toComparison();
	}
}
