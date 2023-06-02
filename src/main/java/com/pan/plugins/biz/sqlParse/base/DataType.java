package com.pan.plugins.biz.sqlParse.base;

/**
 * @author panqj
 * @date 2023/06/01
 */

public enum DataType {
    VARCHAR("VARCHAR", "String"),
    CHAR("CHAR", "String"),
    BLOB("BLOB", "byte[]"),
    TEXT("TEXT", "String"),
    INT("INT", "Long"),
    TINYINT("TINYINT", "Integer"),
    SMALLINT("SMALLINT", "Integer"),
    MEDIUMINT("MEDIUMINT", "Integer"),
    BIT("BIT", "Boolean"),
    BIGINT("BIGINT", "Long"),
    FLOAT("FLOAT", "Float"),
    DOUBLE("DOUBLE", "Double"),
    DECIMAL("DECIMAL", "BigDecimal"),
    BOOLEAN("BOOLEAN", "Boolean"),
    ID("ID", "Long"),
    DATE("DATE", "Date"),
    TIME("TIME", "Time"),
    DATETIME("DATETIME", "Timestamp"),
    TIMESTAMP("TIMESTAMP", "Timestamp"),
    YEAR("YEAR", "Date"),
    ;


    private final String columnType;
    private final String dataType;

    DataType(String columnType, String dataType) {
        this.columnType = columnType;
        this.dataType = dataType;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getDataType() {
        return dataType;
    }

    public static String getDataType(String columnType) {
        final String typeUpperCase = columnType.toUpperCase();
        try {
            DataType type = DataType.valueOf(typeUpperCase);
            return type.getDataType();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return VARCHAR.getDataType();
        }
    }
}
