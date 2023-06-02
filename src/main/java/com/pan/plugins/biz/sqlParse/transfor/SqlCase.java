package com.pan.plugins.biz.sqlParse.transfor;

import com.pan.plugins.base.BaseCase;
import com.pan.plugins.base.Constants;
import com.pan.plugins.biz.sqlParse.base.Column;
import com.pan.plugins.biz.sqlParse.base.DataType;
import com.pan.plugins.util.CaseUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class SqlCase extends BaseCase {

    private final String content;
    private List<Column> columnList = new ArrayList<>();

    private SqlCase(String content) {
        this.content = content;
    }

    public static SqlCase build(String content) {
        return new SqlCase(content);
    }

    @Override
    public SqlCase spilt() {
        try {
            Statement parse = CCJSqlParserUtil.parse(content);
            standerSqlParse((CreateTable) parse);
        } catch (JSQLParserException e) {
            System.out.println("��׼sql�ı�����ʧ�ܣ�");
            simpleSqlParse();
        }
        return this;
    }

    @Override
    public SqlCase checkCase() {
        return this;
    }

    @Override
    public SqlCase handleCase() {
        columnList.forEach(it -> {
            it.setColumnName(CaseUtil.sqlVarFormat(it.getColumnName()));
            it.setCamelName(CaseUtil.caseCamel(it.getColumnName()));
            it.setDataType(DataType.getDataType(it.getColumnType()));
        });
        return this;
    }

    @Override
    public String toStr() {
        StringBuilder sb = new StringBuilder();
        columnList.forEach(it -> {
            sb.append("\n");
            sb.append(it.format());
        });
        return sb.toString();
    }

    private void standerSqlParse(CreateTable createTable) {
        // ��������
        Set<String> primaryKey = new HashSet<>();
        for (Index index : createTable.getIndexes()) {
            // ��������
            if (Constants.PRIMARY_KEY.equals(index.getType())) {
                primaryKey = index.getColumns().stream()
                        .map(Index.ColumnParams::getColumnName)
                        .collect(Collectors.toSet());
            }
        }
        // �����ֶ�
        List<ColumnDefinition> columnDefinitionList = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitionList) {
            Column column = new Column(columnDefinition.getColumnName(), columnDefinition.getColDataType().getDataType());
            List<String> columnSpecs = columnDefinition.getColumnSpecs();
            // ��������
            if (primaryKey.contains(columnDefinition.getColumnName())) {
                column.setPrimaryKey(true);
            }
            // ������������
            if (columnSpecs.contains(Constants.AUTO_INCREMENT)) {
                column.setStrategy(Constants.AUTO_INCREMENT);
            }
            // ����ע
            if (columnSpecs.contains(Constants.COMMENT)) {
                String comment;
                try {
                    comment = columnSpecs.get(columnSpecs.indexOf(Constants.COMMENT) + 1);
                    column.setComment(comment);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            columnList.add(column);
        }
    }

    private void simpleSqlParse() throws IllegalArgumentException {
        List<Column> columns = CaseUtil.sqlSpilt(this.content);
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("�Ƿ� SQL �ı���\n" + content);
        } else {
            this.columnList = columns;
        }
    }


}
