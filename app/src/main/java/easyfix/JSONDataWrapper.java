package easyfix;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class JSONDataWrapper<T> {
    List<T> data;
    
    public JSONDataWrapper() {
        super();
    }
    
    public JSONDataWrapper(List<T> data) {
        super();
        this.data = data;
    }
    
    public List<T> getData() {
        return data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    
}
