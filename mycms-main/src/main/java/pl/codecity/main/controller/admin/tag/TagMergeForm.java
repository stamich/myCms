package pl.codecity.main.controller.admin.tag;

import pl.codecity.main.request.TagMergeRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class TagMergeForm implements Serializable {

    @NotNull
    private List<Long> ids;
    @NotNull
    private String name;
    @NotNull
    private String language;

    public TagMergeForm(){}

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagMergeRequest toTagMergeRequest() {
        TagMergeRequest request = new TagMergeRequest();
        request.setIds(getIds());
        request.setName(getName());
        request.setLanguage(getLanguage());
        return request;
    }
}
