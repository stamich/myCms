package pl.codecity.main.controller.support;

import pl.codecity.main.model.DomainObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DomainObjectSelect2Model<ID extends Serializable> implements Serializable {
	
	private ID id;
	private String text;
	
	public DomainObjectSelect2Model(DomainObject object) {
		setId((ID) object.getId());
		setText(object.print());
	}
	
	public DomainObjectSelect2Model(ID id, String text) {
		setId(id);
		setText(text);
	}
	
	public ID getId() {
		return id;
	}
	
	public void setId(ID id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || !(other instanceof DomainObjectSelect2Model)) return false;
		if (getId() == null) return false;
		DomainObjectSelect2Model that = (DomainObjectSelect2Model) other;
		return getId().equals(that.getId());
	}
}
