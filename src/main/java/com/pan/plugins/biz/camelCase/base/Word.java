package com.pan.plugins.biz.camelCase.base;

/**
 * @author panqj
 * @date 2023/05/31
 */

public class Word {

    private String text;
    private Type type;
    private PosType curPosType; // 当前类型
    private PosType tarPosType; // 目标类型
    private boolean isCase = false;

    public Word(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isCase() {
        return isCase;
    }

    public void setCase(boolean aCase) {
        isCase = aCase;
    }

    public PosType getCurPosType() {
        return curPosType;
    }

    public void setCurPosType(PosType curPosType) {
        this.curPosType = curPosType;
    }

    public PosType getTarPosType() {
        return tarPosType;
    }

    public void setTarPosType(PosType tarPosType) {
        this.tarPosType = tarPosType;
    }

    public enum Type {
        WORD,
        SYMBOL,
        IGNORE_SYMBOL;
    }
    public enum PosType {
        RETAIN,
        SYMBOL,
        NORMAL,
        CAMEL,
        UNDER_LINE_SPILT;
    }
}
