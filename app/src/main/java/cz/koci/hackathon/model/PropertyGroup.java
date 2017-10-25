
package cz.koci.hackathon.model;

import java.util.List;

public class PropertyGroup {

    private String templateId;
    private List<Field> fields = null;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
