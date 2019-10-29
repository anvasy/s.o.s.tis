package bsuir.ai.sostis.model;

import javax.persistence.Entity;

@Entity
public class Summary {
    private String source;
    private String essay;

    public String getEssay() {
        return essay;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
