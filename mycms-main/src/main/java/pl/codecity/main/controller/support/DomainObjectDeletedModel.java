package pl.codecity.main.controller.support;

import pl.codecity.main.model.DomainObject;

import java.io.Serializable;

public class DomainObjectDeletedModel<ID extends Serializable> implements Serializable {

	private ID id;

	public DomainObjectDeletedModel(DomainObject<ID> object) {
		this.id = object.getId();
	}

	public ID getId() {
		return id;
	}
}
