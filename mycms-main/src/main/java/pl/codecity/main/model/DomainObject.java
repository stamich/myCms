package pl.codecity.main.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.lucene.analysis.cjk.CJKWidthFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ja.JapaneseBaseFormFilterFactory;
import org.apache.lucene.analysis.ja.JapaneseKatakanaStemFilterFactory;
import org.apache.lucene.analysis.ja.JapaneseTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymFilterFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@AnalyzerDef(name = "synonyms",
		tokenizer = @TokenizerDef(factory = JapaneseTokenizerFactory.class, params = @Parameter(name = "userDictionary", value = "userdict.txt")),
		filters = {@TokenFilterDef(factory = SynonymFilterFactory.class, params = {
						@Parameter(name = "synonyms", value = "synonyms.txt"),
						@Parameter(name = "userDictionary", value = "userdict.txt"),
						@Parameter(name = "ignoreCase", value = "true"),
						@Parameter(name = "expand", value = "true"),
						@Parameter(name = "tokenizerFactory", value = "org.apache.lucene.analysis.ja.JapaneseTokenizerFactory")}),
				@TokenFilterDef(factory = JapaneseBaseFormFilterFactory.class),
				@TokenFilterDef(factory = CJKWidthFilterFactory.class),
				@TokenFilterDef(factory = JapaneseKatakanaStemFilterFactory.class, params = {
						@Parameter(name = "minimumLength", value = "4")}),
				@TokenFilterDef(factory = LowerCaseFilterFactory.class)})
@SuppressWarnings("serial")
public abstract class DomainObject<ID extends Serializable> implements Serializable {

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(length = 100)
	private String createdBy;

	@Column(nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();

	@Column(length = 100)
	private String updatedBy;

	public abstract ID getId();

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public abstract String print();

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || !(other instanceof DomainObject)) return false;
		DomainObject that = (DomainObject) other;
		return new EqualsBuilder().append(getId(), that.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + getId() + "]";
	}
}
