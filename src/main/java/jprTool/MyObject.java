package jprTool;

import java.util.Date;

public class MyObject {
	private Date createDate;

    // 其他字段和方法

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
