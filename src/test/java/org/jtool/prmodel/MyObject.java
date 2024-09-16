package org.jtool.prmodel;

import java.util.Date;

public class MyObject {
    
    private Date createDate;
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    @Override
    public String toString() {
        return "MyObject{" +
                "createDate=" + createDate +
                '}';
    }
}
