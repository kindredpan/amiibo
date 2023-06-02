package com.pan.plugins.biz.sqlParse.base;


import static com.pan.plugins.base.Constants.AUTO_INCREMENT;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class Column {
    private static final String COMMENT = "    /**\n" +
            "     * %s\n" +
            "     */";
    private static final String PRIMARY = "\n    @TableId(\"%s\")";
    private static final String PRIMARY_AUTO = "\n    @TableId(value = \"%s\", type = IdType.AUTO)";
    private static final String TABLE_FIELD = "\n    @TableField(\"%s\")";
    private static final String FIELD = "\n    private %s %s;";

    public Column(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public Column(String columnName, String columnType, String comment) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.comment = comment;
    }

    private String columnName;
    private String camelName;
    private String columnType;
    private String dataType;
    private String comment;
    private boolean isPrimaryKey;
    private String strategy;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCamelName() {
        return camelName;
    }

    public void setCamelName(String camelName) {
        this.camelName = camelName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String format() {
        StringBuilder str = new StringBuilder();
        // 处理备注
        if (null != this.comment && !"".equals(this.comment.trim())) {
            str.append(String.format(COMMENT, this.comment));
        }
        // 处理注解
        if (isPrimaryKey) {
            String primary = AUTO_INCREMENT.equals(strategy) ? PRIMARY_AUTO : PRIMARY;
            str.append(String.format(primary, columnName));
        } else {
            str.append(String.format(TABLE_FIELD, columnName));
        }
        str.append(String.format(FIELD, dataType, camelName));
        return str.toString();
    }
}
